package org.gobiiproject.gobiiprocess.machine.builder.components.failures;

import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.components.Failure;

import java.util.function.Consumer;

public class ExceptionFailure<S> implements Failure<BuilderState<S>> {

	@Override
	public Consumer<BuilderState<S>> apply(BuilderState<S> s0) {

		return s1 -> {
			for (Exception e : s1.getExceptions()) {
				e.printStackTrace();
			}
		};
	}
}

