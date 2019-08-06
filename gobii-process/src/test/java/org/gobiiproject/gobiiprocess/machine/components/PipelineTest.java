package org.gobiiproject.gobiiprocess.machine.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class PipelineTest {

	private final Transition<TestState> emptyTransition = s0 -> {};
	private final Transition<TestState> increment = s -> s.val++;

	private final Validation<TestState> positiveValidation = s0 -> s1 -> {
		s1.validationCalled = true;
		return true;
	};
	private final Validation<TestState> negativeValidation = s0 -> s1 -> false;

	private final Validation<TestState> incrementValidation = s0 -> {
		final int i = s0.val;
		return s1 -> i + 1 == s1.val;
	};

	private final SideEffect<TestState> sideEffect = s0 -> s1 -> s1.sideEffectCalled = true;
	private final Failure<TestState> failure = s0 -> s1 -> s1.failureCalled = true;

	@Test
	public void testSimple() {

		Step<TestState> step = new Step<>();
		step.setTransition(increment);

		Pipeline<TestState> pipeline = new Pipeline<>();

		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertEquals(1, state.val);
	}

	@Test
	public void testValidation() {

		Step<TestState> step = new Step<>();
		step.setValidation(positiveValidation);

		Pipeline<TestState> pipeline = new Pipeline<>();

		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.validationCalled);
	}
	
	@Test
	public void testSideEffect() {

		Transition<TestState> increment = s -> {};

		Validation<TestState> incrementValidator = s0 -> s1 -> true;

		Step<TestState> step = new Step<>();
		step.setTransition(increment);
		step.setValidation(incrementValidator);
		step.getSideEffects().add(sideEffect);
		step.setFailure(failure);

		Pipeline<TestState> pipeline = new Pipeline<>();

		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.sideEffectCalled);
		assertFalse(state.failureCalled);
	}

	@Test
	public void testStepPrototypeValidation() {

		Prototype<TestState> prototype = new Prototype<>();
		prototype.setValidation(positiveValidation);

		Step<TestState> step = new Step<>();
		step.getPrototypes().add(prototype);

		Pipeline<TestState> pipeline = new Pipeline<>();

		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.validationCalled);
	}

	@Test
	public void testFailure() {

		TestState state = new TestState();

		Step<TestState> step = new Step<>();
		step.setTransition(emptyTransition);
		step.setValidation(negativeValidation);
		step.getSideEffects().add(sideEffect);
		step.setFailure(failure);

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getSteps().add(step);

		pipeline.accept(state);

		assertFalse(state.sideEffectCalled);
		assertTrue(state.failureCalled);
	}

	@Test
	public void testStepPrototypeSideEffect() {

		Prototype<TestState> prototype = new Prototype<>();
		prototype.getSideEffects().add(sideEffect);

		Step<TestState> step = new Step<>();
		step.getPrototypes().add(prototype);

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.sideEffectCalled);
	}

	@Test
	public void testPipelinePrototypeValidation() {

		Prototype<TestState> prototype = new Prototype<>();
		prototype.setValidation(positiveValidation);

		Step<TestState> step = new Step<>();

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getPrototypes().add(prototype);
		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.validationCalled);
	}

	@Test
	public void testPipelinePrototypeFailure() {

		Prototype<TestState> prototype = new Prototype<>();
		prototype.setFailure(failure);

		Step<TestState> step = new Step<>();
		step.setValidation(negativeValidation);

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getPrototypes().add(prototype);
		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.failureCalled);
	}

	@Test
	public void testPipelinePrototypeSideEffect() {

		Prototype<TestState> prototype = new Prototype<>();
		prototype.getSideEffects().add(sideEffect);

		Step<TestState> step = new Step<>();

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getPrototypes().add(prototype);
		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.sideEffectCalled);
	}

	@Test
	public void testStepPrototypeFailure() {

		Prototype<TestState> prototype = new Prototype<>();
		prototype.setFailure(failure);

		Step<TestState> step = new Step<>();
		step.setValidation(negativeValidation);

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getPrototypes().add(prototype);
		pipeline.getSteps().add(step);

		TestState state = new TestState();

		pipeline.accept(state);

		assertTrue(state.failureCalled);
	}

	@Test
	public void testFull() {

		Step<TestState> step0 = new Step<>();
		step0.setTransition(increment);
		step0.setValidation(incrementValidation);
		step0.getSideEffects().add(sideEffect);

		Step<TestState> step1 = new Step<>();
		step1.setTransition(increment);
		step1.setValidation(negativeValidation);
		step1.setFailure(failure);

		Pipeline<TestState> pipeline = new Pipeline<>();
		pipeline.getSteps().add(step0);
		pipeline.getSteps().add(step1);

		TestState state = new TestState();

		pipeline.accept(state);

		assertEquals(2, state.val);
		assertTrue(state.sideEffectCalled);
		assertTrue(state.failureCalled);
	}

	static class TestState {

		int val = 0;

		boolean validationCalled = false;

		boolean sideEffectCalled = false;

		boolean failureCalled = false;

	}
}
