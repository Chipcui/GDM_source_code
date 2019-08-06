package org.gobiiproject.gobiiprocess.machine.builder.processor;

import lombok.Data;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

@Data
public class ProcessorModel {

	private Map<String, ComponentGroup<?>> groups = new HashMap<>();

	public void addComponent(TypeElement component, TypeElement target, Class<?> archetype) {


	}
}
