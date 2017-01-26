package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.access.InstructionFileAccess;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.io.File;
import java.util.List;


/**
 * Created by Phil on 4/12/2016.
 */
public interface InstructionFilesDAO {

    boolean writeInstructions(String instructionFileFqpn,
                              List<GobiiExtractorInstruction> instructions) throws GobiiDaoException; // all three

    List<T> getGobiiInstructionsFromFile(String instructionFileFqpn, InstructionFileAccess<T> instructionFileAccess) throws GobiiDaoException;

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    void makeDirectory(String pathName) throws GobiiDaoException;

    List<GobiiExtractorInstruction> setExtractorGobiiJobStatus(boolean applyToAll, List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir extractorInstructions) throws GobiiDaoException;

    List<List<String>> getFilePreview(File file, String fileFormat);

}
