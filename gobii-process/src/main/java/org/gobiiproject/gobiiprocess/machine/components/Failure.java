package org.gobiiproject.gobiiprocess.machine.components;

@FunctionalInterface
public interface Failure<S> extends Reaction<S>, Fundamental<S> {

	default Failure<S> join(Failure<S> v) {
		return s -> this.apply(s).andThen(v.apply(s));
	}
}
