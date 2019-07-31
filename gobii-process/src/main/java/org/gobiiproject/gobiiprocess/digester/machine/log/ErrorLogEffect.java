package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.DigesterState;

public class ErrorLogEffect extends LogEffect {

	@Override
	public DigesterState react(DigesterState s0, DigesterState s1) {

		for (int i = s0.getInfoLog().size(); i < s1.getErrorLog().size(); i++) {
			error(s1.getInfoLog().get(i));
		}

		return s1;
	}
}
