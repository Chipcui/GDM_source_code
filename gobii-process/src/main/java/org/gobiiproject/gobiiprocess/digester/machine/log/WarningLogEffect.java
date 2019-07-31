package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.DigesterState;

public class WarningLogEffect extends LogEffect {

	@Override
	public DigesterState react(DigesterState s0, DigesterState s1) {

		for (int i = s0.getInfoLog().size(); i < s1.getWarningLog().size(); i++) {
			warn(s1.getInfoLog().get(i));
		}

		return s1;
	}
}