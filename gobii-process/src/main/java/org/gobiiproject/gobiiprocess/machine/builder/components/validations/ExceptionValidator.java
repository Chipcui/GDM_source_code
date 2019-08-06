package org.gobiiproject.gobiiprocess.machine.builder.components.validations;

import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.components.Validation;

import java.util.function.Predicate;

public class ExceptionValidator<S> implements Validation<BuilderState<S>> {

	@Override
	public Predicate<BuilderState<S>> apply(BuilderState<S> s0) {

		return s1 -> s1.getExceptions() == null || s1.getExceptions().isEmpty();
	}
}
