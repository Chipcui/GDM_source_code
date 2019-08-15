package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.BuildException;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.builder.components.Util;
import org.gobiiproject.gobiiprocess.machine.components.*;

public class Prototypes<S> implements Transition<BuilderState<S>> {

	@Override
	public void accept(BuilderState<S> s0) {

		JsonNode schema = s0.getSchema();

		if (! schema.has(Schema.PROTOTYPES)) {
			return;
		}

		final JsonNode prototypes = schema.get(Schema.PROTOTYPES);

		if (! prototypes.isObject()) {
			s0.getExceptions().add(new BuildException("Prototype Schema must be of type Object"));
			return;
		}

		prototypes.fieldNames().forEachRemaining(
				f -> s0.getPrototypes().put(f, buildPrototype(prototypes.get(f), s0)));
	}

	private Prototype<S> buildPrototype(JsonNode proto, BuilderState<S> state) {

		Prototype<S> prototype = new Prototype<>();

		if (proto.has(Schema.Prototype.TRANSITION)) {
			JsonNode transitionSchema = proto.get(Schema.Prototype.TRANSITION);
			prototype.setTransition((Transition<S>) Util.buildComponent(state, state.getValidations(), transitionSchema));
		}

		if (proto.has(Schema.Prototype.SIDE_EFFECTS)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.SIDE_EFFECTS);
			for (JsonNode n : sideEffectSchema) {
				prototype.getSideEffects().add((SideEffect<S>) Util.buildComponent(state, state.getSideEffects(), n));
			}
		}

		if (proto.has(Schema.Prototype.VALIDATION)) {
			JsonNode validationSchema = proto.get(Schema.Prototype.VALIDATION);
			prototype.setValidation((Validation<S>) Util.buildComponent(state, state.getValidations(), validationSchema));
		}

		if (proto.has(Schema.Prototype.FAILURE)) {
			JsonNode failureSchema = proto.get(Schema.Prototype.FAILURE);
			prototype.setFailure((Failure<S>) Util.buildComponent(state, state.getFailures(), failureSchema));
		}

		return prototype;
	}



}
