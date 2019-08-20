package org.gobiiproject.gobiiprocess.digester.machine.file;


import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiiprocess.digester.machine.model.DigesterState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.components.Transition;
import org.gobiiproject.gobiiprocess.commons.model.log.Error;

import java.io.IOException;

@Component("digester/loadProcedureFile")
public class LoadProcedureFile implements Transition<DigesterProcedureFileLoadState> {

	@Override
	public void accept(DigesterState state) {
		try {

			state.setProcedureFileContents(HelperFunctions.readFile(state.getProcedureFilePath()));
		} catch (IOException e) {
			Error error = new Error(Error.Code.PARSE, "Parsing Error during Digester Procedure Parsing", e);
			state.getError().add(error);
		}
	}

}
