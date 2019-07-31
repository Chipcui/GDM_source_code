package org.gobiiproject.gobiiprocess.digester.machine;

import lombok.Getter;
import lombok.experimental.Wither;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiiprocess.JobStatus;
import org.gobiiproject.gobiiprocess.machine.State;

import java.util.LinkedList;
import java.util.List;

@Getter
@Wither
public class DigesterState extends State {

	private GobiiLoaderProcedure procedure;

	private JobStatus jobStatus;

	private List<String> debugLog = new LinkedList<>();
	private List<String> infoLog = new LinkedList<>();
	private List<String> warningLog = new LinkedList<>();
	private List<String> errorLog = new LinkedList<>();
}
