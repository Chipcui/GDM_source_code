package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Data
public class Pipeline<S> implements Transition<S> {

	private List<Prototype<S>> prototypes = new LinkedList<>();

	private List<Step<S>> steps = new LinkedList<>();

	public void accept(S s0) {

		for (Step<S> step : steps) {

			Predicate<S> validator = buildValidator(s0, step, prototypes);
			Consumer<S> sideEffect = buildSideEffect(s0, step, prototypes);
			Consumer<S> failure = buildFailure(s0, step, prototypes);

			step.getTransition().accept(s0);

			if (validator.test(s0)) {
				sideEffect.accept(s0);
			}
			else {
				failure.accept(s0);
				break;
			}
		}
	}

	private Predicate<S> buildValidator(S s0, Step<S> step, List<Prototype<S>> prototypes) {

		Predicate<S> validator = step.getValidation().apply(s0);

		for (Prototype<S> proto : prototypes) {
			validator = validator.and(proto.getValidation().apply(s0));
		}

		for (Prototype<S> proto : step.getPrototypes()) {
			validator = validator.and(proto.getValidation().apply(s0));
		}

		return validator;
	}

	private Consumer<S> buildSideEffect(S s0, Step<S> step, List<Prototype<S>> prototypes) {

		Consumer<S> sideEffect = s -> {};

		for (SideEffect<S> se : step.getSideEffects()) {
			sideEffect = sideEffect.andThen(se.apply(s0));
		}

		for (Prototype<S> proto : prototypes) {
			for (SideEffect<S> se : proto.getSideEffects()) {
				sideEffect = sideEffect.andThen(se.apply(s0));
			}
		}

		for (Prototype<S> proto : step.getPrototypes()) {
			for (SideEffect<S> se : proto.getSideEffects()) {
				sideEffect = sideEffect.andThen(se.apply(s0));
			}
		}

		return sideEffect;
	}

	private Consumer<S> buildFailure(S s0, Step<S> step, List<Prototype<S>> prototypes) {

		Consumer<S> failure = step.getFailure().apply(s0);

		for (Prototype<S> proto : prototypes) {
			failure = failure.andThen(proto.getFailure().apply(s0));
		}

		for (Prototype<S> proto : step.getPrototypes()) {
			failure = failure.andThen(proto.getFailure().apply(s0));
		}

		return failure;
	}
}
