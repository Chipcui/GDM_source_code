package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.*;
import org.gobiiproject.gobiiprocess.machine.builder.components.Util;
import org.gobiiproject.gobiiprocess.machine.components.*;
import org.gobiiproject.gobiiprocess.machine.exceptions.DependencyException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Steps<S> implements Transition<BuilderState<S>> {


	@Override
	public void accept(BuilderState<S> s0) {

		JsonNode schema = s0.getSchema();

		if (! schema.has(Schema.STEPS)) {
			return;
		}

		final JsonNode steps = schema.get(Schema.STEPS);

		if (! steps.isObject()) {
			s0.getExceptions().add(new BuildException("Prototype Schema must be of type Object"));
			return;
		}

		steps.fieldNames().forEachRemaining(
				f -> s0.getSteps().put(f, buildStep(steps.get(f), s0)));
	}


	private Step<S> buildStep(JsonNode proto, BuilderState<S> state) {

		Step<S> step = new Step<>();

		if (proto.has(Schema.Step.PROTOTYPES)) {
			for (JsonNode name : proto.get(Schema.Step.PROTOTYPES)) {
				if (name.isTextual()) {
					Prototype<S> prototype = state.getPrototypes().get(name.asText());
					if (prototype != null) {
						step.getPrototypes().add(prototype);
					}
				}
			}
		}

		if (proto.has(Schema.Step.TRANSITION)) {
			JsonNode transitionSchema = proto.get(Schema.Step.TRANSITION);
			step.setTransition((Transition<S>) Util.buildComponent(state, state.getTransitions(), transitionSchema));
		}

		if (proto.has(Schema.Step.SIDE_EFFECTS)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.SIDE_EFFECTS);
			for (JsonNode n : sideEffectSchema) {
				step.getSideEffects().add((SideEffect<S>) Util.buildComponent(state, state.getSideEffects(), n));
			}
		}

		if (proto.has(Schema.Step.VALIDATION)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.VALIDATION);
			step.setValidation((Validation<S>) Util.buildComponent(state, state.getValidations(), sideEffectSchema));
		}

		if (proto.has(Schema.Step.FAILURE)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.FAILURE);
			step.setFailure((Failure<S>) Util.buildComponent(state, state.getFailures(), sideEffectSchema));
		}

		return step;
	}
}
