package org.gobiiproject.gobiiprocess.machine.components;

public interface Dependency {

	void initialize();

	boolean isValid();

	void release();
}
