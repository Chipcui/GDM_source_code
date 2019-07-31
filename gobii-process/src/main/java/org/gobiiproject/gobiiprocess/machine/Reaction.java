package org.gobiiproject.gobiiprocess.machine;

@FunctionalInterface
public interface Reaction<S> {

	S react(S s0, S s1);
}
