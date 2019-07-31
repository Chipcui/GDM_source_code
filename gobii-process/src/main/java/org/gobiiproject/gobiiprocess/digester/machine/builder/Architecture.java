package org.gobiiproject.gobiiprocess.digester.machine.builder;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Architecture {

	private String name;
	private Map<String, Prototype> prototypes = new HashMap<>();

	private List<TransitionSchema> transitions;
}
