package org.gobiiproject.gobiiprocess.machine.components;

import lombok.Data;
import lombok.experimental.Accessors;

public abstract class Status {

	public abstract boolean isSuccess();
	public abstract boolean isFailure();

	public static Success success() {
		return new Success();
	}

	public static Failure failure(Failure.Code type, String description) {
		return new Failure()
				.setCode(type)
				.setDescription(description);
	}

	public static Failure validationFailure(String description) {
		return failure(Failure.Code.VALIDATION, description);
	}

	public static Failure forkNoBranchFailure(String description) {
		return failure(Failure.Code.FORK_NO_BRANCH, description);
	}

	public static class Success extends Status {
		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public boolean isFailure() {
			return false;
		}
	}

	@Data
	@Accessors(chain=true)
	public static class Failure extends Status {
		@Override
		public boolean isSuccess() {
			return false;
		}
		@Override
		public boolean isFailure() {
			return true;
		}

		public enum Code {
			VALIDATION, FORK_NO_BRANCH
		}

		private Code code;
		private String description;

	}
}
