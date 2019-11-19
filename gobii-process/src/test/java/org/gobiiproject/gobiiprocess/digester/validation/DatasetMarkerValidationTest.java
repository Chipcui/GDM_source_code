package org.gobiiproject.gobiiprocess.digester.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
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
public class DatasetMarkerValidationTest {

    private static String tempFolderLocation;

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        tempFolderLocation = tempFolder.getRoot().getPath();
        File source = new File("src/test/resources/validation/dataset_marker");
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
     * datasetMarkerGroup validation.
     */
    @Test
    public void datasetMarkerAllPassTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/allPass", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);
        Map<String, String> foreignKeyValueFromDBPlatformId = new HashMap<>();
        foreignKeyValueFromDBPlatformId.put("8", "Dart_clone");

        List<NameIdDTO> referenceResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker1");
            nameIdDTOResponse.setId(1);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker2");
            nameIdDTOResponse.setId(2);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker3");
            nameIdDTOResponse.setId(3);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker4");
            nameIdDTOResponse.setId(4);
            referenceResponse.add(nameIdDTOResponse);
        }

        mockForTestCase(foreignKeyValueFromDBPlatformId, referenceResponse);
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/allPass"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_marker", "dataset_marker", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not success", ValidationTestSuite.SUCCESS_TEXT, fileErrors[0].status);
    }

    /**
     * datasetMarkerGroup validation.
     */
    @Test
    public void missingPlatformIdAllPassTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/missingPlatformId", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");
        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);
        Map<String, String> foreignKeyValueFromDBPlatformId = new HashMap<>();
        foreignKeyValueFromDBPlatformId.put("81", "Dart_clone");

        List<NameIdDTO> referenceResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker1");
            nameIdDTOResponse.setId(1);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker2");
            nameIdDTOResponse.setId(2);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker3");
            nameIdDTOResponse.setId(3);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker4");
            nameIdDTOResponse.setId(4);
            referenceResponse.add(nameIdDTOResponse);
        }

        mockForTestCase(foreignKeyValueFromDBPlatformId, referenceResponse);
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/missingPlatformId"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_marker", "dataset_marker", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not success", ValidationTestSuite.FAILURE_TEXT, fileErrors[0].status);
        List<Failure> failures = fileErrors[0].failures;
        assertEquals("Failures are more than the expected", 1, failures.size());
        assertEquals("Unexpected column name", "platform_id", failures.get(0).columnName.get(0));
        assertEquals("Unexpected failure reason", "Undefined value in DB", failures.get(0).reason);
        assertEquals("Unexpected failure", "81", failures.get(0).values.get(0));
    }

    /**
     * datasetMarkerGroup validation.
     * Missing one required field
     */
    @Test
    public void datasetMarkerMissingRequiredFieldTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/missingRequiredColumns", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);
        Map<String, String> foreignKeyValueFromDBPlatformId = new HashMap<>();
        foreignKeyValueFromDBPlatformId.put("8", "Dart_clone");

        List<NameIdDTO> referenceResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker1");
            nameIdDTOResponse.setId(1);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker2");
            nameIdDTOResponse.setId(2);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker3");
            nameIdDTOResponse.setId(3);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker4");
            nameIdDTOResponse.setId(4);
            referenceResponse.add(nameIdDTOResponse);
        }

        mockForTestCase(foreignKeyValueFromDBPlatformId, referenceResponse);
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/missingRequiredColumns"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_marker", "dataset_marker", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not FAILURE", ValidationTestSuite.FAILURE_TEXT, fileErrors[0].status);

        List<Failure> failures = fileErrors[0].failures;
        assertEquals("Failures are more than the expected", 1, failures.size());
        assertEquals("Unexpected failure reason", "Column not found", failures.get(0).reason);
        assertEquals("Unexpected column name", "marker_idx", failures.get(0).columnName.get(0));
    }

    /**
     * datasetMarkerGroup validation.
     */
    @Test
    public void markerNameFailTest() throws IOException {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/markerNameFailTest", tempFolder.getRoot().getAbsolutePath() + "/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer(eq("http://192.168.56.101:8081/gobii-dev/"), eq("mcs397"), eq("q"), eq(null), any()))
                .thenReturn(true);

        Map<String, String> foreignKeyValueFromDBPlatformId = new HashMap<>();
        foreignKeyValueFromDBPlatformId.put("8", "Dart_clone");

        List<NameIdDTO> referenceResponse = new ArrayList<>();
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker1");
            nameIdDTOResponse.setId(1);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker2");
            nameIdDTOResponse.setId(2);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarker3");
            nameIdDTOResponse.setId(3);
            referenceResponse.add(nameIdDTOResponse);
        }
        {
            NameIdDTO nameIdDTOResponse = new NameIdDTO();
            nameIdDTOResponse.setName("dommarkerss4");
            nameIdDTOResponse.setId(0);
            referenceResponse.add(nameIdDTOResponse);
        }

        mockForTestCase(foreignKeyValueFromDBPlatformId, referenceResponse);
        digestFileValidator.performValidation();
        List<Path> pathList =
                Files.list(Paths.get(tempFolder.getRoot().getAbsolutePath() + "/markerNameFailTest"))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        assertEquals("There should be one validation output json file", 1, pathList.size());

        ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
        assertEquals("Expected file name is not dataset_marker", "dataset_marker", fileErrors[0].fileName);
        assertEquals("Expected STATUS is not success", ValidationTestSuite.FAILURE_TEXT, fileErrors[0].status);

        List<Failure> failures = fileErrors[0].failures;
        assertEquals("Failures are more than the expected", 1, failures.size());

        assertEquals("Unexpected failure reason", "marker does not exist in DB", failures.get(0).reason);
        assertEquals("Unexpected column name", "marker_name", failures.get(0).columnName.get(0));
        assertEquals("Unexpected column value", "dommarkerss4", failures.get(0).values.get(0));
    }

    private void mockForTestCase(Map<String, String> foreignKeyValueFromDBPlatformId, List<NameIdDTO> referenceResponse) {
        try {
            PowerMockito
                    .when(ValidationWebServicesUtil.validatePlatformId(eq("8"), any())).thenReturn(foreignKeyValueFromDBPlatformId);
            PowerMockito
                    .when(ValidationWebServicesUtil.getNamesByNameList(Matchers.any(), eq("marker"), eq("8"), any()))
                    .thenReturn(referenceResponse);
        } catch (MaximumErrorsValidationException e) {
            e.printStackTrace();
        }
    }
}




