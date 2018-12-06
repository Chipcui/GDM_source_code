package org.gobiiproject.gobiimodel.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * A simple main method and front end for reading ConfigSettings from the same directory as the base.
 * @author jdl232
 */

public class ConfigSettingsReader {
	static String configLocation="../config/gobii-web.xml";//TODO - make configurable
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
			if(m.getName().toLowerCase().contains(val.toLowerCase())&& (m.getParameterCount()==0)){
				Object out = m.invoke(cs);
				String text;
				if(out instanceof List){
					text= Arrays.deepToString(((List)out).toArray());
				}
				else{
					text=out.toString();
				}
				System.out.println(text);
				System.exit(0);
			}
		}
		System.err.println("Unable to find command with name " + args[0]);
		System.exit(-1);
	}
}
