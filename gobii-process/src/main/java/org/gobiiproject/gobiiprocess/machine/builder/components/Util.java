package org.gobiiproject.gobiiprocess.machine.builder.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.gobiiproject.gobiiprocess.machine.builder.*;
import org.gobiiproject.gobiiprocess.machine.builder.Dependency;
import org.gobiiproject.gobiiprocess.machine.components.Fundamental;
import org.gobiiproject.gobiiprocess.machine.exceptions.DependencyException;

import java.lang.reflect.*;
import java.util.*;
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

			Object dependency = resolveDependency(s0, dependencyName);

			if (dependency == null) {
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

	public static Object resolveDependency(BuilderState<?> s0, String dependencyName) throws DependencyException {

		if (s0.getDependencies().containsKey(dependencyName)) {
			return s0.getDependencies().get(dependencyName);
		} else if (s0.getDefaultImplementations().containsKey(dependencyName)) {

			List<Object> dependencies = new LinkedList<>();
			for (String s : s0.getDefaultImplementations().get(dependencyName)) {
				Object dependency = s0.getDependencies().get(s);
				if (dependency == null) {
					throw new DependencyException(String.format("Dependency not defined: %s", s));
				}
				dependencies.add(dependency);
			}

			return dependencies.stream()
					.reduce(Util::unionObjects)
					.get();
		}
		return null;
	}

	private static boolean containsMethod(Class<?> c, Method method) {
		return method.getDeclaringClass().isAssignableFrom(c);
	}

	private static Object invoker(Method method, Object target0, Object target1, Object[] args) throws InvocationTargetException, IllegalAccessException {


		if (target0 == null || ! containsMethod(target0.getClass(), method)) {
			return method.invoke(target1, args);
		} else if (target1 == null || ! containsMethod(target1.getClass(), method)) {
			return method.invoke(target0, args);
		}

		method.invoke(target0, args);
		return method.invoke(target1, args);
	}

	public static <T> T unionObjects(T a, T b) {

		InvocationHandler handler = (o, method, args) -> {
			if (Void.TYPE.equals(method.getReturnType())) {
				invoker(method, a, b, args);
				return null;
			} else {
				return invoker(method, a, b, args);
			}
		};


		Set<Class<?>> interfaces = new HashSet<>();
		interfaces.addAll(Arrays.asList(a.getClass().getInterfaces()));
		interfaces.addAll(Arrays.asList(b.getClass().getInterfaces()));

		Class<?>[] interfacesArray = new Class<?>[interfaces.size()];

		int i = 0;
		for (Class<?> c : interfaces) {
			interfacesArray[i++] = c;
		}

		return (T) Proxy.newProxyInstance(a.getClass().getClassLoader(), interfacesArray, handler);
	}

	public static Object setField(Object object, Field field, Object value) throws DependencyException {
		String fieldName = field.getName();
		String setterName = "set" + StringUtils.capitalize(fieldName);
		try {
			Object castedValue = field.getType().cast(value);
			Method m = object.getClass().getMethod(setterName, field.getType());
			m.invoke(object, castedValue);
		} catch (NoSuchMethodException e) {
			throw new DependencyException(String.format("Dependency Injection: No setter defined for field [%s] in %s, looking for %s of type %s",
															fieldName, object.getClass().getName(), setterName, field.getClass().getName()));
		} catch (IllegalAccessException e) {
			throw new DependencyException(String.format("Dependency Injection: Setter for dependency %s in %s is not accessible", field, field.getClass().getName()));
		} catch (InvocationTargetException e) {
			throw new DependencyException(String.format("Dependency Injection: Exception when invoking setter for %s in %s", field, value.getClass()));
		} catch (ClassCastException e) {
			throw new DependencyException(String.format("Dependency Injection: Cannot cast %s to %s for field %s in %s",
															value.getClass(), field.getClass(), field.getName(), object.getClass()));
		}

		return object;
	}
}
