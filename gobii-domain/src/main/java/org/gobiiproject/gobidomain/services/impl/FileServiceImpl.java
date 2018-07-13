package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.FilesService;
import org.gobiiproject.gobiidtomapping.instructions.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.InstructionFileAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;


/**
 * Created by Angel on 6/8/2016.
 */
public class FileServiceImpl implements FilesService {

    private Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);


    InstructionFileAccess<Void> instructionFileAccess = new InstructionFileAccess<>(Void.class);

    @Autowired
    DtoMapExtractorInstructions extractorInstructions;


    private boolean isPathLegal(String cropType, String path) throws Exception {

        boolean returnVal = false;


        List<String> legalDirectories = (new ConfigSettings()).getLegalUpdloadDIrectories(cropType);
        for (int idx = 0; (idx < legalDirectories.size()) && !returnVal; idx++) {

            String currentLegalDirectory = legalDirectories.get(idx);
            Integer directoryNameLength = currentLegalDirectory.length();
            if (path.length() >= directoryNameLength) {
                //as long as the _first_ N characters of the _absolute_ file path match, the directory is legal
                returnVal = path.substring(0, directoryNameLength).toLowerCase().contains(currentLegalDirectory.toLowerCase());
            }
        }

        return returnVal;

    }


    private String getFilePath(String cropType, String jobId, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        String returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();

        if (gobiiFileProcessDir.equals(GobiiFileProcessDir.EXTRACTOR_OUTPUT)) {

            ExtractorInstructionFilesDTO extractorInstructionFilesDTO = extractorInstructions.getStatus(cropType, jobId);
            if (extractorInstructionFilesDTO
                    .getGobiiExtractorInstructions().size() > 0
                    && extractorInstructionFilesDTO
                    .getGobiiExtractorInstructions()
                    .get(0)
                    .getDataSetExtracts().size() > 0) {

                GobiiDataSetExtract gobiiDataSetExtract = extractorInstructionFilesDTO
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0);

                returnVal = gobiiDataSetExtract.getExtractDestinationDirectory();


            } else {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.NONE, "There is no instruction for the job ");
            }

        } else if (gobiiFileProcessDir.equals(GobiiFileProcessDir.RAW_USER_FILES)) {

            returnVal = configSettings.getFullyQualifiedFilePath(cropType,
                    gobiiFileProcessDir);
            returnVal += jobId;
        } else if (gobiiFileProcessDir.equals(GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE)) {

            returnVal = configSettings.getFullyQualifiedFilePath(null, gobiiFileProcessDir);

        } else {
            returnVal = configSettings.getFullyQualifiedFilePath(cropType,
                    gobiiFileProcessDir);
        }

        return returnVal;
    }


    @Override
    public void writeFileToProcessDir(String cropType,
                                      String fileName,
                                      GobiiFileProcessDir gobiiFileProcessDir,
                                      byte[] byteArray) throws GobiiDomainException {

        try {

            // our goal here would be to prevent anyone but a test system-defined user from writing
            // arbitrary files to the server. We would  need to do this more systematically when we
            // implement a real authoriization mechanism; however, the authentication object comes
            // out null here. For now it's ok because at least a user has to be authenticated to
            // get access to this service.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//        if( currentPrincipalName != "USER_READER" ) {
            ConfigSettings configSettings = new ConfigSettings();

            // more root processing paths may be added later
            String path = this.getFilePath(cropType, fileName, gobiiFileProcessDir);

            if (!this.isPathLegal(cropType, path)) {
                throw new GobiiDomainException("Illegal path"); // exception message is deliberately value
            }


            instructionFileAccess.writeFile(path, fileName, byteArray);

        } catch (Exception e) {
            throw new GobiiDomainException("Error writing file name " + fileName + ": " + e.getMessage());
        }

    }

    @Override
    public void deleteFileFromProcessDir(String cropType,
                                         String fileName,
                                         GobiiFileProcessDir gobiiFileProcessDir) throws GobiiDomainException {

        try {
            ConfigSettings configSettings = new ConfigSettings();
            String path = this.getFilePath(cropType, fileName, gobiiFileProcessDir);

            if (!this.isPathLegal(cropType, path)) {
                throw new GobiiDomainException("Illegal path"); // exception message is deliberately value
            }


            String fqpn = instructionFileAccess.makeFileName(path, fileName);
            instructionFileAccess.deleteFile(fqpn);

        } catch (Exception e) {
            throw new GobiiDomainException("Error deleting file " + fileName + ": " + e.getMessage());
        }

    }


    @Override
    public void writeJobFileForCrop(String cropType,
                                    String jobId,
                                    String fileName,
                                    GobiiFileProcessDir gobiiFileProcessDir,
                                    byte[] byteArray) throws GobiiException, GobiiDomainException {

        try {

            String path = this.getFilePath(cropType, jobId, gobiiFileProcessDir);

            if (!this.isPathLegal(cropType, path)) {
                throw new GobiiDomainException("Illegal path"); // exception message is deliberately value
            }


            instructionFileAccess.writeFile(path, fileName, byteArray);

        } catch (Exception e) {
            throw new GobiiDomainException("Error writing file name " + fileName + ": " + e.getMessage());
        }

    }

    @Override
    public File readCropFileForJob(String cropType,
                                   String gobiiJobId,
                                   String fileName,
                                   GobiiFileProcessDir gobiiFileProcessDir) throws GobiiException, GobiiDomainException {

        File returnVal;

        try {

            String path = this.getFilePath(cropType, gobiiJobId, gobiiFileProcessDir);

            if (!this.isPathLegal(cropType, path)) {
                throw new GobiiDomainException("Illegal path"); // exception message is deliberately value
            }


            String fqpn = instructionFileAccess.makeFileName(path, fileName);

            returnVal = instructionFileAccess.readFile(fqpn);

            return returnVal;

        } catch (Exception e) {
            throw new GobiiDomainException("Error reading file name " + fileName  + ": " + e.getMessage());
        }

    } // end func
} // end class