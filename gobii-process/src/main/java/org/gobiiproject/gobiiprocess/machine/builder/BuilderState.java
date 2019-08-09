package org.gobiiproject.gobiiprocess.machine.builder;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.gobiiproject.gobiiprocess.machine.components.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class BuilderState<S> {

	private JsonNode schema;

	private Pipeline<S> stateMachine = new Pipeline<>();

	private Map<String, Class<SideEffect<S>>> sideEffects = new HashMap<>();
	private Map<String, Class<Transition<S>>> transitions = new HashMap<>();
	private Map<String, Class<Failure<S>>> failures = new HashMap<>();
	private Map<String, Class<Validation<S>>> validations = new HashMap<>();

	private Map<String, Gate<S>> gates = new HashMap<>();
	private Map<String, Prototype<S>> prototypes = new HashMap<>();
	private Map<String, Fork<S>> forks = new HashMap<>();
	private Map<String, Step<S>> steps = new HashMap<>();
	private Map<String, Pipeline<S>> pipelines = new HashMap<>();

	private List<Exception> exceptions = new LinkedList<>();

	public Pipe<S> getPipe(String name) {

		if (pipelines.containsKey(name)) {
			return pipelines.get(name);
		} else if (steps.containsKey(name)) {
			return steps.get(name);
		}

		return null;
	}
}
