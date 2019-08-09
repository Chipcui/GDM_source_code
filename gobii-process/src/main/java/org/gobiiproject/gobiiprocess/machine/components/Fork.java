package org.gobiiproject.gobiiprocess.machine.components;


import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Fork<S> implements Function<S,Pipe<S>>, Pipe<S> {

	@Getter
	private Map<Predicate<S>, Pipe<S>> branches = new LinkedHashMap<>();

	@Override
	public Pipe<S> apply(S s0) {

		for (Map.Entry<? extends Predicate<S>, Pipe<S>> branch : branches.entrySet()) {
			if (branch.getKey().test(s0)) {
				return branch.getValue();
			}
		}

		return null;
	}
}
