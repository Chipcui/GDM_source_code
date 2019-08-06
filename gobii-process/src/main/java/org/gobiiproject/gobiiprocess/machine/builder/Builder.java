package org.gobiiproject.gobiiprocess.machine.builder;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.components.failures.ExceptionFailure;
import org.gobiiproject.gobiiprocess.machine.builder.components.transitions.Pipelines;
import org.gobiiproject.gobiiprocess.machine.builder.components.transitions.Prototypes;
import org.gobiiproject.gobiiprocess.machine.builder.components.transitions.Setup;
import org.gobiiproject.gobiiprocess.machine.builder.components.transitions.Steps;
import org.gobiiproject.gobiiprocess.machine.builder.components.validations.ExceptionValidator;
import org.gobiiproject.gobiiprocess.machine.components.*;

import java.util.Map;

public class Builder<S> {

	private Pipeline<BuilderState<S>> setup() {

		// Validations
		Validation<BuilderState<S>> exceptionValidation = new ExceptionValidator<>();

		// Failures
		Failure<BuilderState<S>> exceptionFailure = new ExceptionFailure<>();

		// Prototypes
		Prototype<BuilderState<S>> exceptionHandlingPrototype = new Prototype<>();
		exceptionHandlingPrototype.setValidation(exceptionValidation);
		exceptionHandlingPrototype.setFailure(exceptionFailure);

		// Steps
		Step<BuilderState<S>> setupStep = new Step<>();
		setupStep.setTransition(new Setup<>());

		Step<BuilderState<S>> prototypesStep = new Step<>();
		prototypesStep.setTransition(new Prototypes<>());

		Step<BuilderState<S>> stepsStep = new Step<>();
		stepsStep.setTransition(new Steps<>());

		Step<BuilderState<S>> pipelinesStep = new Step<>();
		pipelinesStep.setTransition(new Pipelines<>());

		// Pipeline
		Pipeline<BuilderState<S>> pipeline = new Pipeline<>();

		pipeline.getPrototypes().add(exceptionHandlingPrototype);

		pipeline.getSteps().add(setupStep);
		pipeline.getSteps().add(prototypesStep);
		pipeline.getSteps().add(stepsStep);
		pipeline.getSteps().add(pipelinesStep);

		return pipeline;
	}

	public Map<String, Pipeline<S>> build(JsonNode schema) {;

		Pipeline<BuilderState<S>> builderPipeline = setup();
		BuilderState<S> state = new BuilderState<>();
		state.setSchema(schema);

		builderPipeline.accept(state);

		return state.getPipelines();
	}

}
