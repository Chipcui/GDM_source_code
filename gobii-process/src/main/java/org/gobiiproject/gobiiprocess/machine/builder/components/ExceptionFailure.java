package org.gobiiproject.gobiiprocess.machine.builder.components;

import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.components.Failure;

public class ExceptionFailure<S> implements Failure<BuilderState<S>> {

	@Override
	public void react(BuilderState<S> s0) {

		for (Exception e : s0.getExceptions()) {

			e.printStackTrace();
		}
	}
}
