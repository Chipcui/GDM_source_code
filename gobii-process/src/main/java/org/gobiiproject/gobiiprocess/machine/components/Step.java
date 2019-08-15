package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Data
public class Step<S> extends Prototype<S> {

	private List<Prototype<S>> prototypes = new LinkedList<>();

	@Override
	public Status apply(S s0) {

		Consumer<S> t = this.getTransition();

		Predicate<S> v = this.getValidation().apply(s0);

		Consumer<S> e = s -> {};
		for (SideEffect<S> se : this.getSideEffects()) {
			e = e.andThen(se.apply(s0));
		}

		Consumer<S> f = this.getFailure().apply(s0);

		for (Prototype<S> proto : prototypes) {
			t = t.andThen(proto.getTransition());
			v = v.and(proto.getValidation().apply(s0));
			for (SideEffect<S> se : proto.getSideEffects()) {
				e = e.andThen(se.apply(s0));
			}
			f = f.andThen(proto.getFailure().apply(s0));
		}

		t.accept(s0);

		if (v.test(s0)) {
			e.accept(s0);
			return Status.success();
		} else {
			f.accept(s0);
			return Status.validationFailure("Result of step could not be validated");
		}
	}
}
