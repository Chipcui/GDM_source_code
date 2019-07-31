package org.gobiiproject.gobiiprocess.machine;


public interface Transition<S> {

	S run(S s0);
}
