package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.io.File;


/**
 * Created by Phil on 4/12/2016.
 */
public interface FilesService {

    void writeJobFileForCrop(String cropType,
                             String jobId,
                             String fileName,
                             GobiiFileProcessDir gobiiFileProcessDir,
                             byte[] byteArray) throws GobiiDomainException;

    void deleteFileFromProcessDir(String cropType,
                                  String fileName,
                                  GobiiFileProcessDir gobiiFileProcessDir) throws GobiiDomainException;

    void writeFileToProcessDir(String cropType,
                               String fileName,
                               GobiiFileProcessDir gobiiFileProcessDir,
                               byte[] byteArray) throws GobiiDomainException;

    File readCropFileForJob(String cropType,
                            String gobiiJobId,
                            String fileName,
                            GobiiFileProcessDir gobiiFileProcessDir) throws GobiiDomainException;


}
