package org.gobiiproject.gobiiprocess.machine;

import org.gobiiproject.gobiiprocess.machine.components.Reaction;

public class Util {

	public <S, T extends Reaction<S>> Reaction<S> union(T r0, T r1) {
		if (r0 == null && r1 == null) {
			return null;
		} else if (r0 == null) {
			return r1;
		} else if (r1 == null) {
			return r0;
		}

		return (S s0, S s1) -> { r0.react(s0, s1); r1.react(s0, s1); };
	}

}
