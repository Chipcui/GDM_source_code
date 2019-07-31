package org.gobiiproject.gobiiprocess.machine;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class StateMachine<S extends State> implements Transition<S> {

	private List<Transition<S>> transitions = new LinkedList<>();

	public S run(S s0) {
		return null;
	}
}
