package org.gobiiproject.gobiiprocess.commons.dependencies.logger;

import org.gobiiproject.gobiiprocess.machine.builder.Implementation;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Slf4jLogger implements @Implementation("logger") Logger {

	private Map<String, org.slf4j.Logger> loggers = new HashMap<>();

	@Override
	public void log(LogLevel logLevel, String log) {

		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		final String callingClass = stackTrace[0].getClassName();

		final org.slf4j.Logger logger;

		if (loggers.containsKey(callingClass)) {
			logger = loggers.get(callingClass);
		} else {
			logger = LoggerFactory.getLogger(callingClass);
			loggers.put(callingClass, logger);
		}

		switch(logLevel) {
			case DEBUG:
				logger.debug(log);
			case INFO:
				logger.info(log);
			case WARN:
				logger.warn(log);
			case ERROR:
				logger.error(log);
		}
	}
}
