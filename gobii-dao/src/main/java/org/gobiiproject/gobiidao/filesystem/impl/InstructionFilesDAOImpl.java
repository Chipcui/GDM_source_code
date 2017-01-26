package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.InstructionFilesDAO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Phil on 4/12/2016.
 * Modified by Angel 12/12/2016
 */
public class InstructionFilesDAOImpl implements InstructionFilesDAO {

    private final String LOADER_FILE_EXT = ".json";

    @Override
    public boolean writeInstructions(String instructionFileFqpn,
                                     List<GobiiExtractorInstruction> instructions) throws GobiiDaoException {
        boolean returnVal = false;
        try {

            File instructionFile = new File(instructionFileFqpn);
            if (!instructionFile.exists()) {

                String filePath = instructionFile.getAbsolutePath().
                        substring(0, instructionFile.getAbsolutePath().lastIndexOf(File.separator));

                File destinationDirectory = new File(filePath);

                if (destinationDirectory.exists()) {

                    if (destinationDirectory.isDirectory()) {

                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                        objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
                        String instructionsAsJson = objectMapper.writeValueAsString(instructions);
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(instructionFileFqpn));
                        bufferedWriter.write(instructionsAsJson);
                        bufferedWriter.flush();
                        bufferedWriter.close();

                        returnVal = true;
                    } else {
                        throw new GobiiDaoException("Path of specified instruction file name is not a directory: "
                                + destinationDirectory);
                    } // if-else directory is really a directory

                } else {
                    throw new GobiiDaoException("Path of specified instruction file directory does not exist: "
                            + destinationDirectory);

                } // if-else destination directory exists

            } else {

                throw new GobiiDaoException("The specified instruction file already exists: "
                        + instructionFileFqpn);
            } // if-else file does not arleady exist

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;
            throw new GobiiDaoException(message);
        }
        return returnVal;
    } // writeInstructions

    @Override
    public boolean doesPathExist(String pathName) throws GobiiDaoException {
        return new File(pathName).exists();
    }

    @Override
    public void verifyDirectoryPermissions(String pathName) throws GobiiDaoException {

        File pathToCreate = new File(pathName);
        if (!pathToCreate.canRead() && !pathToCreate.setReadable(true, false)) {
            throw new GobiiDaoException("Unable to set read permissions on directory " + pathName);
        }

        if (!pathToCreate.canWrite() && !pathToCreate.setWritable(true, false)) {
            throw new GobiiDaoException("Unable to set write permissions on directory " + pathName);
        }
    }


    @Override
    public void makeDirectory(String pathName) throws GobiiDaoException {

        if (!doesPathExist(pathName)) {

            File pathToCreate = new File(pathName);

            if (!pathToCreate.mkdirs()) {
                throw new GobiiDaoException("Unable to create directory " + pathName);
            }

            if ((!pathToCreate.canRead()) && !(pathToCreate.setReadable(true, false))) {
                throw new GobiiDaoException("Unable to set read on directory " + pathName);
            }

            if ((!pathToCreate.canWrite()) && !(pathToCreate.setWritable(true, false))) {
                throw new GobiiDaoException("Unable to set write on directory " + pathName);
            }




        } else {
            throw new GobiiDaoException("The specified path already exists: " + pathName);
        }
    }

    @Override
    public List<GobiiExtractorInstruction> setExtractorGobiiJobStatus(boolean applyToAll, List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir gobiiFileProcessDir) throws GobiiDaoException{
        List<GobiiExtractorInstruction> returnVal = instructions;

        GobiiJobStatus gobiiJobStatus;

        switch (gobiiFileProcessDir) {

            case EXTRACTOR_INPROGRESS:
                gobiiJobStatus = GobiiJobStatus.IN_PROGRESS;
                break;

            case EXTRACTOR_INSTRUCTIONS:
                gobiiJobStatus = GobiiJobStatus.STARTED;
                break;

            case EXTRACTOR_DONE:
                gobiiJobStatus = GobiiJobStatus.COMPLETED;
                break;

            default:
                gobiiJobStatus = GobiiJobStatus.FAILED;
        }

        if(applyToAll){

            for(GobiiExtractorInstruction instruction : returnVal){

                for(GobiiDataSetExtract dataSetExtract: instruction.getDataSetExtracts()){

                    dataSetExtract.setGobiiJobStatus(gobiiJobStatus);
                }
            }
        }else{ //check if the output file(s) exist in the directory specified by the *extractDestinationDirectory* field of the *DataSetExtract* item in the instruction file;
            GobiiJobStatus statusFailed = GobiiJobStatus.FAILED;

            for(GobiiExtractorInstruction instruction: returnVal){

                for(GobiiDataSetExtract dataSetExtract: instruction.getDataSetExtracts()){

                    String extractDestinationDirectory = dataSetExtract.getExtractDestinationDirectory();

                    List<String> datasetExtractFiles =  new ArrayList<String>();

                    String fileName="DS"+ Integer.toString(dataSetExtract.getDataSetId());

                    switch (dataSetExtract.getGobiiFileType()) {
                        case GENERIC:
                            //fileNames.add(fileName+".txt"); to be added
                            break;

                        case HAPMAP:
                            datasetExtractFiles.add(fileName+"hmp.txt");
                            break;

                        case FLAPJACK:
                            datasetExtractFiles.add(fileName+".map");

                            datasetExtractFiles.add(fileName+".genotype");

                            break;

                        case VCF:
                            //fileNames.add(fileName+"hmp.txt"); to be added
                            break;

                        default:
                            throw new GobiiDaoException("Noe extension assigned for GobiiFileType: " + dataSetExtract.getGobiiFileType().toString());
                    }


                        for(String s: datasetExtractFiles){

                            String currentExtractFile = extractDestinationDirectory+s;

                            if(doesPathExist(currentExtractFile))dataSetExtract.setGobiiJobStatus(gobiiJobStatus);

                            else dataSetExtract.setGobiiJobStatus(statusFailed);
                        }
                }
            }
        }
        return returnVal;
    }

    @Override
    public InstructionFileAcess<T> getGobiiInstructionsFromFile(String instructionFileFqpn) throws GobiiDaoException {

        List<GobiiExtractorInstruction> returnVal = null;

        try {

            GobiiExtractorInstruction[] instructions = null;

            File file = new File(instructionFileFqpn);

            FileInputStream fileInputStream = new FileInputStream(file);

            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();

            instructions = objectMapper.readValue(fileInputStream, GobiiExtractorInstruction[].class);

            returnVal = Arrays.asList(instructions);

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;

            throw new GobiiDaoException(message);
        }

        return returnVal;

    }


    @Override
    public List<List<String>> getFilePreview(File file, String fileFormat) {
        List<List<String>> returnVal = new ArrayList<List<String>>();
        Scanner input = new Scanner(System.in);
        try {
            int lineCtr = 0; //count lines read
            input = new Scanner(file);

            while (input.hasNextLine() && lineCtr < 50) { //read first 50 lines only
                int ctr = 0; //count words stored
                List<String> lineRead = new ArrayList<String>();
                String line = input.nextLine();
                for (String s : line.split(getDelimiterFor(fileFormat))) {
                    if (ctr == 50) break;
                    else {
                        lineRead.add(s);
                        ctr++;
                    }
                }
                returnVal.add(lineRead);
                lineCtr++;
            }
            input.close();
        } catch (FileNotFoundException e) {
            throw new GobiiDaoException("Cannot find file. " + e.getMessage());
        }

        return returnVal;
    }

    private String getDelimiterFor(String fileFormat) {
        String delimiter;
        switch (fileFormat) {
            case "csv":
                delimiter = ",";
                break;
            case "txt":
                delimiter = "\t";
                break;
            case "vcf":
                delimiter = "\t";
                break;
            default:
                throw new GobiiDaoException("File Format not supported: " + fileFormat);
        }
        return delimiter;
    }

} // InstructionFilesDAOImpl
