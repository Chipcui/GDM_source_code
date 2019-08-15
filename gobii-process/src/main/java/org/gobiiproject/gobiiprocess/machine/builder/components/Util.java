package org.gobiiproject.gobiiprocess.machine.builder.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.gobiiproject.gobiiprocess.machine.builder.*;
import org.gobiiproject.gobiiprocess.machine.builder.Dependency;
import org.gobiiproject.gobiiprocess.machine.components.Fundamental;
import org.gobiiproject.gobiiprocess.machine.exceptions.DependencyException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {

	private static final String COMPONENT_BUILD_EXCEPTION_TEMPLATE_WITH_ARGUMENTS =
			"Exception when building component of type %s with argument %s";

	private static final String COMPONENT_BUILD_EXCEPTION_TEMPLATE =
			"Exception when building component of type %s";

	public static <S,T extends Fundamental<S>> Fundamental<S> buildComponent(BuilderState<S> state,
													Map<String, Class<T>> classes,
													JsonNode componentSchema) {

		T component = null;

		if (componentSchema.isObject()) {

			final String name = componentSchema.get(Schema.Component.NAME).asText();

			Class<T> componentClass = classes.get(name);

			try {
				JsonNode config = componentSchema.get(Schema.Component.CONFIG);
				component = new ObjectMapper().treeToValue(config, componentClass);
			} catch (JsonProcessingException e) {
				String message = String.format(COMPONENT_BUILD_EXCEPTION_TEMPLATE_WITH_ARGUMENTS,
						                       componentClass.toString(),
						                       componentSchema.asText());
				state.getExceptions().add(new Exception(message, e));
			}
		} else if (componentSchema.isTextual()) {

			final String name = componentSchema.asText();
			Class<T> componentClass = classes.get(name);

			try {
				component = componentClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				String message = String.format(COMPONENT_BUILD_EXCEPTION_TEMPLATE,
						componentClass);
				state.getExceptions().add(new Exception(message, e));
			}
		}

		if (component == null) {
			state.getExceptions().add(
					new BuildException(String.format("Component defined by %s could not be built",
							                         componentSchema.asText())));
			return null;
		}

		try {
			fulfillDependencies(state, component);
		} catch (DependencyException e) {
			state.getExceptions().add(e);
		}

		return component;
	}

	private static <S> void fulfillDependencies(BuilderState<S> s0, Fundamental fundamental) throws DependencyException {

		Set<Field> neededFields = Arrays.stream(fundamental.getClass().getDeclaredFields())
				.filter(f -> f.isAnnotationPresent(Dependent.class))
				.collect(Collectors.toSet());

		for (Field field : neededFields) {

			final String dependencyName = field.getAnnotation(Dependent.class).value();

			Object dependency = null;

			if (s0.getDependencies().containsKey(dependencyName)) {
				dependency = s0.getDependencies().get(dependencyName);
			} else if (s0.getDefaultImplementations().containsKey(dependencyName)
			        && s0.getDependencies().containsKey(s0.getDefaultImplementations().get(dependencyName))) {
				dependency = s0.getDependencies().get(s0.getDefaultImplementations().get(dependencyName));
			} else {
				throw new DependencyException(
						String.format("Dependency: %s not defined, required by %s", dependencyName,
								fundamental.getClass().getAnnotation(Component.class).value()));
			}

			if (dependency instanceof Dependency) {
				Dependency d = (Dependency) dependency;
				if (! d.isValid()) {
					d.initialize();
					if (! d.isValid()) {
						throw new DependencyException(String.format("Dependency of name %s could not be initialized", dependencyName));
					}
				}
			}

			setField(fundamental, field, dependency);
		}
	}

	public static Object setField(Object object, Field field, Object value) throws DependencyException {
		try {
			String fieldName = field.getName();
			Method m = object.getClass().getMethod("set" + StringUtils.capitalize(fieldName), value.getClass());
			m.invoke(object, value);
		} catch (NoSuchMethodException e) {
			throw new DependencyException(String.format("No setter defined for %s in %s", field, value.getClass()));
		} catch (IllegalAccessException e) {
			throw new DependencyException(String.format("Setter for dependency %s in %s is not accessible", field, value.getClass()));
		} catch (InvocationTargetException e) {
			throw new DependencyException(String.format("Exception when invoking setter for %s in %s", field, value.getClass()));
		}

		return object;
	}
}
