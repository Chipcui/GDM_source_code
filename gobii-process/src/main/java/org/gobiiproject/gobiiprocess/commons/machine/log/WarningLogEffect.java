package org.gobiiproject.gobiiprocess.commons.machine.log;

import org.gobiiproject.gobiiprocess.commons.machine.CommonState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import java.util.function.Consumer;

@Component("log/warn")
public class WarningLogEffect extends LogEffect {

	@Override
	public Consumer<CommonState> apply(CommonState s0) {
		return null;
	}
}
