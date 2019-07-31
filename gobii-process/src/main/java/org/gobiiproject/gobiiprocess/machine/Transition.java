package org.gobiiproject.gobiiprocess.machine;

import lombok.Data;

import java.util.List;

@Data
public abstract class Transition<S> implements Component<S> {

	private Validation<S> validation;

	private Failure<S> failure;

	private List<SideEffect<S>> sideEffects;

}
