package org.gobiiproject.gobiiprocess.digester.machine.model;

import lombok.Data;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiiprocess.JobStatus;
import org.gobiiproject.gobiiprocess.commons.model.CommonState;

@Data
public class DigesterState extends CommonState {

	private GobiiLoaderProcedure procedure;

	private JobStatus jobStatus;

}
