package org.gobiiproject.gobiiprocess.commons.model;

import lombok.Data;
import org.gobiiproject.gobiiprocess.commons.model.log.Debug;
import org.gobiiproject.gobiiprocess.commons.model.log.Info;
import org.gobiiproject.gobiiprocess.commons.model.log.Warning;
import org.gobiiproject.gobiiprocess.commons.model.log.Error;

import java.util.LinkedList;
import java.util.List;

@Data
public class CommonState {

	private List<Debug> debugLog = new LinkedList<>();
	private List<Info> infoLog = new LinkedList<>();
	private List<Warning> warningLog = new LinkedList<>();
	private List<Error> errorLog = new LinkedList<>();

}
