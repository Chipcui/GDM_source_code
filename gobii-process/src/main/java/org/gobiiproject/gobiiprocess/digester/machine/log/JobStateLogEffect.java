package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.DigesterState;
import org.gobiiproject.gobiiprocess.machine.Component;

@Component("Job State Log")
public class JobStateLogEffect extends LogEffect {

	@Override
	public DigesterState react(DigesterState s0, DigesterState s1) {

		info(String.format("JobState moving from %s to %s", s0.getJobStatus().toString(), s0.getJobStatus().toString()));

		return s1;
	}
}
