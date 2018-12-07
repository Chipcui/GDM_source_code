// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Chaitanya Sarma
// Create Date:   2018-05-18
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.instructions;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.gobii.Helpers.ADLEncapsulator;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestNameIdListTest;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public class GobiiAdlTest {


    private static boolean backendSupoorted;
    private static TestExecConfig testExecConfig;
    private static Logger LOGGER = LoggerFactory.getLogger(DtoCrudRequestNameIdListTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
        backendSupoorted = TestUtils.isBackEndSupported();
        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        testExecConfig = gobiiTestConfiguration.getConfigSettings().getTestExecConfig();
    }


    /***
     * Note: there are a couple of issues with this test. First of all, checking
     * the backend processing flag does not work: locally, mine is set to false and the test
     * runs anyway.
     * Moreover, when it does run, it times out. However, the error message does not display,
     * and instead there is an index out of bounds exception.
     */
    @Test
    public void testADLBatchProcessing() throws Exception {

        if (backendSupoorted) {

            ADLEncapsulator adlEncapsulator = new ADLEncapsulator();
            String configUtilCommandlineStem = testExecConfig.getConfigUtilCommandlineStem();

            // EXAMPLE java -jar E:\gobii\1623-03\gobiiproject\gobii-process\target
            String[] split = configUtilCommandlineStem.split(" ");
            adlEncapsulator.setAdlJarPath(split[2] + "/gobiiadl.jar");
            adlEncapsulator.setInputHost(testExecConfig.getInitialConfigUrl());
            adlEncapsulator.setInputUser(testExecConfig.getLdapUserForUnitTest());
            adlEncapsulator.setInputPassword(testExecConfig.getLdapPasswordForUnitTest());
            adlEncapsulator.setInputTimeout(testExecConfig.getAsynchOpTimeoutMinutes());

            // copy to temp folder
            String tempDirName = "adlTest-" + UUID.randomUUID().toString();
            String tempDirString = testExecConfig.getTestFileDownloadDirectory() + "/" + tempDirName;
            File tempDir = new File(tempDirString);

            tempDir.mkdir();

            // check if include_scenarios.txt exists
            File scenarioFile = new File("src/test/resources/gobiiAdl/include_scenarios.txt");
            File fileFromRepo = new File("src/test/resources/gobiiAdl");

            adlEncapsulator.copyFilesToLocalDir(fileFromRepo, tempDir);

            Scanner sc = new Scanner(scenarioFile);

            while (sc.hasNextLine()) {

                String scenarioName = sc.nextLine();

                File newScenarioDir = new File(tempDir.getAbsoluteFile() + "/" + scenarioName);
                newScenarioDir.mkdir();

                adlEncapsulator.copyFilesToLocalDir(fileFromRepo, newScenarioDir);

                System.out.print(scenarioName);
            }

            adlEncapsulator.copyFilesToLocalDir(fileFromRepo, tempDir);


            adlEncapsulator.setInputDirectory(tempDir.getAbsolutePath());

        } else {
            LOGGER.error("Backend support is not provided in this context: system-critical unit tests will not be run");
        }
    }
}
