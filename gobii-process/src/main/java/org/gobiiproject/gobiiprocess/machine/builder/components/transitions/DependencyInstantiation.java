package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiiprocess.machine.builder.*;
import org.gobiiproject.gobiiprocess.machine.components.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class DependencyInstantiation<S> implements Transition<BuilderState<S>> {

	@Override
	public void accept(BuilderState<S> s0) {
		JsonNode schema = s0.getSchema();

		if (schema.get(Schema.DEPENDENCIES) == null) {
			return;
		}

		JsonNode defaults = schema.get(Schema.DEPENDENCIES)
				                  .get(Schema.Dependency.DEFAULTS);

		defaults.fieldNames().forEachRemaining(
				f -> {
					List<String> d = new LinkedList<>();
					if (defaults.get(f).isTextual()) {
						d = Arrays.asList(defaults.get(f).asText());
					} else {
						for (JsonNode n : defaults.get(f)) {
							d.add(n.asText());
						}
					}
					s0.getDefaultImplementations().put(f, d);
				});

		JsonNode implementations = schema.get(Schema.DEPENDENCIES)
				                         .get(Schema.Dependency.IMPLEMENTATIONS);

		implementations.fieldNames().forEachRemaining(
				f -> s0.getDependencies().put(f, buildDependency(f, implementations.get(f), s0)));

	}

	private Object buildDependency(String name, JsonNode dependencySchema, BuilderState<S> s0) {
		Class<?> implementedClass = s0.getImplementedDependencies().get(name);
		if (implementedClass == null) {
			s0.getExceptions().add(new BuildException(String.format("Dependency implementation for %s could not be resolve", name)));
			return null;
		}
		return new ObjectMapper().convertValue(dependencySchema, implementedClass);
	}

}
