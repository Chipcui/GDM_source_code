package org.gobiiproject.gobiiclient.gobii.Helpers;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ADLEncapsulator {
    private static final String INPUT_HOST = "-h";
    private static final String INPUT_USER = "-u";
    private static final String INPUT_PASSWORD = "-p";
    private static final String INPUT_TIMEOUT = "-t";
    private static final String INPUT_SCENARIO = "-s";
    private static final String INPUT_DIRECTORY = "-d";
    private static final String SPACE = " ";
    private static final String JAVA_COMMAND = "java -jar";
    private static final String OUTPUT_FILE_NAME = "output.txt";
    private static final String ERROR_FILE_NAME = "error.txt";

    private String inputHost, inputUser, inputPassword, inputScenario, inputDirectory, adlJarPath, errorMsg;
    private Integer inputTimeout;

    private String getInputHost() {
        return inputHost;
    }

    public void setInputHost(String inputHost) {
        this.inputHost = inputHost;
    }

    private String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    private String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    private Integer getInputTimeout() {
        return inputTimeout;
    }

    public void setInputTimeout(Integer inputTimeout) {
        this.inputTimeout = inputTimeout;
    }

    private String getInputScenario() {
        return inputScenario;
    }

    public void setInputScenario(String inputScenario) {
        this.inputScenario = inputScenario;
    }

    private String getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    private String getAdlJarPath() {
        return adlJarPath;
    }

    public void setAdlJarPath(String adlJarPath) {
        this.adlJarPath = adlJarPath;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    private void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private String getBasicCommand() {
        String servercommand = JAVA_COMMAND + SPACE;
        servercommand += getAdlJarPath() + SPACE;
        servercommand += INPUT_HOST + SPACE + getInputHost() + SPACE;
        servercommand += INPUT_USER + SPACE + getInputUser() + SPACE;
        servercommand += INPUT_PASSWORD + SPACE + getInputPassword() + SPACE;
        servercommand += INPUT_TIMEOUT + SPACE + getInputTimeout() + SPACE;
        return servercommand;
    }

    private String getBatchAdlCommand() {
        String servercommand = getBasicCommand();
        servercommand += INPUT_DIRECTORY + SPACE + getInputDirectory() + SPACE;
        return servercommand;
    }

    private String getScenarioAdlCommand() {
        String servercommand = getBasicCommand();
        servercommand += INPUT_SCENARIO + SPACE + getInputScenario() + SPACE;
        return servercommand;
    }

    public boolean executeBatchGobiiADL() throws Exception{

        if (getAdlJarPath() == null || getInputHost() == null || getInputUser() == null || getInputPassword() == null || getInputTimeout() == null || getInputDirectory() == null) {
            setErrorMsg("Please set all the input parameters and try again.");
        }

        String outputFileName = getInputDirectory() + "/" + OUTPUT_FILE_NAME;
        String errorFileName = getInputDirectory() + "/" + ERROR_FILE_NAME;

        boolean returnVal = HelperFunctions.tryExec(getBatchAdlCommand(),
                outputFileName,
                errorFileName);

        if (!returnVal) {

            String message = "gobiiadl failed but there was no output error file reported";
            File errorFile = new File(errorFileName);
            if( errorFile.exists()) {
                message = FileUtils.readFileToString(errorFile);
            } else {
                File outputFile = new File(getClass().getClassLoader().getResource(outputFileName).getFile());
                if( outputFile.exists()) {
                    message = FileUtils.readFileToString(errorFile);
                }
            }

            setErrorMsg(message);

//            final String[] msg = new String[0];
//            try (Stream<String> stream = Files.lines(Paths.get(getInputDirectory() + ERROR_FILE_NAME))) {
//                stream.forEach(s -> msg[0] += s + SPACE);
//            } catch (IOException e) {
//                setErrorMsg(e.getMessage());
//            }
//
//            setErrorMsg(msg[0]);
        }

        return returnVal;

    } // func()


    public void copyFilesToLocalDir(File sourceDir, File destinationDir) throws Exception {

        if (sourceDir.exists() && sourceDir.isDirectory() && sourceDir.list().length > 0) {

            for (File currentFile : sourceDir.listFiles()) {

                if (currentFile.isDirectory()) {

                    if (!new File(destinationDir.getAbsoluteFile() + "/" + currentFile.getName()).exists()) {
                        File newDir = new File(destinationDir.getAbsoluteFile() + "/" + currentFile.getName());
                        newDir.mkdir();

                        copyFilesToLocalDir(currentFile, newDir);
                    }

                } else {

                    FileUtils.copyFile(currentFile, new File(destinationDir.getAbsoluteFile() + "/" + currentFile.getName()));
                }
            }

        }


    }
}
