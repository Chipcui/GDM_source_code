package org.gobiiproject.gobiiprocess.machine.components;

@FunctionalInterface
public interface Reaction<S> {

	void react(S s0);
}
