package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.model.DigesterState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import java.util.function.Consumer;

@Component("log/jobState")
public class StateLogEffect extends DigesterLogEffect {

	@Override
	public Consumer<DigesterState> apply(DigesterState s0) {
		return null;
	}
}
