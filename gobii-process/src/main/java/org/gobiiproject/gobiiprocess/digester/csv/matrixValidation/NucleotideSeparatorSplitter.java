package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * A separation routine for multi-allelic nucleotides for GDM-446. Takes a delimited string and converts it into an
 * undelimited N character string of alleles, with N being the number of alleles its looking for.
 */
public class NucleotideSeparatorSplitter implements RowProcessor {

    private int nucleotideCount;
    private static String UNKNOWN_ALLELE = "N";
    private String unknownSegment;

    private static Set<String> validSeparators = new HashSet<>(Arrays.asList(
            ",",
            "/",
            "|"
    ));

    private Set<String> missingSegmentsFromFile;

    public NucleotideSeparatorSplitter(int nucleotideCount, Set<String> missingSegmentsFromFile){
        this.nucleotideCount = nucleotideCount;
        this.missingSegmentsFromFile = missingSegmentsFromFile;
        this.unknownSegment = StringUtils.repeat(UNKNOWN_ALLELE,nucleotideCount);
    }

    public boolean process(int rowNo, List<String> inrow, List<String> outrow, MatrixErrorUtil matrixErrorUtil) {
        boolean returnStatus = true;
        for (String element : inrow) {
            if (element.length() < 1) {
                String errMsg = "SNPSepRemoval Unsupported empty Allele Call in row " + rowNo;
                matrixErrorUtil.setError(errMsg);
                returnStatus = false;
            } else {
                String result=unknownSegment;
                String error = null;
                if(missingSegmentsFromFile.contains(element.toLowerCase())){
                    //noinspection ConstantConditions - This is for readability
                    result = unknownSegment;
                }else{
                    error = validateInputElement(element);
                    if(error==null) {
                        result = processInputElement(element);
                        error = validateOutputElement(result);
                    }
                }
                outrow.add(result);

                if (error!=null) {
                    String baseErrMsg = error + "  in row " + rowNo;
                    matrixErrorUtil.setError(baseErrMsg);
                    returnStatus = false;
                }
            }
        }
        return returnStatus;
    }

    // Important assumptions - element is already whitespace stripped.
    //missingSegmentsFromFile is all lowercase, so 'contains' becomes case insensitive

    /**
     * Returns null on success, or error message on failure. Checks pre-split element maintains some basic characteristics.
     * @param element pre-processing input element
     * @return null on success, error message on failure
     */
    private String validateInputElement(String element){
        int expectedLengthWithSeparators = (nucleotideCount * 2) - 1;
        int length = element.length();
        boolean hasSeparators=(length != nucleotideCount);
        if((length != nucleotideCount) && (length != expectedLengthWithSeparators)){
            return "Unexpected Length Element " + element; // incorrect length
        }

        if(hasSeparators) {
            char separatorCharacter = element.charAt(1);
            if(!validSeparators.contains(""+separatorCharacter)){
                return "Unexpected separator in " + element + ": " + separatorCharacter + " Expected:" + Arrays.deepToString(validSeparators.toArray()); // incorrect separator character OR wrongly sized element
                //Expected separator from <list>, received garbage
            }
            for(int i = 1; i < element.length();i+=2){
                if(element.charAt(i) != separatorCharacter){
                    return "Unexpected character in separator slot in " + element + ": " + element.charAt(i) + " Expected:" + separatorCharacter; // incorrect separator character OR wrongly sized element
                    //Expected more of the same separator, received garbage
                }
            }
        }

        return null;
    }

    /**
     * Removes separators from input elements
     * @param element input element
     * @return processed input element
     */
    private String processInputElement(String element){
        int length = element.length();
        boolean hasSeparators=((length != nucleotideCount));
        if(hasSeparators) { //If nucleotideCount=1, there's no separator character
            char separator = element.charAt(1);
            return element.replaceAll("" + separator, "");
        }
        else return element;
    }

    /**
     * Returns null on success, or error message on failure. Checks element after split is still the right length
     * @param element post-processing input element
     * @return null on success, error message on failure
     */
    private String validateOutputElement(String element) {
        if (element.length() != nucleotideCount) {
            return "Unexpected Segment Count after split in " + element;
        }
        return null;
    }
}