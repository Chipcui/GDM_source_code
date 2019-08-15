package org.gobiiproject.gobiiprocess.machine.builder;

public interface Dependency {

	void initialize();

	boolean isValid();

	void release();
}
