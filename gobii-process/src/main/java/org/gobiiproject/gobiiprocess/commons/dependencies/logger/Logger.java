package org.gobiiproject.gobiiprocess.commons.dependencies.logger;

import org.gobiiproject.gobiiprocess.commons.Util;
import org.gobiiproject.gobiiprocess.machine.builder.Abstract;

@Abstract("logger")
public interface Logger {

	enum LogLevel {
		DEBUG, INFO, WARN, ERROR
	}
	
	void log(LogLevel logLevel, String log);

	default void log(LogLevel logLevel, Throwable t) {
		log(logLevel, Util.throwableToStackTraceString(t));
	}

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

	default void debug(Throwable log) {
		log(LogLevel.DEBUG, log);
	}

	default void info(Throwable log) {
		log(LogLevel.INFO, log);
	}

	default void warn(Throwable log) {
		log(LogLevel.WARN, log);
	}

	default void error(Throwable log) {
		log(LogLevel.ERROR, log);
	}
}
