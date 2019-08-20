package org.gobiiproject.gobiiprocess.commons.model.log;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class Error extends Log {

	public enum Code {

		PARSE(0, "");

		private Integer number;
		private String description;

		Code(Integer number, String description) {
			this.number = number;
			this.description = description;
		}

		public Integer getNumber() {
			return this.number;
		}

		public String getDescription() {
			return this.description;
		}

		@Override
		public String toString() {
			return String.format("%s: %s", number, description);
		}
	}

	private Code code;
	private Throwable exception;

	public Error(Code code, String message) {
		super(message);
		this.code = code;
	}

	public Error(Code code, String message, Throwable exception) {
		this(code, message);
		this.exception = exception;
	}

	@Override
	public String toString() {
		return String.format("%s -- %s", code, super.toString());
	}
}
