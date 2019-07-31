package org.gobiiproject.gobiiprocess.machine.builder.schema;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Schema {

	private String name;
	private Map<String, Prototype> prototypes = new HashMap<>();

	private List<TransitionSchema> transitions;
}
