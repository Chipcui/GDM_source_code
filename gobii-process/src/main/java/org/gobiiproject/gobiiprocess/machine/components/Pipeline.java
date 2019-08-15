package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;
import java.util.LinkedList;
import java.util.List;

@Data
public class Pipeline<S> implements Pipe<S> {

	private List<Prototype<S>> prototypes = new LinkedList<>();

	private List<Pipe<S>> pipes = new LinkedList<>();

	public Status apply(S s0) {

		for (Pipe<S> pipe : pipes) {

			Pipe<S> fullPipe = pipe;
			if (pipe instanceof Step) {
				Prototype<S> step = (Step<S>) pipe;
				for (Prototype<S> p : prototypes) {
					step = step.join(p);
				}
				fullPipe = step;
			}

			Status status = fullPipe.apply(s0);
			if (status.isSuccess()) {
				continue;
			} else {
				return status;
			}
		}

		return Status.success();
	}
}
