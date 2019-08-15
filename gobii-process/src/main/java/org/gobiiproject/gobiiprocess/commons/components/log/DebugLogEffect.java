package org.gobiiproject.gobiiprocess.commons.components.log;

import org.gobiiproject.gobiiprocess.commons.components.CommonState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import java.util.List;
import java.util.function.Consumer;

@Component("log/debug")
public class DebugLogEffect extends LogEffect<CommonState> {

	@Override
	public Consumer<CommonState> apply(CommonState s0) {
		final int numLogs = s0.getDebugLog().size();
		return s1 -> {
			List<String> logs = s1.getDebugLog();
			for (int i = numLogs ; i < logs.size() ; i++) {
				this.getLogger().debug(logs.get(i));
			}
		};
	}
}
