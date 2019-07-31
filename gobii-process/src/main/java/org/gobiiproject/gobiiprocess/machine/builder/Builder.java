package org.gobiiproject.gobiiprocess.machine.builder;

import org.gobiiproject.gobiiprocess.machine.Component;
import org.gobiiproject.gobiiprocess.machine.Failure;
import org.gobiiproject.gobiiprocess.machine.SideEffect;
import org.gobiiproject.gobiiprocess.machine.Transition;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Builder<S> {

	private final SideEffect<S> sideEffectProto = (s0, s1) -> null;
	private final Transition<S> transitionProto = (s0) -> null;
	private final Failure<S> failureProto = (s0, s1) -> null;

	private Map<String, Class<? extends SideEffect<S>>> sideEffects = new HashMap<>();

	private Map<String, Class<? extends Transition<S>>> transitions = new HashMap<>();

	private Map<String, Class<? extends Failure<S>>> failures = new HashMap<>();

	public Builder(String prefix) {
		setup(prefix);
	}

	private String getComponentName(Class<?> c) {
		return c.getAnnotation(Component.class).value();
	}

	private void setup(String prefix) {


		Reflections r = new Reflections(prefix);

		Set<Class<?>> types = r.getTypesAnnotatedWith(Component.class);

		for (Class<?> type : types) {
			if (sideEffectProto.getClass().isAssignableFrom(type)) {

				Class<? extends SideEffect<S>> 
				sideEffects.put(getComponentName(type), );
			}
		}


	}

	private void filterOnTypeParameter(Collection<Class<?>> classes, Class<?> c) {

	}
}
