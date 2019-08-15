package org.gobiiproject.gobiiprocess.commons.components.log;

import org.gobiiproject.gobiiprocess.commons.components.CommonState;
import org.gobiiproject.gobiiprocess.digester.machine.Digester;
import org.gobiiproject.gobiiprocess.machine.components.SideEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LogEffect<T extends CommonState> implements SideEffect<T> {

	public enum LogLevel {
		DEBUG, INFO, WARN, ERROR
	}

	private static Logger LOGGER = LoggerFactory.getLogger(Digester.class);

	protected static void debug(String message) {
		LOGGER.debug(message);
	}

	protected static void info(String message) {
		LOGGER.info(message);
	}

	protected static void warn(String message) {
		LOGGER.warn(message);
	}

	protected static void error(String message) {
		LOGGER.error(message);
	}

	protected static void log(LogLevel logLevel, String message) {

		switch(logLevel) {
			case DEBUG:
				debug(message);
			case INFO:
				info(message);
			case WARN:
				warn(message);
			case ERROR:
				error(message);
		}
	}
}
