package org.gobiiproject.gobiiprocess.machine.builder.components;

import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.components.Validation;

public class ExceptionValidator<S> implements Validation<BuilderState<S>> {

	@Override
	public boolean validate(BuilderState<S> s0) {

		return s0.getExceptions() != null
				&& ! s0.getExceptions().isEmpty();
	}
}
