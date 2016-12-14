package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.LoaderFilePreviewDTO;


/**
 * Created by Angel on 11/2016.
 */
public interface LoaderFilesDAO {

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    LoaderFilePreviewDTO makeDirectory(String directoryPath) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    LoaderFilePreviewDTO getPreview(String directoryPath, String fileExtension) throws GobiiDaoException;

}
