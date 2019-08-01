package org.gobiiproject.gobiiprocess.digester.machine.log;

import org.gobiiproject.gobiiprocess.digester.machine.DigesterState;
import org.gobiiproject.gobiiprocess.machine.components.Component;

@Component("log/jobState")
public class JobStateLogEffect extends LogEffect {

	@Override
	public void react(DigesterState s0, DigesterState s1) {
		if (s0.getJobStatus() != s1.getJobStatus()) {
			info(String.format("JobState moving from %s to %s", s0.getJobStatus().toString(), s1.getJobStatus().toString()));
		}
	}
}
