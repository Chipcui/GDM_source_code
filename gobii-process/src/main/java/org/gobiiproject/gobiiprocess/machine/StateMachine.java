package org.gobiiproject.gobiiprocess.machine;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class StateMachine<S extends State> implements Component<S> {

	private List<Transition<S>> transitions = new LinkedList<>();

	public S run(S s0) {

		S s = s0;
		for (Transition<S> t : transitions) {
			S s1 = s;
			S s2 = t.getValidation().react(s1, t.run(s));
			if (! s2.isFailure()) {
				t.getSideEffects().forEach(se -> se.react(s1, s2));
			} else {
				t.getFailure().react(s1, s2);
				return s2;
			}
			s = s2;
		}

		return s;
	}
}
