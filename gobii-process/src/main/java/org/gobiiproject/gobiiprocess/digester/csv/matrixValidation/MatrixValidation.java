package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.LoaderGlobalConfigs;

public class MatrixValidation {

    private final String datasetType, missingFile, markerFile;
    private int noOfElements;
    private IUPACmatrixToBi iupacMatrixToBi;
    private RowProcessor diploidProcessor;
    private MatrixErrorUtil matrixErrorUtil;
    private NucleotideSeparatorSplitter tetraploidSplitter;

    public MatrixErrorUtil getMatrixErrorUtil() {
        return matrixErrorUtil;
    }

    public int getErrorCount() {
        return matrixErrorUtil.getErrorCount();
    }

    public boolean stopProcessing() {
        return matrixErrorUtil.stopProcessing();
    }

    public MatrixValidation(String datasetType, String missingFile, String markerFile) {
        this.datasetType = datasetType;
        this.missingFile = missingFile;
        this.markerFile = markerFile;
        matrixErrorUtil = new MatrixErrorUtil();
        noOfElements = 0;
        iupacMatrixToBi = new IUPACmatrixToBi();
    }


    public boolean setUp() {
        List<String> missingFileElements = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(missingFile))) {
            stream.forEach(missingFileElements::add);
        } catch (IOException e) {
            matrixErrorUtil.incrementErrorCount();
            Logger.logError("SNPSepRemoval", "Exception in reading SNSSepRemoval missing values.");
            return false;
        }

        //Set of missing file entries, in lowercase to enable easy case insensitive mapping
        Set<String> missingFileSet = missingFileElements.stream().map(String::toLowerCase).collect(Collectors.toSet());

	    if(LoaderGlobalConfigs.isNewTwoLetterNucleotideParse()){
		    diploidProcessor =  new NucleotideSeparatorSplitter(2,missingFileSet);
	    }
	    else {
		    diploidProcessor = new SNPSepRemoval(missingFileElements);
	    }

	    tetraploidSplitter = new NucleotideSeparatorSplitter(4,missingFileSet);
        return true;
    }

    public ValidationResult validate(int rowNo, int rowOffset, List<String> inputRowList, List<String> outputRowList, boolean isVCF, boolean skipValidation ) {
        ValidationResult ret = new ValidationResult(false,0);
        if (noOfElements == 0) noOfElements = inputRowList.size();
        else if (noOfElements != inputRowList.size()) {
            matrixErrorUtil.setError("Exception in processing matrix file. Irregular size matrix. Expected: " + noOfElements + " actual: " + inputRowList.size());
            return ret.success(false);
        }

        ret.numRows=noOfElements;

        /*
         *   IUPAC conversion
         *   Tried to convert IUPAC to BI.
         *   If it fails for a row that semi processed row is added.
         *   Reason being, as it is already a failure we are processing further to identify all errors once.
         *
         * */
	    if (datasetType.equalsIgnoreCase("IUPAC"))
		    if (!iupacMatrixToBi.process(rowNo + rowOffset, inputRowList, outputRowList, matrixErrorUtil)) {
			    return ret.success(false);
		    }
      
	    if (datasetType.equalsIgnoreCase("NUCLEOTIDE_4_LETTER")){
		    if (!tetraploidSplitter.process(rowNo + rowOffset, inputRowList, outputRowList, matrixErrorUtil)) {
			    return ret.success(false);
		    }
	    }
        /*
         *   SNP sep removal
         *   Tried SNP Separator Removal.
         *   If it fails for a row that semi processed row is added.
         *   Reason being, as it is already a failure we are processing further to identify all errors once.
         *
         * */
        if (datasetType.equalsIgnoreCase("NUCLEOTIDE_2_LETTER") && !isVCF)
            if (!diploidProcessor.process(rowNo + rowOffset, inputRowList, outputRowList, matrixErrorUtil)) {
                return ret.success(false);
            }

        if (isVCF) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(markerFile)))) {
                int lineNo = 0;
                int refPos = -1, altPos = -1;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (lineNo == 0) {
                        List<String> headerLine = Arrays.asList(line.split("\t"));
                        for (int i = 0; i < headerLine.size(); i++) {
                            if (headerLine.get(i).equalsIgnoreCase("ref"))
                                refPos = i;
                            if (headerLine.get(i).equalsIgnoreCase("alts"))
                                altPos = i;
                        }
                        if (refPos == -1 || altPos == -1) {
                            matrixErrorUtil.setError("Exception in processing matrix file. Marker file does not contain ref and alt columns.");
                            return ret.success(false);
                        }
                        lineNo++;
                    }// As data in the vcf file starts from line with is not zero(generally 10). rowNo will be offset by this amount(10). So we substract offset from rowNo for comparison.
                    // As there is a single header line in digest.marker we are adding one.
                    else if (lineNo == rowNo - (rowOffset) + 1) {
                        List<String> dataLine = Arrays.asList(line.split("\t"));
                        String newLine = dataLine.get(refPos) + "\t" + dataLine.get(altPos);
                        if (!VCFTransformer.transformVCFLine(newLine, rowNo, inputRowList, outputRowList, matrixErrorUtil)) {
                            //note, outputRowList here is now a two letter file, which needs to be validated
                            matrixErrorUtil.setError("Exception in processing matrix file. Failed in VCF Transformer.");
                            return ret.success(false);
                        }
                        lineNo++;
                    } else lineNo++;
                }

            } catch (FileNotFoundException e) {
                matrixErrorUtil.setError("Could not find marker file");
                return ret.success(false);
            } catch (IOException e) {
                matrixErrorUtil.setError("Could not read marker file" + e.getMessage());
                return ret.success(false);
            }
        }

        /*
         * VALIDATION.
         * Validates each element is a valid value or not based on the datasetType.
         * */
        if (datasetType.equalsIgnoreCase("NUCLEOTIDE_4_LETTER") || datasetType.equalsIgnoreCase("DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("IUPAC") ||
                datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("SSR_ALLELE_SIZE") ||
                datasetType.equalsIgnoreCase("NUCLEOTIDE_2_LETTER") || datasetType.equalsIgnoreCase("VCF")) {

            //Pass through to output
            if (datasetType.equalsIgnoreCase("DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("SSR_ALLELE_SIZE"))
                outputRowList.addAll(inputRowList);

            if(skipValidation){
                return ret.success(true);
            }

            boolean datasetValidationStatus=DigestMatrix.validateDatasetList(rowNo + rowOffset, outputRowList, datasetType, matrixErrorUtil);
            return ret.success(datasetValidationStatus);
        } else return ret.success(false);
    }
}


