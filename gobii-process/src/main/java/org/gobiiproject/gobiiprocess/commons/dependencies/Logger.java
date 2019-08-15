package org.gobiiproject.gobiiprocess.commons.dependencies;

import lombok.Setter;
import org.gobiiproject.gobiiprocess.machine.builder.Dependency;
import org.slf4j.LoggerFactory;

public class Logger implements Dependency {

	@Setter
	private String target;

	private org.slf4j.Logger logger;

	@Override
	public void initialize() {
		logger = LoggerFactory.getLogger(target);
	}

	@Override
	public boolean isValid() {
		return logger != null;
	}

	@Override
	public void release() {

	}
}
