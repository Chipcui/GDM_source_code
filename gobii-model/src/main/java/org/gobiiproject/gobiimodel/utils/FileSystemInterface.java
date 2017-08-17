package org.gobiiproject.gobiimodel.utils;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;

public class FileSystemInterface {

	private static boolean keepAllFiles=false;
	/**
	 * As unix RM command.
	 * @param file
	 */
	public static void rm(String file){
		if(keepAllFiles){
			return;
		}
		HelperFunctions.tryExec("rm "+file);
	}

	public static void rmIfExist(File file){
		if(file==null)return;
		if(keepAllFiles){
			return;
		}
		if(file.exists()){
			file.delete();
		}
	}
	public static void rmIfExist(String file){
		if(file==null)return;
		File f=new File(file);
		rmIfExist(f);
	}
	/**
	 * As unix MV command.
	 * @param from
	 * @param to
	 */
	public static void mv(String from, String to){
		HelperFunctions.tryExec("mv "+from + " " + to);
	}

	public static int lineCount(String file){
		if(!new File(file).exists()){
			ErrorLogger.logDebug("FileSystemInterface","File " + file + " being WC'd does not exist");
			return 0;
		}
		String [] exec={"wc","-l",file};
		int retVal=-1;
		try {
			ProcessBuilder builder = new ProcessBuilder(exec);
			Process p = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));//What terrible person makes 'InputStream' the type of the output of a process
			p.waitFor();
			retVal=Integer.parseInt(reader.readLine().split(" ")[0]);

		}
		catch(Exception e){
			ErrorLogger.logError("FileSystemInterface","Unable to call wc",e);
		}
		return retVal;
	}

	/**
	 * Determines whether or not to keep temporary files. Useful for debuggins
	 * @param keep
	 */
	public static void keepAllFiles(boolean keep){
		keepAllFiles=keep;
	}

	/**
	 *Returns a filtered list of files under a directory where the files match an extension
	 * @param baseDir starting directory for datatypes, does not traverse filders
	 * @return List of files
	 */
	public static List<File> getAllFiles(File baseDir, String extension) {
		File[] list = baseDir.listFiles();
		List<File> ret = new ArrayList<File>();
		if (list == null) return null;
		for (File f : list)
			if (f.isFile()) { //Improvised off Java's FileExtensionFilter.
				String name = f.getName();
				int i = name.lastIndexOf('.');
				if (i < 0) continue;
				if (i > name.length() - 1) continue;
				if (!name.substring(i + 1).equals(extension)) continue;
				ret.add(f);
			}
		return ret;
	}
}
