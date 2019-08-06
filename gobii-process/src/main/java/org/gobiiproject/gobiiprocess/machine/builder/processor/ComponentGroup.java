package org.gobiiproject.gobiiprocess.machine.builder.processor;

import lombok.Data;
import org.gobiiproject.gobiiprocess.machine.components.Failure;
import org.gobiiproject.gobiiprocess.machine.components.SideEffect;
import org.gobiiproject.gobiiprocess.machine.components.Transition;
import org.gobiiproject.gobiiprocess.machine.components.Validation;

import java.util.LinkedList;
import java.util.List;

@Data
public class ComponentGroup<T> {

	private final Class<T> target;

	private List<Transition<T>> transitions = new LinkedList<>();
	private List<Validation<T>> validations = new LinkedList<>();
	private List<SideEffect<T>> sideEffects = new LinkedList<>();
	private List<Failure<T>> failures = new LinkedList<>();

	private ComponentGroup(Class<T> target) {
		this.target = target;
	}

	public static <T> ComponentGroup<T> of(Class<T> c) {
		return new ComponentGroup<T>(c);
	}
}
