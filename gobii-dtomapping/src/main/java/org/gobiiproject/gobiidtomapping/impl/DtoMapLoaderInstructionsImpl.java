package org.gobiiproject.gobiidtomapping.impl;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiidtomapping.*;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderInstructionsImpl implements DtoMapLoaderInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderInstructionsImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    DtoMapExperiment dtoMapExperiment;

    @Autowired
    DtoMapProject dtoMapProject;

    @Autowired
    DtoMapPlatform dtoMapPlatform;

    @Autowired
    DtoMapDataSet dtoMapDataSet;

    @Autowired
    DtoMapProtocol dtoMapProtocol;

    @Autowired
    private DtoMapJob dtoMapJob = null;

    private InstructionFileAccess<List<GobiiLoaderInstruction>> instructionFileAccess = new InstructionFileAccess<>(GobiiLoaderInstruction.class);


    private void createDirectories(String instructionFileDirectory,
                                   GobiiFile gobiiFile) throws GobiiDaoException {


        if (null != instructionFileDirectory) {
            instructionFileAccess.createDirectory(instructionFileDirectory);
        }

        if (gobiiFile.isCreateSource()) {
            instructionFileAccess.createDirectory(gobiiFile.getSource());
        }

        instructionFileAccess.createDirectory(gobiiFile.getDestination());

    } // createDirectories()


    @Override
    public LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiException {

        LoaderInstructionFilesDTO returnVal = loaderInstructionFilesDTO;

        if (LineUtils.isNullOrEmpty(returnVal.getInstructionFileName())) {
            throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                    "The instruction file DTO is missing the instruction file name"
            );
        }


        try {


            ConfigSettings configSettings = new ConfigSettings();


            if (null == cropType) {
                throw new GobiiDtoMappingException("Loader instruction request does not specify a crop");
            }

            String instructionFileDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.LOADER_INSTRUCTIONS);

            String instructionFileFqpn = instructionFileDirectory
                    + loaderInstructionFilesDTO.getInstructionFileName()
                    + INSTRUCTION_FILE_EXT;



            GobiiLoaderInstruction primaryLoaderInstruction = loaderInstructionFilesDTO.getGobiiLoaderInstructions().get(0);


            for (GobiiLoaderInstruction currentInstruction : loaderInstructionFilesDTO
                    .getGobiiLoaderInstructions()) {

                if (StringUtils.isNotEmpty(currentInstruction.getTable())) {
                    primaryLoaderInstruction.getColumnsByTableName().put(currentInstruction.getTable(),currentInstruction.getGobiiFileColumns());
                }
            }



            primaryLoaderInstruction.setColumnsByTableName(
                    loaderInstructionFilesDTO
                            .getGobiiLoaderInstructions()
                            .stream()
                            .filter(gfi -> StringUtils.isNotEmpty(gfi.getTable()))
                            .collect(Collectors.toMap(GobiiLoaderInstruction::getTable, GobiiLoaderInstruction::getGobiiFileColumns))
            );


            if (primaryLoaderInstruction.getJobPayloadType() == null) {
                throw new Exception("The primary instruction does not have a payload type");
            }


//            for (Integer currentFileIdx = 0;
//                 currentFileIdx < loaderInstructionFilesDTO.getGobiiLoaderInstructions().size();
//                 currentFileIdx++) {
//
//                primaryLoaderInstruction =
//                        loaderInstructionFilesDTO.getGobiiLoaderInstructions().get(currentFileIdx);


            if (LineUtils.isNullOrEmpty(primaryLoaderInstruction.getGobiiCropType())) {
                primaryLoaderInstruction.setGobiiCropType(cropType);
            }

            GobiiFile currentGobiiFile = primaryLoaderInstruction.getGobiiFile();

            // check that we have all required values
            if (LineUtils.isNullOrEmpty(currentGobiiFile.getSource())) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "The file associated with instruction is missing the source file path"
                );
            }

            if (LineUtils.isNullOrEmpty(currentGobiiFile.getDestination())) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "The file associated with instruction  is missing the destination file path"
                );
            }

            if (currentGobiiFile.isRequireDirectoriesToExist()) {

                if (!instructionFileAccess.doesPathExist(currentGobiiFile.getSource())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "require-to-exist was set to true, but the source file path does not exist: "
                                    + currentGobiiFile.getSource());

                }

                if (!instructionFileAccess.doesPathExist(currentGobiiFile.getDestination())) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "require-to-exist was set to true, but the source file path does not exist: "
                                    + currentGobiiFile.getSource());
                }

            }


            // if so, proceed with processing

            //validate loader instruction

            // check if the dataset is referenced by the specified experiment
            if (primaryLoaderInstruction.getDataSet().getId() != null) {

                DataSetDTO dataSetDTO = dtoMapDataSet.getDataSetDetails(primaryLoaderInstruction.getDataSet().getId());

                // check if the experiment is referenced by the specified project
                if (primaryLoaderInstruction.getExperiment().getId() != null) {

                    if (!dataSetDTO.getExperimentId().equals(primaryLoaderInstruction.getExperiment().getId())) {

                        throw new GobiiDtoMappingException("The specified experiment in the dataset is incorrect");
                    }

                    ExperimentDTO experimentDTO = dtoMapExperiment.getExperimentDetails(primaryLoaderInstruction.getExperiment().getId());

                    if (!experimentDTO.getProjectId().equals(primaryLoaderInstruction.getProject().getId())) {

                        throw new GobiiDtoMappingException("The specified project in the experiment is incorrect");

                    }

                }

                // check if the datatype is referenced by the dataset
                if (primaryLoaderInstruction.getDatasetType().getId() != null) {

                    if (!dataSetDTO.getTypeId().equals(primaryLoaderInstruction.getDatasetType().getId())) {

                        throw new GobiiDtoMappingException("The specified data type in the dataset is incorrect");

                    }

                }

            }


            if (primaryLoaderInstruction.getPlatform().getId() != null && primaryLoaderInstruction.getExperiment().getId() != null) {

                ExperimentDTO experimentDTO = dtoMapExperiment.getExperimentDetails(primaryLoaderInstruction.getExperiment().getId());

                if (experimentDTO.getVendorProtocolId() != null) {

                    VendorProtocolDTO vendorProtocolDTO = dtoMapProtocol.getVendorProtocolByVendorProtocolId(experimentDTO.getVendorProtocolId());

                    if (vendorProtocolDTO.getProtocolId() != null) {

                        ProtocolDTO protocolDTO = dtoMapProtocol.getProtocolDetails(vendorProtocolDTO.getProtocolId());

                        if (protocolDTO.getPlatformId() != null) {

                            Integer loaderPlatformId = primaryLoaderInstruction.getPlatform().getId();

                            if (!loaderPlatformId.equals(protocolDTO.getPlatformId())) {

                                throw new GobiiDtoMappingException("The specified platform in the experiment is incorrect");

                            }

                        }

                    }

                }

            }


            // "source file" is the data file the user may have already uploaded
            if (currentGobiiFile.isCreateSource()) {

                createDirectories(instructionFileDirectory,
                        currentGobiiFile);

            } else {

                // it's supposed to exist, so we check
                if (instructionFileAccess.doesPathExist(currentGobiiFile.getSource())) {

                    createDirectories(instructionFileDirectory,
                            currentGobiiFile);
                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The load file was specified to exist, but does not exist: "
                                    + currentGobiiFile.getSource());

                } // if-else the source file exists

            } // if-else we're creating a source file


