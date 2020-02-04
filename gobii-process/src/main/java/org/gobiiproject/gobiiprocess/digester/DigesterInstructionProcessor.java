package org.gobiiproject.gobiiprocess.digester;

import java.io.IOException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiiprocess.digester.Digester;
import org.gobiiproject.gobiiprocess.digester.ProcessorResult;

/**
 * Created by jdl232 on 3/28/2017.
 */
@FunctionalInterface
public interface DigesterInstructionProcessor {

    ProcessorResult process(GobiiLoaderProcedure procedure, GobiiLoaderInstruction instruction) throws IOException, InterruptedException;
}
