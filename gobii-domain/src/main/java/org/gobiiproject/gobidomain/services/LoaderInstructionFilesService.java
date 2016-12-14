package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.LoaderInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */
public interface LoaderInstructionFilesService {
    LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO);
    LoaderInstructionFilesDTO getInstruction(String cropType, String instructionFileName) throws GobiiDomainException;
}
