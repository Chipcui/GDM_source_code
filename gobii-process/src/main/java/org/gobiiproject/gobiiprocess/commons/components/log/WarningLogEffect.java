package org.gobiiproject.gobiiprocess.commons.components.log;

import org.gobiiproject.gobiiprocess.commons.model.CommonState;
import org.gobiiproject.gobiiprocess.commons.model.log.Warning;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import java.util.List;
import java.util.function.Consumer;

@Component("log/warn")
public class WarningLogEffect extends LogEffect<CommonState> {

	@Override
	public Consumer<CommonState> apply(CommonState s0) {
		final int numLogs = s0.getWarning().size();
		return s1 -> {
			List<Warning> logs = s1.getWarning();
			for (int i = numLogs ; i < logs.size() ; i++) {
				this.getLogger().warn(logs.get(i).toString());
			}
		};
	}
}
