package org.gobiiproject.gobiidao.gql;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;


public class GqlWrapper {

    private static Logger LOGGER = LoggerFactory.getLogger(GqlWrapper.class);

    public static Integer GQL_WRAPPER_TIMEOUT = -1;
    public static Integer GQL_RETURN_SUCCESS = 0;
    public static Integer GQL_RETURN_INCOMPLETE_PARAMETERS = 1;
    public static Integer GQL_RETURN_ERROR_PARSING_PARAMETERS = 2;
    public static Integer GQL_RETURN_INVALID_OPTIONS = 3;
    public static Integer GQL_RETURN_ERROR_PARSING_JSON = 4;
    public static Integer GQL_RETURN_NOT_ENTRY_VERTEX = 5;
    public static Integer GQL_RETURN_NO_OUTPUT_PATH = 6;
    public static Integer GQL_RETURN_OUTPUT_FILE_CREATION_FAILED = 7;
    public static Integer GQL_RETURN_FEATURE_NOT_IMPLEMENTED = 8;
    public static Integer GQL_RETURN_NO_PATH_FOUND = 9;
    public static Integer GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET = 10;
    /*
    * 	SUCCESS: "Operation completed successfully.",
		INCOMPLETE_PARAMETERS: "There were fewer parameters passed than what is required. Please check the usage help (-h).",
		ERROR_PARSING_PARAMETERS: "The parameters given cannot be parsed. Please check your syntax.",
		INVALID_OPTIONS: "A given option/flag is invalid. Please check.",
		ERROR_PARSING_JSON: "An error occured while parsing a json parameter. Make sure it is of the proper format.",
		NOT_ENTRY_VERTEX: "A non-entry vertex was supplied without a sub-graph.",
		NO_OUTPUT_PATH: "No output file path was given.",
		OUTPUT_FILE_CREATION_FAILED: "Creating the output file failed.",
		FEATURE_NOT_IMPLEMENTED: "This feature is not implemented for this version of GQL.",
		NO_PATH_FOUND: "No path can be derived between the two vertices given. Both direct descendants and common relative algorithms have been exhausted.",
		NO_FILTERS_APPLIED_TO_TARGET: "The filters selected did not reduce a goal-vertex. Aborting to avoid a potentially huge query."
    * */
    private static Integer timeOutSecs = 10;

    private static String message;

    public static String message() {
        return message;
    }

    public static Integer run(String execString, String outputFileName, String errorFileName) throws Exception {

        Integer returnVal = 0;

        String[] execArray = HelperFunctions.makeExecString(execString);
        Process process = HelperFunctions.initProcecess(execArray, outputFileName, errorFileName, null, timeOutSecs);

        if (!process.isAlive()) {
            returnVal = process.exitValue();

            // GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET is not considered a failure, but needs special handling by caller
            if (returnVal != 0 &&
                    !returnVal.equals(GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET)) {

                String errorText = "";
                BufferedReader br = new BufferedReader(new FileReader(errorFileName));
                while (br.ready()) {
                    errorText += br.readLine() + "\n";
                }
                message = "Error executing gql query " + execString + ": " + errorText;
                LOGGER.error(message);
                //throw new GobiiDaoException(message);
            }

        } else {
            returnVal = GQL_WRAPPER_TIMEOUT;
            String errrorText = "The shell process for commandline "
                    + execString
                    + " exceeded the maximum wait time of "
                    + timeOutSecs
                    + " seconds";

            if (process.isAlive()) {
                process.destroy();
                process.waitFor(timeOutSecs, TimeUnit.SECONDS);
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
            }

            LOGGER.error(errrorText);
            message = errrorText;

        }

        return returnVal;

    } // end method run()

} // end class
