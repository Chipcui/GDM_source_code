package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.InstructionFilesDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderFiles;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by Angel on 11/2016.
 */
public class DtoMapLoaderFilesImpl implements DtoMapLoaderFiles {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderFilesImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private InstructionFilesDAO instructionFilesDAO;

    public LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDaoException {

        LoaderFilePreviewDTO returnVal = new LoaderFilePreviewDTO();

        if (LineUtils.isNullOrEmpty(cropType)) {
            throw new GobiiDaoException("Crop type is required");
        }
        if (LineUtils.isNullOrEmpty(directoryName)) {
            throw new GobiiDaoException("A directory name is required");
        }

        String fileCropDirectory = null;
        ConfigSettings configSettings = new ConfigSettings();
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            throw new GobiiDaoException("could not get processing path because of: " + e.getMessage());
        }

        if (LineUtils.isNullOrEmpty(fileCropDirectory)) {
            throw new GobiiDaoException("There is no directory defined for this configuraiton item "
                    + GobiiFileProcessDir.RAW_USER_FILES.toString());
        }

        if (!instructionFilesDAO.doesPathExist(fileCropDirectory)) {
            instructionFilesDAO.makeDirectory(fileCropDirectory);
        } else {
            instructionFilesDAO.verifyDirectoryPermissions(fileCropDirectory);
        }


        String directoryPath = fileCropDirectory + directoryName;
        if (!instructionFilesDAO.doesPathExist(directoryPath)) {
            instructionFilesDAO.makeDirectory(directoryPath);

        } else {
            instructionFilesDAO.verifyDirectoryPermissions(directoryPath);
        }

        returnVal.setDirectoryName(directoryPath);
        returnVal.setId(1);//this is arbitrary for now

        return returnVal;

    } // createDirectories()

    public LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDaoException {

        LoaderFilePreviewDTO returnVal = new LoaderFilePreviewDTO();

        if (LineUtils.isNullOrEmpty(cropType)) {
            throw new GobiiDaoException("Crop type is required");
        }

        if (LineUtils.isNullOrEmpty(directoryName)) {
            throw new GobiiDaoException("A directory name is required");
        }

        if (LineUtils.isNullOrEmpty(fileFormat)) {
            throw new GobiiDaoException("A file format is required");
        }


        ConfigSettings configSettings = new ConfigSettings();
        String fileCropDirectory = null;
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            throw new GobiiDaoException("could not get processing path because of: " + e.getMessage());
        }

        if (LineUtils.isNullOrEmpty(fileCropDirectory)) {
            throw new GobiiDaoException("There is no directory defined for this configuraiton item "
                    + GobiiFileProcessDir.RAW_USER_FILES.toString());
        }

        String directoryPath = fileCropDirectory + directoryName;

        if (!instructionFilesDAO.doesPathExist(directoryPath)) {
            throw new GobiiDaoException("The specified directory does not exist: " + directoryPath);
        } else {

//            returnVal = instructionFilesDAO.getPreview(directoryPath, fileFormat);

            String extension ="."+fileFormat;
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();


            if(files.length==0){
                throw new GobiiDaoException("There are no files in this directory:" + directory.getName());
            }else {
                for (File file : files) {
                    if (file.getName().endsWith(extension)) {
                        if (returnVal.getFileList().isEmpty()) {//if first file in directory, get preview
                            returnVal.setFilePreview(instructionFilesDAO.getFilePreview(file, fileFormat));
                        }
                        returnVal.getFileList().add(file.getName());
                    }
                }
                if (returnVal.getFileList().isEmpty()) {//if no files are found that matches format
                    throw new GobiiDaoException("There are no files of the specified format in the directory:" + directory.getName());
                }
            }
            returnVal.setDirectoryName(directory.getAbsolutePath());
            returnVal.setId(1);//this is arbitrary for now
        }
        return returnVal;

    } // createDirectories()


}
