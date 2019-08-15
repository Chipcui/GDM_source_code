package org.gobiiproject.gobiiprocess.machine.components;


import java.util.function.Consumer;

public interface Transition<S> extends Consumer<S>, Fundamental<S>{

	default Transition<S> join(Transition<S> t) {
		return s -> {
			this.accept(s);
			t.accept(s);
		};
	}
}
