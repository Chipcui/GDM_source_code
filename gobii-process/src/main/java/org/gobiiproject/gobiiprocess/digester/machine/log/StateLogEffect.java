package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.DigesterState;

public class StateLogEffect extends LogEffect {

	private LogLevel logLevel = LogLevel.DEBUG;

	@Override
	public void react(DigesterState s0, DigesterState s1) {

		log(logLevel, s0.toString());
	}

}
