package org.gobiiproject.gobiiprocess.machine.components;

@FunctionalInterface
public interface Validation<S> {

	boolean validate(S s0);
}
