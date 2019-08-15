package org.gobiiproject.gobiiprocess.machine.components;

@FunctionalInterface
public interface SideEffect<S> extends Reaction<S>, Fundamental<S> {

	default SideEffect<S> join(SideEffect<S> se) {
		return s -> this.apply(s).andThen(se.apply(s));
	}
}
