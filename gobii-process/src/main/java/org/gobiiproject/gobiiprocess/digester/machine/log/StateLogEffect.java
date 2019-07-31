package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.DigesterState;

public class StateLogEffect extends LogEffect {

	private LogLevel logLevel;

	public StateLogEffect() {
		this.logLevel = LogLevel.DEBUG;
	}

	public StateLogEffect(LogLevel level) {
		logLevel = level;
	}

	public StateLogEffect(String level) {
		logLevel = LogLevel.valueOf(level.toLowerCase());
	}

	@Override
	public DigesterState react(DigesterState s0, DigesterState s1) {

		log(logLevel, s0.toString());

		return s1;
	}

}
