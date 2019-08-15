package org.gobiiproject.gobiiprocess.commons.dependencies.logger;

import org.gobiiproject.gobiiprocess.machine.builder.Abstract;

@Abstract("logger")
public interface Logger {

	enum LogLevel {
		DEBUG, INFO, WARN, ERROR
	}
	
	void log(LogLevel logLevel, String log);

	default void debug(String log) {
		log(LogLevel.DEBUG, log);
	}

	default void info(String log) {
		log(LogLevel.INFO, log);
	}

	default void warn(String log) {
		log(LogLevel.WARN, log);
	}

	default void error(String log) {
		log(LogLevel.ERROR, log);
	}
}
