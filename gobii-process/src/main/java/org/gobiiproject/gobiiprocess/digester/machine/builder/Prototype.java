package org.gobiiproject.gobiiprocess.digester.machine.builder;

import lombok.Data;

import java.util.List;

@Data
public class Prototype {

	private List<SideEffectSchema> sideEffects;

	private ValidationSchema validation;

	private FailureSchema failure;
}
