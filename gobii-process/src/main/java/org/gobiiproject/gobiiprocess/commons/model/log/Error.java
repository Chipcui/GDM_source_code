package org.gobiiproject.gobiiprocess.commons.model.log;

import lombok.Data;

@Data
public class Error extends Log {

	private ErrorCode code;

	@Override
	public String toString() {
		return code + " : " + super.getMessage();
	}
}
