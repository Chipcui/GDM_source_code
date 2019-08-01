package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Step<S> extends Prototype<S> {

	private List<Prototype<S>> prototypes = new LinkedList<>();
	private Transition<S> transition;

}
