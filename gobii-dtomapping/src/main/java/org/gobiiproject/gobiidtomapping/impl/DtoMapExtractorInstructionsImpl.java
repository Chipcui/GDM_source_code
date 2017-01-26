package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.InstructionFilesDAO;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 * Modified by Angel 12/12/2016
 */
public class DtoMapExtractorInstructionsImpl implements DtoMapExtractorInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapExtractorInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private InstructionFilesDAO instructionFilesDAO;

    @Autowired
    DtoMapContact dtoMapContact;

    private void createDirectories(String instructionFileDirectory) throws GobiiDaoException {


        if (null != instructionFileDirectory) {

            if (!instructionFilesDAO.doesPathExist(instructionFileDirectory)) {

                instructionFilesDAO.makeDirectory(instructionFileDirectory);

            } else {
                instructionFilesDAO.verifyDirectoryPermissions(instructionFileDirectory);
            }
        }

    } // createDirectories()


    @Override
    public ExtractorInstructionFilesDTO writeInstructions(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws GobiiException {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

//            String currentGobiiCropType = extractorInstructionFilesDTO.getCropType();
//            if (null == currentGobiiCropType) {
//                throw new Exception("Extractor instruction request does not specify a crop");
//            }

            String instructionFileDirectory = configSettings.getProcessingPath(cropType,
                    GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS);

            createDirectories(instructionFileDirectory);

            String instructionFileFqpn = instructionFileDirectory
                    + extractorInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;


            for (GobiiExtractorInstruction currentExtractorInstruction :
                    extractorInstructionFilesDTO.getGobiiExtractorInstructions()) {

                if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "instruction file name is missing");
                }

                if( LineUtils.isNullOrEmpty(currentExtractorInstruction.getGobiiCropType())) {

                    currentExtractorInstruction.setGobiiCropType(cropType);
                }

                if (null != currentExtractorInstruction.getContactId() && currentExtractorInstruction.getContactId() > 0) {

                    ContactDTO contactDTO = dtoMapContact.getContactDetails(currentExtractorInstruction.getContactId());


                    if (!LineUtils.isNullOrEmpty(contactDTO.getEmail())) {

                        currentExtractorInstruction.setContactEmail(contactDTO.getEmail());

                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "The contact record for contactId "
                                        + currentExtractorInstruction.getContactId()
                                        + " does not have an email address");
                    }

                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                            "contactId is missing");
                }

                String extractionFileDestinationPath = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_OUTPUT);


                for (GobiiDataSetExtract currentGobiiDataSetExtract :
                        currentExtractorInstruction.getDataSetExtracts()) {

                    // check that we have all required values
                    if (LineUtils.isNullOrEmpty(currentGobiiDataSetExtract.getDataSetName())) {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "DataSet name is missing");
                    }

                    if (LineUtils.isNullOrEmpty(Integer.toString(currentGobiiDataSetExtract.getDataSetId()))) {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                                "Dataset ID is missing");
                    }

                    String formatName = currentGobiiDataSetExtract.getGobiiFileType().toString().toLowerCase();

                    String dataSetId = currentGobiiDataSetExtract.getDataSetId().toString();

                    String extractorFileDestinationLocation =
                            extractionFileDestinationPath
                                    + formatName
                                    + "/"
                                    + "ds_"
                                    + dataSetId
                                    + "/";

                    if (!instructionFilesDAO.doesPathExist(extractorFileDestinationLocation)) {

                        instructionFilesDAO.makeDirectory(extractorFileDestinationLocation);

                    } else {
                        instructionFilesDAO.verifyDirectoryPermissions(extractorFileDestinationLocation);
                    }


                    currentGobiiDataSetExtract.setExtractDestinationDirectory(extractorFileDestinationLocation);

                }
            } // iterate instructions/files

            if (!instructionFilesDAO.doesPathExist(instructionFileFqpn)) {

                if(instructionFilesDAO.writeInstructions(instructionFileFqpn,
                        returnVal.getGobiiExtractorInstructions())){

                    returnVal.setJobId(extractorInstructionFilesDTO.getInstructionFileName());
                }
            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                        "The specified instruction file already exists: " + instructionFileFqpn);
            }


        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }


        return returnVal;

    } // writeInstructions

    @Override
    public ExtractorInstructionFilesDTO getStatus(String cropType, String instructionFileName) throws GobiiException {

        ExtractorInstructionFilesDTO returnVal = new ExtractorInstructionFilesDTO();

        ConfigSettings configSettings = new ConfigSettings();
        try {

            String fileDirExtractorInProgressFqpn = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_INPROGRESS)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            String fileDirExtractorInstructionsFqpn = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            String fileDirExtractorDoneFqpn = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.EXTRACTOR_DONE)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            returnVal.setJobId(instructionFileName);

            returnVal.setInstructionFileName(instructionFileName);

            List<GobiiExtractorInstruction> gobiiExtractorInstructionsWithStatus;
            if (instructionFilesDAO.doesPathExist(fileDirExtractorInProgressFqpn)){
            //check if file  is in InProgress

                List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFilesDAO
                        .getGobiiExtractorInstructionsFromFile(fileDirExtractorInProgressFqpn);

                gobiiExtractorInstructionsWithStatus = setGobiiExtractorInstructionsStatus(gobiiExtractorInstructionsFromFile,
                        GobiiFileProcessDir.EXTRACTOR_INPROGRESS);

                returnVal.setGobiiExtractorInstructions(gobiiExtractorInstructionsWithStatus);


            } else if (instructionFilesDAO.doesPathExist(fileDirExtractorInstructionsFqpn)) {
            //check if file just started

                List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFilesDAO
                        .getGobiiExtractorInstructionsFromFile(fileDirExtractorInstructionsFqpn);

                gobiiExtractorInstructionsWithStatus =  setGobiiExtractorInstructionsStatus(gobiiExtractorInstructionsFromFile,
                        GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS);

                returnVal.setGobiiExtractorInstructions(gobiiExtractorInstructionsWithStatus);

            } else if (instructionFilesDAO.doesPathExist(fileDirExtractorDoneFqpn)){
            //check if file  is already done

                List<GobiiExtractorInstruction> gobiiExtractorInstructionsFromFile = instructionFilesDAO
                        .getGobiiExtractorInstructionsFromFile(fileDirExtractorDoneFqpn);

                gobiiExtractorInstructionsWithStatus = setGobiiExtractorInstructionsStatus(gobiiExtractorInstructionsFromFile,
                        GobiiFileProcessDir.EXTRACTOR_DONE);

                returnVal.setGobiiExtractorInstructions(gobiiExtractorInstructionsWithStatus);

            }else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified instruction file does not exist: " +
                                instructionFileName);

            } // if-else instruction file exists

        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }

        return returnVal;

    }

    private List<GobiiExtractorInstruction> setGobiiExtractorInstructionsStatus(List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir gobiiFileDir) {

        List<GobiiExtractorInstruction> returnVal;

        if (null != instructions) {

            if(gobiiFileDir.toString().equals("EXTRACTOR_DONE")){

                returnVal = instructionFilesDAO.setExtractorGobiiJobStatus(false, instructions, gobiiFileDir); //individually check and set status of files based on if written in the output directories
            }
            else {
                returnVal = instructionFilesDAO.setExtractorGobiiJobStatus(true, instructions, gobiiFileDir);
            }

        } else {

            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The instruction file exists, but could not be read in directory "+ gobiiFileDir.toString());
        }

        return returnVal;
    }
}
