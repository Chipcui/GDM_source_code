package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.InstructionFilesDAO;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapQCInstructions;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiQCComplete;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapQCInstructionsImpl implements DtoMapQCInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapQCInstructionsImpl.class);

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
    public QCInstructionsDTO createInstructions(String cropType, QCInstructionsDTO qcInstructionsDTO) throws GobiiException {

        QCInstructionsDTO returnVal = qcInstructionsDTO;

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileDirectory = configSettings.getProcessingPath(cropType,
                    GobiiFileProcessDir.QC_NOTIFICATIONS);

            createDirectories(instructionFileDirectory);

            String instructionFileFqpn = instructionFileDirectory
                    + qcInstructionsDTO.getGobiiQCComplete().getDataFileName()
                    + INSTRUCTION_FILE_EXT;


            if (!instructionFilesDAO.doesPathExist(instructionFileFqpn)) {
                instructionFilesDAO.writeInstructions(instructionFileFqpn,
                        qcInstructionsDTO.getGobiiQCComplete());

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
    public QCInstructionsDTO getInstruction(String cropType, String instructionFileName) {

        QCInstructionsDTO returnVal = new QCInstructionsDTO();

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String instructionFileDirectory = configSettings.getProcessingPath(cropType,
                    GobiiFileProcessDir.QC_NOTIFICATIONS);

            String instructionFileFqpn = instructionFileDirectory
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;

            if (instructionFilesDAO.doesPathExist(instructionFileFqpn)) {


                GobiiQCComplete gobiiQCComplete =
                        instructionFilesDAO
                                .getInstructions(instructionFileFqpn);

                if (null != gobiiQCComplete) {

                    returnVal.setGobiiQCComplete(gobiiQCComplete);

//                    returnVal.getGobiiQCComplete().setDataFileName(instructionFileName);

                } else {

                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,

                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,

                            "The instruction file exists, but could not be read: " +

                                    instructionFileName);

                }

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,

                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,

                        "The specified instruction file does not exist: " +

                                instructionFileName);

            } // if-else instruction file exists

        } catch (Exception e) {

            LOGGER.error("Gobii Maping Error", e);

            System.out.println(e);
        }

        return returnVal;
    }
}
