package org.gobiiproject.gobiiprocess.digester.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestFileValidator;
import org.gobiiproject.gobiiprocess.digester.utils.validation.MaximumErrorsValidationException;
import org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationWebServicesUtil;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.ValidationError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ValidationWebServicesUtil.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
public class DatasetDnarunValidationTest {

    private static String tempFolderLocation;

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        tempFolderLocation = tempFolder.getRoot().getPath();
        File source = new File("src/test/resources/validation/dataset_dnarun");
        FileUtils.copyDirectory(source, tempFolder.getRoot());
    }

    /**
     * According to JUnit no exception is thrown when temp folder is not
     * deleted. This method ensures that temp folder is always deleted.
     */
    @AfterClass
    public static void checkAndCleanTempFile() {
        try {
            FileUtils.deleteDirectory(new File(tempFolderLocation));
        } catch (IOException e) {
        }
    }

    /**
     * Dataset validation.
     */
    @Test
    public void datasetDnarunAllPassTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/allPass", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);

        List<NameIdDTO> dnarunNameResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_1");
            nameIdDTOResponse.setId(262);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_2");
            nameIdDTOResponse.setId(226);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_3");
            nameIdDTOResponse.setId(271);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        Map<String, String> experimentForeignKeyReturn = new HashMap<>();
        experimentForeignKeyReturn.put("1", "validationTestExp1");
        try {
            PowerMockito
                    .when(ValidationWebServicesUtil.getAllowedForeignKeyList(eq("dnarun"), any()))
                    .thenReturn(experimentForeignKeyReturn);

            PowerMockito
                    .when(ValidationWebServicesUtil.getNamesByNameList(Matchers.any(), eq(GobiiEntityNameType.DNARUN.toString()), eq("1"), any()))
                    .thenReturn(dnarunNameResponse);
        } catch (MaximumErrorsValidationException e) {
            e.printStackTrace();
        }
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/allPass"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_dnarun", "dataset_dnarun", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not success", ValidationTestSuite.SUCCESS_TEXT, fileErrors[0].status);
    }

    /**
     * Dataset_dnarun validation.
     * Has all required fields, two undefined dnarun_name
     */
    @Test
    public void datasetDnarunDnarunNameTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/dnarunName/missingNames",
                tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);
        List<NameIdDTO> dnarunNameResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_1");
            nameIdDTOResponse.setId(262);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_2");
            nameIdDTOResponse.setId(226);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_3");
            nameIdDTOResponse.setId(271);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_doma_9");
            nameIdDTOResponse.setId(0);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_doma_10");
            nameIdDTOResponse.setId(0);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        Map<String, String> experimentForeignKeyReturn = new HashMap<>();
        experimentForeignKeyReturn.put("1", "validationTestExp1");
        try {
            PowerMockito
                    .when(ValidationWebServicesUtil.getAllowedForeignKeyList(eq("dnarun"), any()))
                    .thenReturn(experimentForeignKeyReturn);
            PowerMockito
                    .when(ValidationWebServicesUtil.getNamesByNameList(Matchers.any(), eq("dnarun"), eq("1"), any()))
                    .thenReturn(dnarunNameResponse);
        } catch (MaximumErrorsValidationException e) {
            e.printStackTrace();
        }
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/dnarunName/missingNames"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_dnarun", "dataset_dnarun", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not success", ValidationTestSuite.FAILURE_TEXT, fileErrors[0].status);

        List<Failure> failures = fileErrors[0].failures;
        assertEquals("Failures are more than the expected", 1, failures.size());
        assertEquals("Unexpected failure reason", "dnarun_name does not exist in DB", failures.get(0).reason);
        assertEquals("Unexpected column name", "dnarun_name", failures.get(0).columnName.get(0));
        assertEquals("Unexpected dnarun_name", "dnarunname_doma_9", failures.get(0).values.get(0));
        assertEquals("Unexpected dnarun_name", "dnarunname_doma_10", failures.get(0).values.get(1));
    }

    /**
     * Dataset_dnarun validation.
     * Has all required fields, two undefined dnarun_name
     */
    @Test
    public void datasetDnarunSkipnarunNameVerificationTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/dnarunName/skipdnaNameVerification",
                tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);
        List<NameIdDTO> dnarunNameResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_1");
            nameIdDTOResponse.setId(262);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_2");
            nameIdDTOResponse.setId(226);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_3");
            nameIdDTOResponse.setId(271);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_doma_9");
            nameIdDTOResponse.setId(0);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_doma_10");
            nameIdDTOResponse.setId(0);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        Map<String, String> experimentForeignKeyReturn = new HashMap<>();
        experimentForeignKeyReturn.put("1", "validationTestExp1");
        try {
            PowerMockito
                    .when(ValidationWebServicesUtil.getAllowedForeignKeyList(eq("dnarun"), any()))
                    .thenReturn(experimentForeignKeyReturn);
            PowerMockito
                    .when(ValidationWebServicesUtil.getNamesByNameList(Matchers.any(), eq("dnarun"), eq("1"), any()))
                    .thenReturn(dnarunNameResponse);
        } catch (MaximumErrorsValidationException e) {
            e.printStackTrace();
        }
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/dnarunName/skipdnaNameVerification"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_dnarun", "dataset_dnarun", fileErrors[0].fileName);
        assertEquals("Expected STATUS is success", ValidationTestSuite.SUCCESS_TEXT, fileErrors[0].status);
    }

    /**
     * Dataset_dnarun validation.
     * Missing one required field
     */
    @Test
    public void datasetDnarunMissingRequiredFieldTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/missingRequiredColumns", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);
        List<NameIdDTO> dnarunNameResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_1");
            nameIdDTOResponse.setId(262);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_2");
            nameIdDTOResponse.setId(226);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_3");
            nameIdDTOResponse.setId(271);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        Map<String, String> experimentForeignKeyReturn = new HashMap<>();
        experimentForeignKeyReturn.put("1", "validationTestExp1");
        try {
            PowerMockito
                    .when(ValidationWebServicesUtil.getAllowedForeignKeyList(eq("dnarun"), any()))
                    .thenReturn(experimentForeignKeyReturn);
            PowerMockito
                    .when(ValidationWebServicesUtil.getNamesByNameList(Matchers.any(), eq("dnarun"), eq("1"), any()))
                    .thenReturn(dnarunNameResponse);
        } catch (MaximumErrorsValidationException e) {
            e.printStackTrace();
        }

        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/missingRequiredColumns"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_dnarun", "dataset_dnarun", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not FAILURE", ValidationTestSuite.FAILURE_TEXT, fileErrors[0].status);

        List<Failure> failures = fileErrors[0].failures;
        assertEquals("Failures are more than the expected", 1, failures.size());
        assertEquals("Unexpected failure reason", "Column not found", failures.get(0).reason);
        assertEquals("Unexpected column name", "dnarun_idx", failures.get(0).columnName.get(0));
    }

    /**
     * Marker validation.
     * Missing values in required field
     */
    @Test
    public void datasetDnarunMissingValuesInRequiredFieldTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/missingValuesInRequiredColumns", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);

        List<NameIdDTO> dnarunNameResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_1");
            nameIdDTOResponse.setId(262);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_2");
            nameIdDTOResponse.setId(226);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dnarunname_dom_3");
            nameIdDTOResponse.setId(271);
            dnarunNameResponse.add(nameIdDTOResponse);
        }
        Map<String, String> experimentForeignKeyReturn = new HashMap<>();
        experimentForeignKeyReturn.put("1", "validationTestExp1");
        try {
            PowerMockito
                    .when(ValidationWebServicesUtil.getAllowedForeignKeyList(eq("dnarun"), any()))
                    .thenReturn(experimentForeignKeyReturn);
            PowerMockito
                    .when(ValidationWebServicesUtil.getNamesByNameList(Matchers.any(), eq("dnarun"), eq("1"), any()))
                    .thenReturn(dnarunNameResponse);
        } catch (MaximumErrorsValidationException e) {
            e.printStackTrace();
        }
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/missingValuesInRequiredColumns"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_dnarun", "dataset_dnarun", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not FAILURE", ValidationTestSuite.FAILURE_TEXT, fileErrors[0].status);

        List<Failure> failures = fileErrors[0].failures;
        assertEquals("Failures are more than the expected", 1, failures.size());
        assertEquals("Unexpected failure reason", "NULL VALUE", failures.get(0).reason);
        assertEquals("Unexpected column name", "dataset_id", failures.get(0).columnName.get(0));
    }
}