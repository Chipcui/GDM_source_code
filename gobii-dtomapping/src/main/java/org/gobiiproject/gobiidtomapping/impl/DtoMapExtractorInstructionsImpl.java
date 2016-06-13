package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.ExtractorInstructionsDAO;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapExtractorInstructionsImpl implements DtoMapExtractorInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapExtractorInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private ExtractorInstructionsDAO extractorInstructionsDAO;

    private void createDirectories(String instructionFileDirectory,
                                   GobiiDataSetExtract gobiiDataSetExtract) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            if (!extractorInstructionsDAO.doesPathExist(instructionFileDirectory)) {
                extractorInstructionsDAO.makeDirectory(instructionFileDirectory);
            }
        }

    } // createDirectories()


    @Override
    public ExtractorInstructionFilesDTO writeInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO) {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileDirectory = configSettings
                    .getCurrentCropConfig()
                    .getInstructionFilesDirectory();

            String instructionFileFqpn = instructionFileDirectory
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            for (GobiiExtractorInstruction currentExtractorInstruction :
                    extractorInstructionFilesDTO.getGobiiExtractorInstructions()) {

                boolean allValuesSpecified = true;
                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    allValuesSpecified = false;
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                            "instruction file name is missing");
                }

                for (GobiiDataSetExtract currentGobiiDataSetExtract :
                        currentExtractorInstruction.getDataSetExtracts()) {

                    // check that we have all required values
                    if (LineUtils.isNullOrEmpty(currentGobiiDataSetExtract.getDataSetName())) {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                                "DataSet name is missing");
                    }

                    if (LineUtils.isNullOrEmpty(Integer.toString(currentGobiiDataSetExtract.getDataSetId()))) {
                        allValuesSpecified = false;
                        returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.MISSING_REQUIRED_VALUE,
                                "Dataset ID is missing");
                    }

                }



            } // iterate instructions/files

            if (0 ==
                    returnVal
                            .getDtoHeaderResponse()
                            .getStatusMessages()
                            .stream()
                            .filter(m -> m.getStatusLevel().equals(DtoHeaderResponse.StatusLevel.ERROR))
                            .collect(Collectors.toList())
                            .size()
                    ) {


                extractorInstructionsDAO.writeInstructions(instructionFileFqpn,
                        returnVal.getGobiiExtractorInstructions());
            }


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    } // writeInstructions

    @Override
    public ExtractorInstructionFilesDTO readInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO) {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileFqpn = configSettings
                    .getCurrentCropConfig()
                    .getInstructionFilesDirectory()
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            if (extractorInstructionsDAO.doesPathExist(instructionFileFqpn)) {


                List<GobiiExtractorInstruction> instructions =
                        extractorInstructionsDAO
                                .getInstructions(instructionFileFqpn);

                if (null != instructions) {
                    extractorInstructionFilesDTO.setGobiiExtractorInstructions(instructions);
                } else {
                    returnVal.getDtoHeaderResponse()
                            .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                    DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                    "The instruction file exists, but could not be read: " +
                                            instructionFileFqpn);
                }

            } else {

                returnVal.getDtoHeaderResponse()
                        .addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                                DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST,
                                "The specified instruction file does not exist: " +
                                        instructionFileFqpn);

            } // if-else instruction file exists

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