//            } // iterate instructions/files


            // NOW CREATE THE JOB RECORD *********************************************************
            Integer dataSetId = null;
            if (JobPayloadType.CV_PAYLOADTYPE_MATRIX.equals(primaryLoaderInstruction.getJobPayloadType())) {
                if (primaryLoaderInstruction.getDataSetId() != null) {
                    dataSetId = primaryLoaderInstruction.getDataSetId();
                } else {
                    if (primaryLoaderInstruction.getDataSet() != null && primaryLoaderInstruction.getDataSet().getId() != null) {
                        dataSetId = primaryLoaderInstruction.getDataSetId();
                    }
                }

                if (dataSetId == null || dataSetId <= 0) {
                    throw new GobiiException("The specified job has payload type MATRIX, but no dataset ID: " + loaderInstructionFilesDTO.getInstructionFileName());
                }

            }


            Integer contactId = primaryLoaderInstruction.getContactId();

            if (contactId != null || contactId > 0) {


                //check for duplicate job name and provide meaningful error message
                JobDTO jobDTOExisting = dtoMapJob.getJobDetailsByJobName(loaderInstructionFilesDTO.getInstructionFileName());
                if (jobDTOExisting.getJobId() == null || jobDTOExisting.getJobId() <= 0) {

                    JobDTO jobDTONew = new JobDTO();

                    jobDTONew.setJobName(loaderInstructionFilesDTO.getInstructionFileName());
                    jobDTONew.setDatasetId(dataSetId);
                    jobDTONew.setSubmittedBy(contactId);
                    jobDTONew.setMessage("Instruction file written by web services");
                    jobDTONew.setStatus(JobProgressStatusType.CV_PROGRESSSTATUS_PENDING.getCvName());
                    jobDTONew.setPayloadType(primaryLoaderInstruction.getJobPayloadType().getCvName());
                    jobDTONew.setType(JobType.CV_JOBTYPE_LOAD.getCvName());
                    jobDTONew.setSubmittedDate(new Date());

                    dtoMapJob.createJob(jobDTONew);

                    instructionFileAccess.writeInstructions(instructionFileFqpn,
                            returnVal.getGobiiLoaderInstructions());

                } else {

                    throw new GobiiException("The specified loader job already exists: " + loaderInstructionFilesDTO.getInstructionFileName());

                }// if-else a job with that name already exists

            } else {

                throw new GobiiException("The specified job does not have a contact ID: " + loaderInstructionFilesDTO.getInstructionFileName());

            } //if-else we have a contact id


        } catch (GobiiException e) {
            throw e;
        } catch (Exception e) {
            throw new GobiiException(e);
        }

        return returnVal;

    } // writeInstructions

    @Override
    public LoaderInstructionFilesDTO getInstruction(String cropType, String instructionFileName) throws GobiiDtoMappingException {

        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        try {
            ConfigSettings configSettings = new ConfigSettings();
            String instructionFile = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.LOADER_INSTRUCTIONS)
                    + instructionFileName
                    + INSTRUCTION_FILE_EXT;


            if (instructionFileAccess.doesPathExist(instructionFile)) {

                InstructionFileAccess<GobiiLoaderInstruction> instructionFileAccessGobiiLoaderInstruction = new InstructionFileAccess<>(GobiiLoaderInstruction.class);
                List<GobiiLoaderInstruction> instructions =
                        instructionFileAccessGobiiLoaderInstruction.getInstructions(instructionFile,
                                GobiiLoaderInstruction[].class);

                if (null != instructions) {
                    returnVal.setInstructionFileName(instructionFileName);
                    returnVal.setGobiiLoaderInstructions(instructions);

                } else {

                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The instruction file exists, but could not be read: " +
                                    instructionFile);

                }

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified instruction file does not exist: " +
                                instructionFile);

            } // if-else instruction file exists

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            System.out.println(e);
        }

        return returnVal;
    }
}
