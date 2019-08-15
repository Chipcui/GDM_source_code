package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Data
public class Prototype<S> implements Pipe<S> {

	private Transition<S> transition = s -> {};
	private Validation<S> validation = s0 -> s1 -> true;
	private Failure<S> failure = s0 -> s1 -> {};
	private List<SideEffect<S>> sideEffects = new LinkedList<>();

	@Override
	public Status apply(S s0) {

		Predicate<S> v = validation.apply(s0);

		Consumer<S> e = sideEffects.stream()
								   .reduce(SideEffect::join)
				                   .orElse(x -> y -> {})
								   .apply(s0);

		Consumer<S> f = failure.apply(s0);

		this.transition.accept(s0);

		if (v.test(s0)) {
			e.accept(s0);
			return Status.success();
		} else {
			f.accept(s0);
			return Status.validationFailure("Result of step could not be validated");
		}
	}

	public Prototype<S> join(Prototype<S> p) {
		Prototype<S> joined = new Prototype<>();
		joined.setTransition(this.transition.join(p.getTransition()));
		joined.setValidation(this.validation.join(p.getValidation()));
		joined.getSideEffects().addAll(this.sideEffects);
		joined.getSideEffects().addAll(p.getSideEffects());
		joined.setFailure(this.getFailure().join(p.getFailure()));

		return joined;
	}
}
