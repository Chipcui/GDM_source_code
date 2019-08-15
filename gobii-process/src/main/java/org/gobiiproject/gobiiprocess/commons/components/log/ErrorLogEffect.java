package org.gobiiproject.gobiiprocess.commons.components.log;

import org.gobiiproject.gobiiprocess.commons.components.CommonState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;

import java.util.function.Consumer;

@Component("log/error")
public class ErrorLogEffect extends LogEffect<CommonState> {

	@Override
	public Consumer<CommonState> apply(CommonState s0) {

		final int numErrors = s0.getErrorLog().size();
		return s1 -> {
			for (int i = numErrors ; i < s1.getErrorLog().size() ; i++) {

			}
		};
	}

}
