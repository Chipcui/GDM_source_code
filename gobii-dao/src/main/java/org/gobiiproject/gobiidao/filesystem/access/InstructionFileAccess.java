package org.gobiiproject.gobiidao.filesystem.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by araquel on 3/16/2017.
 */
public class InstructionFileAccess<T> {

    private final String LOADER_FILE_EXT = ".json";


    Class<T> instanceType;

    public InstructionFileAccess(Class instanceType) {
        this.instanceType = instanceType;
    }

    public Boolean writeInstructions(String instructionFileFqpn,
                                    T instructions) throws GobiiDaoException {

         Boolean returnVal = null;

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
                    throw new GobiiDaoException("Path of specified instruction file does not exist: "
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


    public T getInstruction(String instructionFileFqpn) {

        T returnVal = null;

        try {

            File file = new File(instructionFileFqpn);

            FileInputStream fileInputStream = new FileInputStream(file);

            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();

            returnVal = objectMapper.readValue(fileInputStream, instanceType);

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;
            throw new GobiiDaoException(message);
        }

        return returnVal;

    }

    // it is irritating that we seem to need a separate function that does everything the same as getINstruction()
    // except except the type parameterization for the List type. There is probably a more elegant and hence
    // less redundant way tot do this; however, for now, encapsulating this code in one class is a huge improvement
    // over the way things were.
    public List<T> getInstructions(String instructionFileFqpn, Class<T[]> listType) throws GobiiDaoException {

        List<T> returnVal = null;

        try {

            T[] instructions = null;

            File file = new File(instructionFileFqpn);

            FileInputStream fileInputStream = new FileInputStream(file);

            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();

            instructions = objectMapper.readValue(fileInputStream, listType);

            returnVal = Arrays.asList(instructions);

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;

            throw new GobiiDaoException(message);
        }

        return returnVal;

    }

}
