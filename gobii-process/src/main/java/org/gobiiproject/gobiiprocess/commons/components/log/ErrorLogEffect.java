package org.gobiiproject.gobiiprocess.commons.components.log;

import org.gobiiproject.gobiiprocess.commons.model.CommonState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import org.gobiiproject.gobiiprocess.commons.model.log.Error;

import java.util.List;
import java.util.function.Consumer;

@Component("log/error")
public class ErrorLogEffect extends LogEffect<CommonState> {

	@Override
	public Consumer<CommonState> apply(CommonState s0) {
		final int numLogs = s0.getErrorLog().size();
		return s1 -> {
			List<Error> logs = s1.getErrorLog();
			for (int i = numLogs ; i < logs.size() ; i++) {
				this.getLogger().error(logs.get(i).toString());
			}
		};
	}

}
