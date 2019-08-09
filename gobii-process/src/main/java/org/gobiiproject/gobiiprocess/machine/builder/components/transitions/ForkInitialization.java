package org.gobiiproject.gobiiprocess.machine.builder.components.transitions;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.BuildException;
import org.gobiiproject.gobiiprocess.machine.builder.BuilderState;
import org.gobiiproject.gobiiprocess.machine.builder.Schema;
import org.gobiiproject.gobiiprocess.machine.components.Fork;
import org.gobiiproject.gobiiprocess.machine.components.Transition;

public class ForkInitialization<S> implements Transition<BuilderState<S>> {

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
				f -> s0.getForks().put(f, initializeFork(forks.get(f), s0)));
	}

	private Fork<S> initializeFork(JsonNode branchSchema, BuilderState<S> s0) {

		Fork<S> fork = new Fork<>();

		return fork;
	}

}
