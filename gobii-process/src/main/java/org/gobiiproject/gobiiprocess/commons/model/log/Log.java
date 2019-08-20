package org.gobiiproject.gobiiprocess.commons.model.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
@Builder
public class Log {

	private String message;

	@Override
	public String toString() {
		return message;
	}
}
