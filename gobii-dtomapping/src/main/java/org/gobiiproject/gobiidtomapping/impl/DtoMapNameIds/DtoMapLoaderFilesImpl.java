package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFilesDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderFiles;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 11/2016.
 */
public class DtoMapLoaderFilesImpl implements DtoMapLoaderFiles {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderFilesImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private LoaderFilesDAO loaderFilesDAO;

    public LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDaoException {
        LoaderFilePreviewDTO returnVal = null;
        String fileCropDirectory = null;
        ConfigSettings configSettings = new ConfigSettings();
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            throw new GobiiDaoException("could not get processing path because of: " + e.getMessage());
        }


        if (null != fileCropDirectory) {
            if (!loaderFilesDAO.doesPathExist(fileCropDirectory)) {
                loaderFilesDAO.makeDirectory(fileCropDirectory);
            } else {
                loaderFilesDAO.verifyDirectoryPermissions(fileCropDirectory);
            }
        }


        if (null != directoryName) {
            String directoryPath = fileCropDirectory+ directoryName;
            if (!loaderFilesDAO.doesPathExist(directoryPath)) {
                returnVal = loaderFilesDAO.makeDirectory(directoryPath);
            } else {
                loaderFilesDAO.verifyDirectoryPermissions(directoryPath);
            }
        }


        return returnVal;

    } // createDirectories()

    public LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDaoException {
        LoaderFilePreviewDTO returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();
        String fileCropDirectory = null;
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            throw new GobiiDaoException("could not get processing path because of: " + e.getMessage());
        }

        String directoryPath = fileCropDirectory+ directoryName;
        if (!loaderFilesDAO.doesPathExist(directoryPath)) {
                throw new GobiiDaoException("The specified directory does not exist: " + directoryPath);
            }else{
                returnVal = loaderFilesDAO.getPreview(directoryPath, fileFormat);
            }
        return returnVal;

    } // createDirectories()





}
