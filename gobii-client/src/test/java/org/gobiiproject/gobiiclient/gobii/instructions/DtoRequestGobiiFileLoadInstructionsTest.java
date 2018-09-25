// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.instructions;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestContactTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestDataSetTest;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProtocolDTO;
import org.gobiiproject.gobiimodel.types.*;

import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.*;


public class DtoRequestGobiiFileLoadInstructionsTest {

    private static GobiiUriFactory gobiiUriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    public void testSendLoaderInstructionFile() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();


        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = GobiiClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);


        String digesterOutputDirectory =
                GobiiClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String instructionOneTableName = "foo_table";
        Integer instructionOneDataSetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>()).getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET);
        Integer instructionOneContactId = (new GlobalPkColl<DtoCrudRequestContactTest>()).getAPkVal(DtoCrudRequestContactTest.class, GobiiEntityNameType.CONTACT);
        String gobiiCropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();
        gobiiLoaderInstructionOne.setTable(instructionOneTableName);
        gobiiLoaderInstructionOne.setDataSetId(instructionOneDataSetId);
        gobiiLoaderInstructionOne.setContactId(instructionOneContactId);
        gobiiLoaderInstructionOne.setGobiiCropType(gobiiCropType);
        gobiiLoaderInstructionOne.setTable("matrix"); // need this for job record creation test


        // column one
        String instructionOneColumnOneName = "my_foo_column";
        DataSetType dataSetTypeTableOneColumnOne = DataSetType.IUPAC;
        String findTextTableOneColumnOne = "foo-replace-me";
        String replaceTextTextTableOneColumnOne = "bar-replace-me";
        GobiiFileColumn gobiiFileColumnOne = new GobiiFileColumn()
                .setCCoord(1)
                .setRCoord((1))
                .setGobiiColumnType(GobiiColumnType.VCF_MARKER)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName(instructionOneColumnOneName)
                .setFindText(findTextTableOneColumnOne)
                .setReplaceText(replaceTextTextTableOneColumnOne)
                .setDataSetType(dataSetTypeTableOneColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        // column two
        GobiiFileColumn gobiiFileColumnTwo = new GobiiFileColumn()
                .setCCoord(1)
                .setRCoord(1)
                .setGobiiColumnType(GobiiColumnType.CSV_COLUMN)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName("my_bar");

        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnOne);
        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnTwo);

        // GobiiFile Config
        String testSourceDirName = digesterInputDirectory + "file_one_dir";
        String testDestinationDirName = digesterOutputDirectory + "file_one_dir";
        gobiiLoaderInstructionOne.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dir")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.VCF)
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName);

        // VCF Parameters
        gobiiLoaderInstructionOne.getVcfParameters()
                .setMaf(1.1f)
                .setMinDp(1.1f)
                .setMinQ(1.1f)
                .setRemoveIndels(true)
                .setToIupac(true);

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionOne);


        // INSTRUCTION ONE END
        // **********************************************************************

        /*** InstructionTwo START - Test for wrong projectId for given experiment. Valid experimentId will be used here ***/
        //get existing dataset
        Integer dataSetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET));

        RestUri datasetsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        datasetsUri.setParamValue("id", dataSetId.toString());

        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(datasetsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceDataSet.get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelopeDataSet.getPayload().getData().get(0);

        Integer experimentId = dataSetDTOResponse.getExperimentId();


        GobiiLoaderInstruction gobiiLoaderInstructionTwo = new GobiiLoaderInstruction();

        String instructionTwoTableName = "test_table2";

        //get experimentDTO
        RestUri experimentsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_EXPERIMENTS);
        experimentsUri.setParamValue("id", experimentId.toString());

        GobiiEnvelopeRestResource<ExperimentDTO,ExperimentDTO> gobiiEnvelopeRestResourceExperiment = new GobiiEnvelopeRestResource<>(experimentsUri);
        PayloadEnvelope<ExperimentDTO> resultEnvelopeExperiment = gobiiEnvelopeRestResourceExperiment.get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeExperiment.getHeader()));
        ExperimentDTO experimentDTOResponse = resultEnvelopeExperiment.getPayload().getData().get(0);

        Integer projectId = experimentDTOResponse.getProjectId();

        // invalid projectId
        Integer invalidProjectId = projectId + 1;

        gobiiLoaderInstructionTwo.setTable(instructionTwoTableName);
        gobiiLoaderInstructionTwo.setDataSetId(dataSetId);
        gobiiLoaderInstructionTwo.getDataSet().setId(dataSetId);
        gobiiLoaderInstructionTwo.getDataSet().setName(dataSetDTOResponse.getDatasetName());
        gobiiLoaderInstructionTwo.setGobiiCropType(gobiiCropType);
        gobiiLoaderInstructionTwo.getExperiment().setId(experimentId);
        gobiiLoaderInstructionTwo.getExperiment().setName(experimentDTOResponse.getExperimentName());
        gobiiLoaderInstructionTwo.getProject().setId(invalidProjectId);

        // column one
        String instructionTwoColumnOneName = "test2_column_one";
        DataSetType dataSetTypeTwoColumnOne = DataSetType.IUPAC;
        GobiiFileColumn gobiiFileTwoColumnOne = new GobiiFileColumn()
                .setGobiiColumnType(GobiiColumnType.CONSTANT)
                .setConstantValue("test2")
                .setName(instructionTwoColumnOneName)
                .setDataSetType(dataSetTypeTwoColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileTwoColumnOne);

        // gobiifile config
        String testSourceTwoDirName = digesterInputDirectory + "file_two_dir";
        String testDestinationTwoDirName = digesterOutputDirectory + "file_two_dir";
        gobiiLoaderInstructionTwo.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dr")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.GENERIC)
                .setSource(testSourceTwoDirName)
                .setDestination(testDestinationTwoDirName);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionTwo);

        /***** InstructionTwo END *****/

        //The primary instruction does not have a payload type
        PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);


        // first one should have failed due to lack of a payload type
        Assert.assertNotEquals(null, loaderInstructionFileDTOResponseEnvelope);

        Assert.assertTrue("There should have been a missing payload type error",
                loaderInstructionFileDTOResponseEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("primary instruction does not have a payload type"))
                        .count() > 0);

        JobPayloadType payloadTypeToTest = JobPayloadType.CV_PAYLOADTYPE_MATRIX;
        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions().get(0).setJobPayloadType(payloadTypeToTest);
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        //now it should succeed
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFileDTOResponseEnvelope.getHeader()));

        LoaderInstructionFilesDTO loaderInstructionFileDTOResponse = loaderInstructionFileDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, loaderInstructionFileDTOResponse);

        //Now re-retrieve with the link we got back
        Assert.assertNotNull(loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection());
        Assert.assertNotNull(loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem());
