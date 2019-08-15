package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Fork<S> implements Pipe<S> {

	@Getter
	private Map<Predicate<S>, Pipe<S>> branches = new LinkedHashMap<>();

	@Override
	public Status apply(S s0) {

		for (Map.Entry<? extends Predicate<S>, Pipe<S>> branch : branches.entrySet()) {
			if (branch.getKey().test(s0)) {
				return branch.getValue().apply(s0);
			}
		}

		return Status.forkNoBranchFailure("No branch was in fork");
	}
}
