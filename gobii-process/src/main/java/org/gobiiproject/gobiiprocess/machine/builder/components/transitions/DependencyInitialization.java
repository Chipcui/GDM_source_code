package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Implementation;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.builder.Abstract;
import org.gobiiproject.gobiiprocess.machine.components.Transition;
import org.reflections.Reflections;

import java.lang.reflect.AnnotatedType;
import java.util.*;
import java.util.function.Consumer;

public class DependencyInitialization<S> implements Transition<BuilderState<S>> {

	@Override
	public void accept(BuilderState<S> builderState) {

		JsonNode packages = builderState.getSchema().get(Schema.PACKAGES);

		for (JsonNode pack : packages) {
			loadAbstracts(builderState, pack.asText());
			loadImplementations(builderState, pack.asText());
		}
	}

	private void loadAbstracts(BuilderState<S> s0, String prefix) {

		Set<Class<?>> types = new Reflections(prefix).getTypesAnnotatedWith(Abstract.class);
		
		for (Class<?> c : types) {
			if (c.getAnnotation(Abstract.class) != null) {
				s0.getAbstractDependencies().put(c.getAnnotation(Abstract.class).value(), c);
			}
		}

	}

	private void loadImplementations(BuilderState<S> s0, String prefix) {

		for (Class<?> abstractClass : s0.getAbstractDependencies().values()) {
			for (Class<?> subtype : new Reflections(prefix).getSubTypesOf(abstractClass)) {
				Arrays.stream(subtype.getAnnotatedInterfaces())
						.filter(at -> at.isAnnotationPresent(Implementation.class))
						.forEach(at -> s0.getImplementedDependencies()
								         .put(at.getAnnotation(Implementation.class).value(),
	          								  subtype));
			}
		}
	}
}
