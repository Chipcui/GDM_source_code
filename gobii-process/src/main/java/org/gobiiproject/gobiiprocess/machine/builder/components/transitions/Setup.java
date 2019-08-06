package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.components.*;
import org.reflections.Reflections;

import java.util.Set;

@Data
public class Setup<S> implements Transition<BuilderState<S>> {

	private final Transition<S> transitionPrototype = s -> {};

	@Override
	public void accept(BuilderState<S> s0) {

		JsonNode packages = s0.getSchema().get(Schema.PACKAGES);

		for (JsonNode pack : packages) {
			loadTransitions(s0, pack.asText());
			loadSideEffects(s0, pack.asText());
			loadValidations(s0, pack.asText());
			loadFailures(s0, pack.asText());
		}

	}

	private void loadTransitions(BuilderState<S> s0, String prefix) {

		Set<Class<? extends Transition>> subtypes = new Reflections().getSubTypesOf(Transition.class);

		for (Class<? extends Transition> subtype : subtypes) {
			if (subtype.isAnnotationPresent(Component.class)) {
				String name = subtype.getAnnotation(Component.class).value();
				s0.getTransitions().put(name, (Class<Transition<S>>) subtype);
			}
		}
	}

	private void loadSideEffects(BuilderState<S> s0, String prefix) {

		Set<Class<? extends SideEffect>> subtypes = new Reflections().getSubTypesOf(SideEffect.class);

		for (Class<? extends SideEffect> subtype : subtypes) {
			if (subtype.isAnnotationPresent(Component.class)) {
				String name = subtype.getAnnotation(Component.class).value();
				s0.getSideEffects().put(name, (Class<SideEffect<S>>) subtype);
			}
		}
	}

	private void loadValidations(BuilderState<S> s0, String prefix) {

		Set<Class<? extends Validation>> subtypes = new Reflections().getSubTypesOf(Validation.class);

		for (Class<? extends Validation> subtype : subtypes) {
			if (subtype.isAnnotationPresent(Component.class)) {
				String name = subtype.getAnnotation(Component.class).value();
				s0.getValidations().put(name, (Class<Validation<S>>) subtype);
			}
		}
	}

	private void loadFailures(BuilderState<S> s0, String prefix) {

		Set<Class<? extends Failure>> subtypes = new Reflections().getSubTypesOf(Failure.class);

		for (Class<? extends Failure> subtype : subtypes) {
			if (subtype.isAnnotationPresent(Component.class)) {
				String name = subtype.getAnnotation(Component.class).value();
				s0.getFailures().put(name, (Class<Failure<S>>) subtype);
			}
		}
	}

	private String getComponentName(Class<?> c) {
		return c.getAnnotation(Component.class).value();
	}
}
