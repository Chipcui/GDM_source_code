package org.gobiiproject.gobiiprocess.commons.model.log;

import lombok.Data;

@Data
public class Log {

	private String message;

	@Override
	public String toString() {
		return message;
	}
}
