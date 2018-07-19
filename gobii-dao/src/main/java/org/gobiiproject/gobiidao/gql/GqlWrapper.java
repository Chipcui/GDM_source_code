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
    public static void run(String execString, String outputFileName, String errorFileName) throws Exception {


        Integer timeOutSecs = 10;

        String[] execArray = HelperFunctions.makeExecString(execString);
        Process process = HelperFunctions.initProcecess(execArray, outputFileName, errorFileName, null, timeOutSecs);

        if( !process.isAlive()) {
            Integer exitValue = process.exitValue();

            if (exitValue != 0) {
                String errorText = "";
                BufferedReader br = new BufferedReader(new FileReader(errorFileName));
                while (br.ready()) {
                    errorText += br.readLine() + "\n";
                }
                String message = "Error executing gql query " + execString + ": " + errorText;
                LOGGER.error(message);
                throw new GobiiDaoException(message);
            }

        } else {
            String message = "The shell process for commandline "
                    +  execString
                    + " exceeded the maximum wait time of "
                    + timeOutSecs
                    + " seconds";
            LOGGER.error(message);

            if( process.isAlive()) {
                process.destroy();
                process.waitFor(timeOutSecs, TimeUnit.SECONDS);
                if( process.isAlive()) {
                    process.destroyForcibly();
                }
            }


            throw new GobiiDaoException(message);


        }

    } // end method run()

} // end class
