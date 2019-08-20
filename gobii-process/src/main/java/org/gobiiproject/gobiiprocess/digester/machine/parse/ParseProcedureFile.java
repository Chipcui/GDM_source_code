package org.gobiiproject.gobiiprocess.digester.machine.parse;

import org.gobiiproject.gobiimodel.dto.Marshal;
import org.gobiiproject.gobiiprocess.commons.model.log.Error;
import org.gobiiproject.gobiiprocess.digester.machine.model.DigesterState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.components.Transition;

import java.io.IOException;

@Component("parseProcedureFile")
public class ParseProcedureFile implements Transition<DigesterState> {

	@Override
	public void accept(DigesterState state) {
		try {
			state.setProcedure(Marshal.unmarshalGobiiLoaderProcedure(state.getFileLoadState().getProcedureFileContent()));
		} catch (IOException e) {
			Error error = new Error(Error.Code.PARSE, "Parse Failure", e);
			state.getError().add(error);
		}
	}
}
