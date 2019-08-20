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

	private List<Debug> debug = new LinkedList<>();
	private List<Info> info = new LinkedList<>();
	private List<Warning> warning = new LinkedList<>();
	private List<Error> error = new LinkedList<>();

}
