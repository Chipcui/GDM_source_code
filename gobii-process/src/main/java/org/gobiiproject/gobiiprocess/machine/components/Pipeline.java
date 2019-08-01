package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Pipeline<S> implements Transition<S> {

	private List<Prototype<S>> prototypes = new LinkedList<>();

	private List<Step<S>> steps = new LinkedList<>();

	public void run(S s0) {

		for (Step<S> step : steps) {

			step.getTransition().run(s0);

			boolean validation = step.getValidation().validate(s0);

			for (Prototype<S> proto : step.getPrototypes()) {
				validation &= proto.getValidation().validate(s0);
				if (! validation) {
					break;
				}
			}

			if (! validation) {
				step.getFailure().react(s0);
				for (Prototype<S> proto : prototypes) {
					proto.getFailure().react(s0);
				}

				return;
			}
		}
	}
}
