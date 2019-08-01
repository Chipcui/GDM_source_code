package org.gobiiproject.gobiiprocess.machine.components;


public interface Transition<S> {

	void run(S s0);
}
