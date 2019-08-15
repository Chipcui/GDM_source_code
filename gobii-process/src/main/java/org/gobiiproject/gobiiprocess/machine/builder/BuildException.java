package org.gobiiproject.gobiiprocess.machine.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BuildException extends Exception {

	public BuildException(String msg) {
		super(msg);
	}

}
