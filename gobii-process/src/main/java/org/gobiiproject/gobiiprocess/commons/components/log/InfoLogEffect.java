package org.gobiiproject.gobiiprocess.commons.components.log;

import org.gobiiproject.gobiiprocess.commons.components.CommonState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import java.util.List;
import java.util.function.Consumer;

@Component("log/info")
public class InfoLogEffect extends LogEffect<CommonState> {

	@Override
	public Consumer<CommonState> apply(CommonState s0) {
		final int numLogs = s0.getInfoLog().size();
		return s1 -> {
			List<String> logs = s1.getInfoLog();
			for (int i = numLogs ; i < logs.size() ; i++) {
				this.getLogger().info(logs.get(i));
			}
		};
	}
}
