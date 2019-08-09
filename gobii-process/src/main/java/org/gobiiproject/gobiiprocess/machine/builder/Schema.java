package org.gobiiproject.gobiiprocess.machine.builder;

public class Schema {

	public static final String PACKAGES = "packages";
	public static final String PROTOTYPES = "prototypes";
	public static final String FORKS = "forks";
	public static final String STEPS = "steps";
	public static final String PIPELINES = "pipelines";

	public static class Prototype {

		public static final String SIDE_EFFECTS = "sideEffects";
		public static final String FAILURE = "failure";
		public static final String VALIDATION = "validation";

	}

	public static class Step {

		public static final String PROTOTYPES = "prototypes";
		public static final String TRANSITION = "transition";
		public static final String SIDE_EFFECTS = "sideEffects";
		public static final String FAILURE = "failure";
		public static final String VALIDATION = "validation";

	}

	public static class Pipeline {

		public static final String PROTOTYPES = "prototypes";
		public static final String STEPS = "steps";
	}

	public static class SideEffect {
		public static final String NAME = "name";
		public static final String CONFIG = "config";
	}

	public static class Validation {
		public static final String NAME = "name";
		public static final String CONFIG = "config";
	}

	public static class Failure {
		public static final String NAME = "name";
		public static final String CONFIG = "config";
	}

	public static class Component {
		public static final String NAME = "name";
		public static final String CONFIG = "config";
	}
}
