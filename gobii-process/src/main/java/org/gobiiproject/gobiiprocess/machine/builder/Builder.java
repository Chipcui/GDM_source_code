package org.gobiiproject.gobiiprocess.machine.builder;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiiprocess.machine.builder.components.failures.ExceptionFailure;
import org.gobiiproject.gobiiprocess.machine.builder.components.transitions.*;
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

		Step<BuilderState<S>> dependenciesInitializationStep = new Step<>();
		dependenciesInitializationStep.setTransition(new DependencyInitialization<>());

		Step<BuilderState<S>> dependenciesInstantiationStep = new Step<>();
		dependenciesInstantiationStep.setTransition(new DependencyInstantiation<>());

		Step<BuilderState<S>> prototypesStep = new Step<>();
		prototypesStep.setTransition(new Prototypes<>());

		Step<BuilderState<S>> forkInitializationStep = new Step<>();
		forkInitializationStep.setTransition(new ForkInitialization<>());

		Step<BuilderState<S>> stepsStep = new Step<>();
		stepsStep.setTransition(new Steps<>());

		Step<BuilderState<S>> pipelinesStep = new Step<>();
		pipelinesStep.setTransition(new Pipelines<>());

		Step<BuilderState<S>> forkFulfillmentStep = new Step<>();
		forkFulfillmentStep.setTransition(new ForkFulfillment<>());

		// Pipeline
		Pipeline<BuilderState<S>> pipeline = new Pipeline<>();

		pipeline.getPrototypes().add(exceptionHandlingPrototype);

		pipeline.getPipes().add(setupStep);
		pipeline.getPipes().add(dependenciesInitializationStep);
		pipeline.getPipes().add(dependenciesInstantiationStep);
		pipeline.getPipes().add(prototypesStep);
		pipeline.getPipes().add(forkInitializationStep);
		pipeline.getPipes().add(stepsStep);
		pipeline.getPipes().add(pipelinesStep);
		pipeline.getPipes().add(forkFulfillmentStep);

		return pipeline;
	}

	public Map<String, Pipeline<S>> build(JsonNode schema) {;

		Pipeline<BuilderState<S>> builderPipeline = setup();
		BuilderState<S> state = new BuilderState<>();
		state.setSchema(schema);

		builderPipeline.apply(state);

		return state.getPipelines();
	}

}
