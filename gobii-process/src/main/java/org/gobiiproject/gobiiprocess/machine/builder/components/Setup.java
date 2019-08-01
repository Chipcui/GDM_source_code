package org.gobiiproject.gobiiprocess.machine.builder.components;

import lombok.Data;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.components.*;
import org.reflections.Reflections;

import java.util.Set;

@Data
public class Setup<S> implements Transition<BuilderState<S>> {

	private final SideEffect<S> sideEffectProto = (s0) -> {};
	private final Transition<S> transitionProto = (s0) -> {};
	private final Failure<S> failureProto = (s0) -> {};
	private final Validation<S> validationProto = (s0) -> false;

	private String prefix;

	@Override
	public void run(BuilderState<S> s0) {

		Reflections r = new Reflections(prefix);

		Set<Class<?>> types = r.getTypesAnnotatedWith(Component.class);

		for (Class<?> type : types) {
			if (sideEffectProto.getClass().isAssignableFrom(type)) {
				s0.getSideEffects().put(getComponentName(type), (Class<SideEffect<S>>) type);
			} else if (transitionProto.getClass().isAssignableFrom(type)) {
				s0.getTransitions().put(getComponentName(type), (Class<Transition<S>>) type);
			} else if (failureProto.getClass().isAssignableFrom(type)) {
				s0.getFailures().put(getComponentName(type), (Class<Failure<S>>) type);
			} else if (validationProto.getClass().isAssignableFrom(type)) {
				s0.getValidations().put(getComponentName(type), (Class<Validation<S>>) type);
			}
		}
	}

	private String getComponentName(Class<?> c) {
		return c.getAnnotation(Component.class).value();
	}
}
