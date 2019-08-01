package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Prototype<S> {

	private Validation<S> validation = s -> true;
	private Failure<S> failure = s -> {};
	private List<SideEffect<S>> sideEffects = new LinkedList<>();

}
