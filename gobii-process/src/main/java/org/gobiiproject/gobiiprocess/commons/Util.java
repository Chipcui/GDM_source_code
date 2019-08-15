package org.gobiiproject.gobiiprocess.commons;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Util {

	public static String throwableToStackTraceString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
}
