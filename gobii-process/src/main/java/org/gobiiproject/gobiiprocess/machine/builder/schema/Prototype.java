package org.gobiiproject.gobiiprocess.machine.builder.schema;

import lombok.Data;

import java.util.List;

@Data
public class Prototype {

	private List<SideEffectSchema> sideEffects;

	private ValidationSchema validation;

	private FailureSchema failure;
}
