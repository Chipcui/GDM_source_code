package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiiprocess.machine.builder.BuildException;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.components.Pipeline;
import org.gobiiproject.gobiiprocess.machine.components.Prototype;
import org.gobiiproject.gobiiprocess.machine.components.Step;
import org.gobiiproject.gobiiprocess.machine.components.Transition;

public class Pipelines<S> implements Transition<BuilderState<S>> {

	@Override
	public void accept(BuilderState<S> s0) {

		JsonNode schema = s0.getSchema();

		if (! schema.has(Schema.PIPELINES)) {
			return;
		}

		final JsonNode pipelines = schema.get(Schema.PIPELINES);

		if (! pipelines.isObject()) {
			s0.getExceptions().add(new BuildException("Prototype Schema must be of type Object"));
			return;
		}

		pipelines.fieldNames().forEachRemaining(
				f -> s0.getPipelines().put(f, buildPipeline(pipelines.get(f), s0)));
	}

	private Pipeline<S> buildPipeline(JsonNode pipelineSchema, BuilderState<S> state) {

		Pipeline<S> pipeline = new Pipeline<>();

		JsonNode prototypesSchema = pipelineSchema.get(Schema.Pipeline.PROTOTYPES);

		for (JsonNode prototypeSchema : prototypesSchema) {
			Prototype<S> prototype = state.getPrototypes().get(prototypeSchema.asText());
			if (prototype != null) {
				pipeline.getPrototypes().add(prototype);
			}
		}

		JsonNode stepsSchema = pipelineSchema.get(Schema.Pipeline.STEPS);

		for (JsonNode stepSchema : stepsSchema) {
			final Step<S> step = state.getSteps().get(stepSchema.asText());
			if (step != null) {
				pipeline.getSteps().add(step);
			}
		}

		return pipeline;
	}
}
