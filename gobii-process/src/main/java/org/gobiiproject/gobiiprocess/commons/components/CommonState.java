package org.gobiiproject.gobiiprocess.commons.components;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CommonState {

	private List<String> debugLog = new LinkedList<>();
	private List<String> infoLog = new LinkedList<>();
	private List<String> warningLog = new LinkedList<>();
	private List<String> errorLog = new LinkedList<>();
}
