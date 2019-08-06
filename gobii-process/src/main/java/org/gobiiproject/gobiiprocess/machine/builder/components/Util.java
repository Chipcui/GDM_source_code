package org.gobiiproject.gobiiprocess.machine.builder.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;

import java.util.Map;

public class Util {

	private static final String COMPONENT_BUILD_EXCEPTION_TEMPLATE_WITH_ARGUMENTS =
			"Exception when building component of type %s with argument %s";

	private static final String COMPONENT_BUILD_EXCEPTION_TEMPLATE =
			"Exception when building component of type %s";

	public static <T> T buildComponent(BuilderState<?> state, Map<String, Class<T>> classes, JsonNode componentSchema) {

		T component = null;

		if (componentSchema.isObject()) {

			final String name = componentSchema.get(Schema.SideEffect.NAME).asText();

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

		return component;
	}
}
