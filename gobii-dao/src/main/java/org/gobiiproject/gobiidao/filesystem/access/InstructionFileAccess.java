package org.gobiiproject.gobiidao.filesystem.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by araquel on 1/26/2017.
 */
public class InstructionFileAccess<T> {

    private List<T> instructions = new ArrayList<>();

    public InstructionFileAccess(Class<T> dtoType) {

    }

    public List<T> getGobiiInstructionsFromFile(String instructionFileFqpn, Class<T> dtoType) throws GobiiDaoException {

        T returnVal;

        try {

            File file = new File(instructionFileFqpn);

            //FileInputStream fileInputStream = new FileInputStream(file);

            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
z
            returnVal = objectMapper.readValue(file, dtoType);

            //returnVal = Arrays.asList(instructions);//

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;

            throw new GobiiDaoException(message);
        }

        return returnVal;

    }

    public List<T> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<T> instructions) {
        this.instructions = instructions;
    }

}
