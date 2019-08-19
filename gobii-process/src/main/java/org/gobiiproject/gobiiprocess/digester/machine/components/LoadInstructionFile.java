package org.gobiiproject.gobiiprocess.digester.machine.components;


import org.gobiiproject.gobiiprocess.digester.machine.model.DigesterState;
import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.components.Transition;

@Component("loadInstuctionFile")
public class LoadInstructionFile implements Transition<DigesterState> {

	@Override
	public void accept(DigesterState digesterState) {
		
	}
}
