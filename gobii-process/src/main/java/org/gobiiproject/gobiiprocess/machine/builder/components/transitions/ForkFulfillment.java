package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.BuildException;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.components.Fork;
import org.gobiiproject.gobiiprocess.machine.components.Pipe;
import org.gobiiproject.gobiiprocess.machine.components.Pipeline;
import org.gobiiproject.gobiiprocess.machine.components.Transition;

public class ForkFulfillment<S> implements Transition<BuilderState<S>> {

	public void accept(BuilderState<S> s0) {

		final JsonNode schema = s0.getSchema();

		if (! schema.has(Schema.FORKS)) {
			return;
		}

		if (! schema.isObject()) {
			s0.getExceptions().add(new BuildException("Branch Schema must be of type Object"));
		}

		final JsonNode forks = schema.get(Schema.FORKS);

		forks.fieldNames().forEachRemaining(
				f -> s0.getForks().put(f, fulfillFork(forks.get(f), s0)));
	}

	private Fork<S> fulfillFork(JsonNode forkSchema, BuilderState<S> s0) {

		if (! forkSchema.isObject()) {
			s0.getExceptions().add(new BuildException("Branch Schema must be of type Object"));
			return null;
		}

		Fork<S> fork = new Fork<S>();

		forkSchema.fieldNames().forEachRemaining(
				f -> fork.getBranches().put(s0.getGates().get(f), buildBranch(s0, forkSchema.get(f))));

		return fork;
	}

	private Pipe<S> buildBranch(BuilderState<S> s0, JsonNode forkSchema) {

		if (forkSchema.isArray()) {
			Pipeline<S> pipeline = new Pipeline<>();
			for (JsonNode pipe : forkSchema) {
				pipeline.getPipes().add(s0.getPipe(pipe.asText()));
			}
			return pipeline;
		} else if (forkSchema.isTextual()) {
			return s0.getPipe(forkSchema.asText());
		}

		return null;
	}
}
