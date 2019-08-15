package org.gobiiproject.gobiiprocess.digester.machine;

import lombok.Data;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiiprocess.JobStatus;
import org.gobiiproject.gobiiprocess.commons.components.CommonState;

@Data
public class DigesterState extends CommonState {

	private GobiiLoaderProcedure procedure;

	private JobStatus jobStatus;

}
