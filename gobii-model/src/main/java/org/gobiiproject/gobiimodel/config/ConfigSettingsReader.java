package org.gobiiproject.gobiimodel.config;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A simple main method and front end for reading ConfigSettings from the same directory as the base.
 * @author jdl232
 */

public class ConfigSettingsReader {
	static String configLocation="gobii-web.xml";
	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
		if(args.length == 0 || args.length > 1) {
			System.out.println("Usage: ConfigSettingReader.jar [configuration name]");
			System.out.println("\t returns value of the configuration method call, assuming no arguments");
			System.out.println("\t Special Usage: ConfigSettingReader.jar list - lists all accessible methods");
			System.exit( -1);
		}
		ConfigSettings cs = new ConfigSettings(configLocation);
		String val = args[0];
		Class configClass = ConfigSettings.class;

		if(args[0].toLowerCase().equals("list")){
			for(Method m: configClass.getMethods()){
				if(m.getParameterCount()==0)
					System.out.println(m.getName());
			}
			System.exit(0);
		}
		for(Method m: configClass.getMethods()){
			if(m.getName().toLowerCase().contains(args[0].toLowerCase())&& (m.getParameterCount()==0)){
				String out = m.invoke(cs).toString();
				System.out.println(out);
				System.exit(0);
			}
		}
		System.err.println("Unable to find command with name " + args[0]);
	}
}
