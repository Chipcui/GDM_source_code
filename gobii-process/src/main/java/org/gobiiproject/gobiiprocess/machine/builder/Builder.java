package org.gobiiproject.gobiiprocess.machine.builder;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.components.Setup;
import org.gobiiproject.gobiiprocess.machine.components.*;

public class Builder<S> {

	private Pipeline<BuilderState<S>> builder = new Pipeline<>();

	public Builder(String prefix) {
		setup(prefix);
	}

	private void setup(String prefix) {

		Step<BuilderState<S>> setupStep = new Step();
		setupStep.setTransition(new Setup<>());

	}

	public Pipeline<S> build(JsonNode schema) {;

		BuilderState<S> state = new BuilderState<>();

		return state.getStateMachine();
	}

}
