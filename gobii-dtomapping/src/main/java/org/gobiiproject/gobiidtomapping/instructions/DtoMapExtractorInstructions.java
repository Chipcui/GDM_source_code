package org.gobiiproject.gobiidtomapping.instructions;


import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapExtractorInstructions {

    ExtractorInstructionFilesDTO writeInstructions(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO)  throws GobiiException;
    ExtractorInstructionFilesDTO getStatus(String cropType, String jobId)  throws GobiiException;
}
