package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import java.io.File;

/**
 * Stores extra, static arguments a transform might, but usually doesn't, need from the system.
 */
public class TransformArguments {
	/*Location of the marker file*/
	public File markerFile;
	/*Destination file location. Used in matrix transposition*/
	public String destinationFile;
	/*Base script directory - loader for loader, extractor for extract*/
	public String scriptDir;
}