//        Assert.assertNotNull(loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().get(0));



        Assert.assertTrue("The number of remapped tables does not match the number of tables sent",
                loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions().size() ==
                        loaderInstructionFileDTOResponse.getGobiiLoaderInstructions().get(0).getColumnsByTableName().size());
        

        //verify remaping
        for (GobiiLoaderInstruction currentLoaderInstructionSent : loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions()) {

            Assert.assertTrue("A table is missing from the table mapping: " + currentLoaderInstructionSent.getTable(),
                    loaderInstructionFileDTOResponse
                            .getGobiiLoaderInstructions().get(0)
                            .getColumnsByTableName().containsKey(currentLoaderInstructionSent.getTable()));

            Assert.assertTrue("The number of columns for the table does not match: " + currentLoaderInstructionSent.getTable(),
                    loaderInstructionFileDTOResponse
                            .getGobiiLoaderInstructions().get(0)
                            .getColumnsByTableName().get(currentLoaderInstructionSent.getTable()).size() ==
                            currentLoaderInstructionSent.getGobiiFileColumns().size());

            Assert.assertTrue("The number of columns for the table does not match: " + currentLoaderInstructionSent.getTable(),
                    loaderInstructionFileDTOResponse
                            .getGobiiLoaderInstructions().get(0)
                            .getColumnsByTableName().containsKey(currentLoaderInstructionSent.getTable()));

            Assert.assertNotNull("The job status field should not be null: " + currentLoaderInstructionSent.getGobiiJobStatus(),
                    currentLoaderInstructionSent.getGobiiJobStatus());

            Assert.assertTrue("The default reported status should not be " + JobProgressStatusType.CV_PROGRESSSTATUS_NOSTATUS.getCvName(),
                    !currentLoaderInstructionSent.getGobiiJobStatus().equals(JobProgressStatusType.CV_PROGRESSSTATUS_NOSTATUS.getCvName()));


            for (GobiiFileColumn currentFileColumnSent : currentLoaderInstructionSent.getGobiiFileColumns()) {

                Assert.assertTrue("File column "
                                + currentFileColumnSent.getName()
                                + "of table "
                                + currentLoaderInstructionSent.getTable()
                                + " does not exist in the remapped instruction",
                        loaderInstructionFileDTOResponse
                                .getGobiiLoaderInstructions().get(0)
                                .getColumnsByTableName().get(currentLoaderInstructionSent.getTable())
                                .stream()
                                .filter(gfc -> gfc.getName().equals(currentFileColumnSent.getName())).count() > 0);
            }

        }

        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE USING THE HATEOS LINKS

        LinkCollection linkCollection = loaderInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection();
        Assert.assertNotNull(linkCollection);
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == 1);
        RestUri restUriFromLink = gobiiUriFactory.RestUriFromUri(linkCollection.getLinksPerDataItem().get(0).getHref());
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(restUriFromLink);
        PayloadEnvelope<LoaderInstructionFilesDTO> resultEnvelope = gobiiEnvelopeRestResourceForGet
                .get(LoaderInstructionFilesDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieveResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull(loaderInstructionFilesDTOretrieveResponse.getGobiiLoaderInstructions());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFileDTOResponseEnvelope.getHeader()));

        Assert.assertTrue(
                2 == loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .size()
        );

        Assert.assertNotNull(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions().get(0)
        );


        Assert.assertTrue(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .get(0)
                        .getDataSetId().equals(instructionOneDataSetId)
        );


        Assert.assertTrue(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .get(0)
                        .getGobiiFileColumns()
                        .get(0)
                        .getName().equals(instructionOneColumnOneName)
        );

        // ************** VERIFY THAT A JOB RECORD WAS CREATED FOR OUR INSTRUCTION
        String jobName = loaderInstructionFilesDTOretrieveResponse.getInstructionFileName();
        RestUri restUriForJob = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_JOB)
                .addUriParam("jobName", jobName);
        GobiiEnvelopeRestResource<JobDTO,JobDTO> gobiiEnvelopeRestResourceForJob = new GobiiEnvelopeRestResource<>(restUriForJob);
        PayloadEnvelope<JobDTO> resultEnvelopeForJob = gobiiEnvelopeRestResourceForJob
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForJob.getHeader()));

        Assert.assertTrue("No job record was created for job " + jobName,
                resultEnvelopeForJob.getPayload().getData().size() == 1);

        JobDTO submittedJobDto = resultEnvelopeForJob.getPayload().getData().get(0);
        Assert.assertTrue("The job name of the retrieved DTO for the submitted job does not match the requested job name" + jobName,
                submittedJobDto.getJobName().equals(jobName));

        Assert.assertTrue("The instruction file's payload type " + payloadTypeToTest.getCvName() + "does not match the retrievedtype " + submittedJobDto.getPayloadType(),
                submittedJobDto.getPayloadType().equals(payloadTypeToTest.getCvName()));

        Assert.assertTrue("The instruction file's payload type " + payloadTypeToTest.getCvName() + "does not match the retrievedtype " + submittedJobDto.getPayloadType(),
                submittedJobDto.getPayloadType().equals(payloadTypeToTest.getCvName()));


        //VERIFY THAT THE DATSET RECORD HAS THE JOB ID FOR THE JOB
        Integer loadedDataSetId = loaderInstructionFileDTOResponse.getGobiiLoaderInstructions().get(0).getDataSetId();
        RestUri dataSEtUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        dataSEtUri.setParamValue("id", loadedDataSetId.toString());
        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceForLoadedDataset = new GobiiEnvelopeRestResource<>(dataSEtUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeForLoadedDataset = gobiiEnvelopeRestResourceForLoadedDataset
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForLoadedDataset.getHeader()));
        DataSetDTO dataSetDTOResponsForLoadedDataset = resultEnvelopeForLoadedDataset.getPayload().getData().get(0);

        Assert.assertNotNull("Dataset record does not have a job id",
                dataSetDTOResponsForLoadedDataset.getJobId());
        Assert.assertEquals("The loade dataset record does not have the correct job ID",
                submittedJobDto.getJobId(),  dataSetDTOResponsForLoadedDataset.getJobId());



        // ************** VERIFY THAT WE CAN MEANINGFULLY TEST FOR NON EXISTENT DIRECTORIES
        String newInstructionFileName = "testapp_" + DateUtils.makeDateIdString();
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileName);

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setRequireDirectoriesToExist(true); // <== should result in validation error


        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertTrue("Non-existent files are not being reported properly: there are not two ENTITY_DOES_NOT_EXIST errors",
                1 ==
                        loaderInstructionFileDTOResponseEnvelope.getHeader().getStatus()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getGobiiValidationStatusType()
                                                .equals(GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .count()

        );


        // ************** VERIFY THAT THE DIRECTORIES WE SHOULD HAVE CREATED DO EXIST
        String newInstructionFileNameNoError = "testapp_" + DateUtils.makeDateIdString();
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName)
                .setRequireDirectoriesToExist(true); // <== should result in validation error

        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(loaderInstructionFileDTOResponseEnvelope.getHeader()));

        // ************** VERIFY THAT WE GET AN ERROR WHEN A FILE ALREADY EXISTS
        loaderInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertTrue("At least one error message should contain 'already exists'",
                loaderInstructionFileDTOResponseEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("already exists"))
                        .count()
                        > 0);


        // ************** VERIFY THAT WE ERROR ON USER INPUT FILE THAT SHOULD EXISTS BUT DOESN'T EXIST

        loaderInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setCreateSource(false); // <== should result in validation error


        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        PayloadEnvelope<LoaderInstructionFilesDTO> testForuserInputFileExistsCausesErrorEnvelopse = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertTrue(
                1 ==
                        testForuserInputFileExistsCausesErrorEnvelopse.getHeader()
                                .getStatus()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getGobiiValidationStatusType()
                                                .equals(GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .count()
        );

        // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality
        String instructionFileDirectory = GobiiClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);
        String bogusUserInputFile = instructionFileDirectory + newInstructionFileNameNoError + ".json";

        loaderInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());
        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(bogusUserInputFile)
                .setDestination(testDestinationDirName)
                .setCreateSource(false); // <== should result in validation error

        payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
        PayloadEnvelope<LoaderInstructionFilesDTO> testForuserInputFileExistsNoErrorEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(testForuserInputFileExistsNoErrorEnvelope.getHeader()));

    }

    @Ignore
    public void testInvalidExperimentForDataset() throws Exception {

        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();

        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = GobiiClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);

        String digesterOutputDirectory =
                GobiiClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String instructionOneTableName = "test_table1";

        //get existing dataset
        Integer dataSetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET));

        RestUri datasetsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        datasetsUri.setParamValue("id", dataSetId.toString());

        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(datasetsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceDataSet.get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelopeDataSet.getPayload().getData().get(0);

        Integer experimentId = dataSetDTOResponse.getExperimentId();

        // invalid experimentId
        Integer invalidExperimentId = experimentId + 1;

        String gobiiCropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        /**  InstructionOne START -  test for wrong experimentId for given dataset **/
        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();

        gobiiLoaderInstructionOne.setTable(instructionOneTableName);
        gobiiLoaderInstructionOne.setDataSetId(dataSetId);
        gobiiLoaderInstructionOne.getDataSet().setId(dataSetId);
        gobiiLoaderInstructionOne.getDataSet().setName(dataSetDTOResponse.getDatasetName());
        gobiiLoaderInstructionOne.setGobiiCropType(gobiiCropType);
        gobiiLoaderInstructionOne.getExperiment().setId(invalidExperimentId);

        // column one
        String instructionColumnOneName = "test_column_one";
        DataSetType dataSetTypeColumnOne = DataSetType.IUPAC;
        GobiiFileColumn gobiiFileColumnOne = new GobiiFileColumn()
                .setGobiiColumnType(GobiiColumnType.CONSTANT)
                .setConstantValue("test")
                .setName(instructionColumnOneName)
                .setDataSetType(dataSetTypeColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnOne);

        // gobiifile config
        String testSourceDirName = digesterInputDirectory + "file_one_dir";
        String testDestinationDirName = digesterOutputDirectory + "file_one_dir";
        gobiiLoaderInstructionOne.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dr")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.GENERIC)
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionOne);

        /***  InstructionOne END ***/

        PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResourceForInstruction = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory
                .resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));

        PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForInstruction.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertNotEquals(null, loaderInstructionFileDTOResponseEnvelope);

        Assert.assertTrue("The error message should say that the experiment is invalid for the given dataset",
                loaderInstructionFileDTOResponseEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("the specified experiment in the dataset is incorrect"))
                        .count()
                        > 0);
    }

    @Ignore
    public void testInvalidProjectForExperiment() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();

        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = GobiiClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);

        String digesterOutputDirectory =
                GobiiClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String gobiiCropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        //get existing dataset
        Integer dataSetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET));

        RestUri datasetsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        datasetsUri.setParamValue("id", dataSetId.toString());

        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(datasetsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceDataSet.get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelopeDataSet.getPayload().getData().get(0);

        Integer experimentId = dataSetDTOResponse.getExperimentId();

        /*** InstructionTwo START - Test for wrong projectId for given experiment. Valid experimentId will be used here ***/
        GobiiLoaderInstruction gobiiLoaderInstructionTwo = new GobiiLoaderInstruction();

        String instructionTwoTableName = "test_table2";

        //get experimentDTO
        RestUri experimentsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_EXPERIMENTS);
        experimentsUri.setParamValue("id", experimentId.toString());

        GobiiEnvelopeRestResource<ExperimentDTO,ExperimentDTO> gobiiEnvelopeRestResourceExperiment = new GobiiEnvelopeRestResource<>(experimentsUri);
        PayloadEnvelope<ExperimentDTO> resultEnvelopeExperiment = gobiiEnvelopeRestResourceExperiment.get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeExperiment.getHeader()));
        ExperimentDTO experimentDTOResponse = resultEnvelopeExperiment.getPayload().getData().get(0);

        Integer projectId = experimentDTOResponse.getProjectId();

        // invalid projectId
        Integer invalidProjectId = projectId + 1;

        gobiiLoaderInstructionTwo.setTable(instructionTwoTableName);
        gobiiLoaderInstructionTwo.setDataSetId(dataSetId);
        gobiiLoaderInstructionTwo.getDataSet().setId(dataSetId);
        gobiiLoaderInstructionTwo.getDataSet().setName(dataSetDTOResponse.getDatasetName());
        gobiiLoaderInstructionTwo.setGobiiCropType(gobiiCropType);
        gobiiLoaderInstructionTwo.getExperiment().setId(experimentId);
        gobiiLoaderInstructionTwo.getExperiment().setName(experimentDTOResponse.getExperimentName());
        gobiiLoaderInstructionTwo.getProject().setId(invalidProjectId);

        // column one
        String instructionTwoColumnOneName = "test2_column_one";
        DataSetType dataSetTypeTwoColumnOne = DataSetType.IUPAC;
        GobiiFileColumn gobiiFileTwoColumnOne = new GobiiFileColumn()
                .setGobiiColumnType(GobiiColumnType.CONSTANT)
                .setConstantValue("test2")
                .setName(instructionTwoColumnOneName)
                .setDataSetType(dataSetTypeTwoColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileTwoColumnOne);

        // gobiifile config
        String testSourceTwoDirName = digesterInputDirectory + "file_two_dir";
        String testDestinationTwoDirName = digesterOutputDirectory + "file_two_dir";
        gobiiLoaderInstructionTwo.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dr")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.GENERIC)
                .setSource(testSourceTwoDirName)
                .setDestination(testDestinationTwoDirName);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionTwo);

        /***** InstructionTwo END *****/

        PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResourceForInstruction = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory
                .resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));

        PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForInstruction.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertNotEquals(null, loaderInstructionFileDTOResponseEnvelope);


        Assert.assertTrue("The error message should say that the project is invalid for the given experiment",
                loaderInstructionFileDTOResponseEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("the specified project in the experiment is incorrect"))
                        .count()
                        > 0);

    }

    @Ignore
    public void testInvalidDataTypeForDataset() throws Exception {

        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();

        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = GobiiClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);

        String digesterOutputDirectory =
                GobiiClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String instructionThreeTableName = "test_table3";

        //get existing dataset
        Integer dataSetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET));

        RestUri datasetsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        datasetsUri.setParamValue("id", dataSetId.toString());

        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(datasetsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceDataSet.get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelopeDataSet.getPayload().getData().get(0);

        Integer dataTypeId = dataSetDTOResponse.getDatatypeId();

        // invalid dataTypeId
        Integer invalidDataTypeId = dataTypeId + 1;

        String gobiiCropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        /**  InstructionOne START -  test for wrong experimentId for given dataset **/
        GobiiLoaderInstruction gobiiLoaderInstructionThree = new GobiiLoaderInstruction();

        gobiiLoaderInstructionThree.setTable(instructionThreeTableName);
        gobiiLoaderInstructionThree.setDataSetId(dataSetId);
        gobiiLoaderInstructionThree.getDataSet().setId(dataSetId);
        gobiiLoaderInstructionThree.getDataSet().setName(dataSetDTOResponse.getDatasetName());
        gobiiLoaderInstructionThree.setGobiiCropType(gobiiCropType);
        gobiiLoaderInstructionThree.getDatasetType().setId(invalidDataTypeId);

        // column one
        String instructionColumnOneName = "test_column_one";
        DataSetType dataSetTypeColumnOne = DataSetType.IUPAC;
        GobiiFileColumn gobiiFileColumnOne = new GobiiFileColumn()
                .setGobiiColumnType(GobiiColumnType.CONSTANT)
                .setConstantValue("test")
                .setName(instructionColumnOneName)
                .setDataSetType(dataSetTypeColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        gobiiLoaderInstructionThree.getGobiiFileColumns().add(gobiiFileColumnOne);

        // gobiifile config
        String testSourceDirName = digesterInputDirectory + "file_one_dir";
        String testDestinationDirName = digesterOutputDirectory + "file_one_dir";
        gobiiLoaderInstructionThree.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dr")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.GENERIC)
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionThree);

        /***  InstructionOne END ***/

        PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResourceForInstruction = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory
                .resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));

        PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForInstruction.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertNotEquals(null, loaderInstructionFileDTOResponseEnvelope);

        Assert.assertTrue("The error message should say that the data type is invalid for the given dataset",
                loaderInstructionFileDTOResponseEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("the specified data type in the dataset is incorrect"))
                        .count()
                        > 0);


    }

    @Ignore
    public void testInvalidPlatformForExperiment() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();

        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = GobiiClientContext
                .getInstance(null, false)
                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INSTRUCTIONS);

        String digesterOutputDirectory =
                GobiiClientContext.getInstance(null, false)
                        .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);

        String gobiiCropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();

        //get existing dataset
        Integer dataSetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET));

        RestUri datasetsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        datasetsUri.setParamValue("id", dataSetId.toString());

        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(datasetsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceDataSet.get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelopeDataSet.getPayload().getData().get(0);

        Integer experimentId = dataSetDTOResponse.getExperimentId();

        /*** InstructionTwo START - Test for wrong platformId for given experiment. Valid experimentId will be used here ***/
        GobiiLoaderInstruction gobiiLoaderInstructionTwo = new GobiiLoaderInstruction();

        String instructionTwoTableName = "test_table2";

        //get experimentDTO
        RestUri experimentsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_EXPERIMENTS);
        experimentsUri.setParamValue("id", experimentId.toString());

        GobiiEnvelopeRestResource<ExperimentDTO,ExperimentDTO> gobiiEnvelopeRestResourceExperiment = new GobiiEnvelopeRestResource<>(experimentsUri);
        PayloadEnvelope<ExperimentDTO> resultEnvelopeExperiment = gobiiEnvelopeRestResourceExperiment.get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeExperiment.getHeader()));
        ExperimentDTO experimentDTOResponse = resultEnvelopeExperiment.getPayload().getData().get(0);


        //get protocolDTO
        RestUri protocolUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_EXPERIMENTS)
                .setParamValue("id", experimentDTOResponse.getId().toString())
                .appendSegment(GobiiServiceRequestId.URL_PROTOCOL);

        GobiiEnvelopeRestResource<ProtocolDTO,ProtocolDTO> gobiiEnvelopeRestResourceProtocol = new GobiiEnvelopeRestResource<>(protocolUri);
        PayloadEnvelope<ProtocolDTO> resultEnvelopeProtocol = gobiiEnvelopeRestResourceProtocol.get(ProtocolDTO.class);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeProtocol.getHeader()));
        ProtocolDTO protocolDTOResponse = resultEnvelopeProtocol.getPayload().getData().get(0);

        Integer platformId = protocolDTOResponse.getPlatformId();

        //invalid platformId
        Integer invalidPlatformId = platformId + 1;

        gobiiLoaderInstructionTwo.setTable(instructionTwoTableName);
        gobiiLoaderInstructionTwo.setGobiiCropType(gobiiCropType);
        gobiiLoaderInstructionTwo.getExperiment().setId(experimentId);
        gobiiLoaderInstructionTwo.getExperiment().setName(experimentDTOResponse.getExperimentName());
        gobiiLoaderInstructionTwo.getPlatform().setId(invalidPlatformId);

        // column one
        String instructionTwoColumnOneName = "test2_column_one";
        DataSetType dataSetTypeTwoColumnOne = DataSetType.IUPAC;
        GobiiFileColumn gobiiFileTwoColumnOne = new GobiiFileColumn()
                .setGobiiColumnType(GobiiColumnType.CONSTANT)
                .setConstantValue("test2")
                .setName(instructionTwoColumnOneName)
                .setDataSetType(dataSetTypeTwoColumnOne)
                .setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);

        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileTwoColumnOne);

        // gobiifile config
        String testSourceTwoDirName = digesterInputDirectory + "file_two_dir";
        String testDestinationTwoDirName = digesterOutputDirectory + "file_two_dir";
        gobiiLoaderInstructionTwo.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dr")
                .setDestination("c://mydir")
                .setGobiiFileType(GobiiFileType.GENERIC)
                .setSource(testSourceTwoDirName)
                .setDestination(testDestinationTwoDirName);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionTwo);

        /***** InstructionTwo END *****/

        PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResourceForInstruction = new GobiiEnvelopeRestResource<>(DtoRequestGobiiFileLoadInstructionsTest.gobiiUriFactory
                .resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));

        PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForInstruction.post(LoaderInstructionFilesDTO.class,
                payloadEnvelope);

        Assert.assertNotEquals(null, loaderInstructionFileDTOResponseEnvelope);


        Assert.assertTrue("The error message should say that the platform is invalid for the given experiment",
                loaderInstructionFileDTOResponseEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("the specified platform in the experiment is incorrect"))
                        .count()
                        > 0);

    }


}
