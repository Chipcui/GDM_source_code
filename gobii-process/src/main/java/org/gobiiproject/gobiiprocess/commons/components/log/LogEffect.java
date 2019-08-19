package org.gobiiproject.gobiiprocess.commons.components.log;

import lombok.Data;
import org.gobiiproject.gobiiprocess.commons.model.CommonState;
import org.gobiiproject.gobiiprocess.commons.dependencies.logger.Logger;
import org.gobiiproject.gobiiprocess.machine.builder.Dependent;
import org.gobiiproject.gobiiprocess.machine.components.SideEffect;

@Data
public abstract class LogEffect<T extends CommonState> implements SideEffect<T> {

	@Dependent("logger")
	private Logger logger;
}
