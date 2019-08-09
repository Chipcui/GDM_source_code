package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Prototype<S> implements Pipe<S>, Transition<S> {

	private Transition<S> transition = s -> {};
	private Validation<S> validation = s0 -> s1 -> true;
	private Failure<S> failure = s0 -> (s1 -> {});
	private List<SideEffect<S>> sideEffects = new LinkedList<>();

	@Override
	public void accept(S s) {
		transition.accept(s);
	}
}
