package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.BuildException;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.builder.components.Util;
import org.gobiiproject.gobiiprocess.machine.components.Prototype;
import org.gobiiproject.gobiiprocess.machine.components.Transition;

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

		if (proto.has(Schema.Prototype.SIDE_EFFECTS)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.SIDE_EFFECTS);
			for (JsonNode n : sideEffectSchema) {
				prototype.getSideEffects().add(Util.buildComponent(state, state.getSideEffects(), n));
			}
		}

		if (proto.has(Schema.Prototype.VALIDATION)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.VALIDATION);
			prototype.setValidation(Util.buildComponent(state, state.getValidations(), sideEffectSchema));
		}

		if (proto.has(Schema.Prototype.FAILURE)) {
			JsonNode sideEffectSchema = proto.get(Schema.Prototype.FAILURE);
			prototype.setFailure(Util.buildComponent(state, state.getFailures(), sideEffectSchema));
		}

		return prototype;
	}



}
