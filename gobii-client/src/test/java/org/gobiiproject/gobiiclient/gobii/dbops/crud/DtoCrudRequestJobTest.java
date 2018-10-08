package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.RestRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.gobii.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by VCalaminos on 8/25/2017.
 */
public class DtoCrudRequestJobTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    @Override
    public void create() throws Exception {

        // make dummy statusDTO
        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();

        RestUri statusUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(statusUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        JobDTO jobDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(jobDTOResponse, null);
        Assert.assertTrue(jobDTOResponse.getJobId() > 0);

    }

    /* This unit test should test the creation of a matrix job with dataset ID is not specified
     *  The exception should be tested in this function
     * */
    @Test
    public void createFailMatrixJob() throws Exception {

        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();

        newJobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName());

        RestUri jobUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(jobUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        Assert.assertTrue("The error message should contain 'Matrix load job does not have a dataset id'",
                resultEnvelope.getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().contains("Matrix load job does not have a dataset id"))
                        .count()
                        > 0);

        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }

    @Test
    @Override
    public void get() throws Exception {

        RestUri restUriStatusForAll = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceForAll = new GobiiEnvelopeRestResource<>(restUriStatusForAll);
        PayloadEnvelope<JobDTO> resultEnvelopeForAll = gobiiEnvelopeRestResourceForAll
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForAll.getHeader()));
        List<JobDTO> jobDTOList = resultEnvelopeForAll.getPayload().getData();
        Assert.assertNotNull(jobDTOList);
        Assert.assertTrue(jobDTOList.size() > 0);
        Assert.assertNotNull(jobDTOList.get(0).getJobName());

        String jobName = jobDTOList.get(0).getJobName();

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParamName("jobName", RestRequestId.URL_JOB);
        restUriStatus.setParamValue("jobName", jobName);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource.get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        JobDTO jobDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(jobDTOResponse, null);
        Assert.assertTrue(jobDTOResponse.getJobName().equals(jobName));

    }


    @Test
    @Override
    public void update() throws Exception {

        RestUri restUriStatusForAll = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceForAll = new GobiiEnvelopeRestResource<>(restUriStatusForAll);
        PayloadEnvelope<JobDTO> resultEnvelopeForAll = gobiiEnvelopeRestResourceForAll
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForAll.getHeader()));
        List<JobDTO> jobDTOList = resultEnvelopeForAll.getPayload().getData();
        Assert.assertNotNull(jobDTOList);
        Assert.assertTrue(jobDTOList.size() > 0);
        Assert.assertNotNull(jobDTOList.get(0).getJobName());

        JobDTO jobDTOReceived = jobDTOList.get(0);
        String jobName = jobDTOReceived.getJobName();

        String newMessage = "new message";
        jobDTOReceived.setMessage(newMessage);

        RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParamName("jobName", RestRequestId.URL_JOB);
        restUriStatusForGetById.setParamValue("jobName", jobName);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);

        PayloadEnvelope<JobDTO> postRequestEnvelope = new PayloadEnvelope<>(jobDTOReceived, GobiiProcessType.UPDATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResourceGetById
                .put(JobDTO.class, postRequestEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        JobDTO jobDtoRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(jobDtoRetrieved.getJobName().equals(jobName));
        Assert.assertTrue(jobDtoRetrieved.getMessage().equals(newMessage));
    }

    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<JobDTO> jobDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(jobDTOList);
        Assert.assertTrue(jobDTOList.size() > 0);
        Assert.assertNotNull(jobDTOList.get(0).getJobName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == jobDTOList.size());

        List<Integer> itemToTest = new ArrayList<>();
        if (jobDTOList.size() > 50) {
            itemToTest = TestUtils.makeListOfIntegersInRange(10, jobDTOList.size());
        } else {
            for (int idx = 0; idx < jobDTOList.size(); idx++) {
                itemToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemToTest) {

            JobDTO currentJobDto = jobDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriStatusForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriStatusForGetById);
            PayloadEnvelope<JobDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(JobDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        }


    }

    @Test
    public void getJobByDataSetId() throws Exception {

        // create analysis for new dataset for this test

        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CV.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_TYPE_NAME.toString()));
        namesUri.setParamValue("filterValue", "analysis_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForAnalysisTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelopeAnalysis = gobiiEnvelopeRestResourceForAnalysisTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeAnalysis.getHeader()));
        List<NameIdDTO> analysisTypes = resultEnvelopeAnalysis.getPayload().getData();

        List<NameIdDTO> analysisProperTerm = new ArrayList<>(analysisTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(analysisProperTerm, 1);

        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(analysisDTORequest, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);
        AnalysisDTO callingAnalysisDTO = analysisDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        4,
                        entityParamValues));

        List<Integer> analysisIds = new ArrayList<>();

        for (AnalysisDTO currentAnalysis : analyses) {

            payloadEnvelopeAnalysis = new PayloadEnvelope<>(currentAnalysis, GobiiProcessType.CREATE);
            gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(RestRequestId.URL_ANALYSIS));
            analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                    payloadEnvelopeAnalysis);
            AnalysisDTO createdAnalysis = analysisDTOResponseEnvelope.getPayload().getData().get(0);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

            analysisIds.add(createdAnalysis.getAnalysisId());
        }


        // make new dataset
        DataSetDTO dataSetDTO = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        RestUri dataSetUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetPost = new GobiiEnvelopeRestResource<>(dataSetUri);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));

        DataSetDTO newDataSetDtoResponse = resultEnvelopeDataSet.getPayload().getData().get(0);
        Assert.assertTrue(newDataSetDtoResponse.getDataSetId() > 0);

        // create job

        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();

        newJobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName());
        newJobDto.getDatasetIds().add(newDataSetDtoResponse.getDataSetId());

        RestUri jobUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(jobUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        JobDTO createdJobDto = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull(createdJobDto.getJobId());

        // retrieve job by jobname
        RestUri restUriStatus = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParamName("jobName", RestRequestId.URL_JOB);
        restUriStatus.setParamValue("jobName", createdJobDto.getJobName());
        gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriStatus);
        PayloadEnvelope<JobDTO> resultEnvelopeGet = gobiiEnvelopeRestResource.get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeGet.getHeader()));
        JobDTO jobDTOResponse = resultEnvelopeGet.getPayload().getData().get(0);

        Assert.assertNotNull(jobDTOResponse.getJobName());


        // retrieve job by datasetID
        RestUri restUriJobByDataSetID = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_DATASETS)
                .addUriParam("datasetId")
                .setParamValue("datasetId", newDataSetDtoResponse.getDataSetId().toString())
                .appendSegment(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResourceByDatasetID = new GobiiEnvelopeRestResource<>(restUriJobByDataSetID);
        PayloadEnvelope<JobDTO> resultEnvelopeByDatasetID = gobiiEnvelopeRestResourceByDatasetID
                .get(JobDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeByDatasetID.getHeader()));
        JobDTO jobDTOResponseByDataSetID = resultEnvelopeByDatasetID.getPayload().getData().get(0);

        Assert.assertNotNull(jobDTOResponseByDataSetID.getJobName());
        Assert.assertTrue(jobDTOResponse.getJobName().equals(jobDTOResponseByDataSetID.getJobName()));

        // reretrieve dataset to verify JobId
        Integer jobId = jobDTOResponseByDataSetID.getJobId();
        Integer datasetId = newDataSetDtoResponse.getDataSetId();

        RestUri projectsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(RestRequestId.URL_DATASETS)
                .setParamValue("id", datasetId.toString());
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<DataSetDTO> datsetReretrieveRsultEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dataSetDTOResponse = datsetReretrieveRsultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue("The job ID on the dataset does not match the one for the job DTO",
                dataSetDTOResponse.getJobId().equals(jobId));

        //now update a different field in the dataset
        String newDatasetName = UUID.randomUUID().toString();
        dataSetDTOResponse.setDatasetName(newDatasetName);
        PayloadEnvelope<DataSetDTO> datsetUpdatedRsultEnvelope = gobiiEnvelopeRestResourceForProjects
                .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTOResponse,GobiiProcessType.UPDATE));
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(datsetUpdatedRsultEnvelope.getHeader()));

        // now examine the result of having updated the dto
        PayloadEnvelope<DataSetDTO> datsetPostUpdateEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(DataSetDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(datsetPostUpdateEnvelope.getHeader()));
        DataSetDTO dataSetDTOPostUpdate = datsetPostUpdateEnvelope.getPayload().getData().get(0);

        Assert.assertTrue("dataset dto was not updated",
                dataSetDTOPostUpdate.getDatasetName().equals(newDatasetName));

        Assert.assertEquals("The job ID of the datset was changed by an unrelated field update",
                jobId,
                dataSetDTOPostUpdate.getJobId());

    }

    @Test
    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<JobDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(JobDTO.class, RestRequestId.URL_JOB);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;


        PayloadEnvelope<JobDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);
    }


    @Test
    public void testLoadedDataWorkflow() throws Exception {
        /*
            Per GP1-1534, and until GP1-1539 is implemented, the modified_by and modified_date columns
            of the dataset table are reserved for the user and date on which a dataset was succesfully
            loaded. This test is intended to verify this workflow.
         */


        // Verify that new and modified datasets have null modified_date and modified_by colu7mns
        Integer arbitraryDatasetId = (new GlobalPkColl<DtoCrudRequestDataSetTest>())
                .getFreshPkVals(DtoCrudRequestDataSetTest.class,
                        GobiiEntityNameType.DATASET,
                        1).get(0);


        RestUri datasetUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(RestRequestId.URL_DATASETS);
        datasetUri.setParamValue("id", arbitraryDatasetId.toString());
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDatsetsets = new GobiiEnvelopeRestResource<>(datasetUri);
        PayloadEnvelope<DataSetDTO> datasetResultEnvelope = gobiiEnvelopeRestResourceForDatsetsets
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(datasetResultEnvelope.getHeader()));


        Assert.assertTrue("There is no dataset with dataset id " + arbitraryDatasetId,
                datasetResultEnvelope.getPayload().getData().size() == 1);

        DataSetDTO dataSetDTO = datasetResultEnvelope.getPayload().getData().get(0);
        Assert.assertNull("New datasets should not have a modified_by value",
                dataSetDTO.getModifiedBy());

        Assert.assertNull("New datasets should not have a modified_date value",
                dataSetDTO.getModifiedDate());

        Assert.assertNull("New datasets should not have a job id value",
                dataSetDTO.getJobId());

        dataSetDTO.setDatasetName("New foo name");
        datasetResultEnvelope = gobiiEnvelopeRestResourceForDatsetsets
                .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTO, GobiiProcessType.UPDATE));
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(datasetResultEnvelope.getHeader()));


        // re-retrieve dataset after update
        datasetResultEnvelope = gobiiEnvelopeRestResourceForDatsetsets
                .get(DataSetDTO.class);

        Assert.assertTrue("There is no dataset with dataset id " + arbitraryDatasetId,
                datasetResultEnvelope.getPayload().getData().size() == 1);

        dataSetDTO = datasetResultEnvelope.getPayload().getData().get(0);
        Assert.assertNull("Modified datasets should not have a modified_by value",
                dataSetDTO.getModifiedBy());

        Assert.assertNull("Modified datasets should not have a modified_date value",
                dataSetDTO.getModifiedDate());

        Assert.assertNull("datasets should not have a job id value until a job record has be created",
                dataSetDTO.getJobId());

        // Make a job for the dataset
        // In server-land, the job will ahve been created by the extract or load service
        // So for example in that context, the contactId will have come from the instruction
        // file. Here we will emulate that behavior.


        JobDTO newJobDto = TestDtoFactory.makePopulateJobDTO();
        Integer jobContactId = (new GlobalPkColl<DtoCrudRequestContactTest>())
                .getAPkVal(DtoCrudRequestContactTest.class,
                        GobiiEntityNameType.CONTACT);

        // give the job our contact and dataset id
        newJobDto.setSubmittedBy(jobContactId);
        newJobDto.getDatasetIds().add(arbitraryDatasetId);

        //we only update the dfataset in this way for completed load jobs
        newJobDto.setType(JobType.CV_JOBTYPE_LOAD.getCvName());
        newJobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName());
        newJobDto.setStatus(JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED.getCvName());

        RestUri statusUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_JOB);
        GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(statusUri);
        PayloadEnvelope<JobDTO> payloadEnvelope = new PayloadEnvelope<>(newJobDto, GobiiProcessType.CREATE);
        PayloadEnvelope<JobDTO> jobResultEnvelope = gobiiEnvelopeRestResource
                .post(JobDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(jobResultEnvelope.getHeader()));
        newJobDto = jobResultEnvelope.getPayload().getData().get(0); //capture the new job id

        // Now that we've createed a new job for the specified dataset with the dataset Id
        // and the contact ID (as will be done for a sucessful load job), the modified_by
        // and modified_date of the dataset record should be set accordingly
        // re-retrieve dataset after update
        datasetResultEnvelope = gobiiEnvelopeRestResourceForDatsetsets
                .get(DataSetDTO.class);
        dataSetDTO = datasetResultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull("The modified date of the the dataset should now have a value",
                dataSetDTO.getModifiedDate());

        Assert.assertEquals("The job ID for the datset does not match the job",
                dataSetDTO.getJobId(),
                newJobDto.getJobId());

        Assert.assertEquals("The modified_by value should match that the submitted by user of the job",
                dataSetDTO.getModifiedBy(),
                newJobDto.getSubmittedBy());


        // Now we are going to update the dataset to modify columns unrelated ot the
        // modify date and modified by columns
        // re-retrieve our
        datasetResultEnvelope = gobiiEnvelopeRestResourceForDatsetsets
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(datasetResultEnvelope.getHeader()));
        Assert.assertTrue("No dataset retrieved ",
                datasetResultEnvelope.getPayload().getData().size() == 1);

        DataSetDTO retrievedDatasetDto = datasetResultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue("The retrieved dataset is not the one that was tested",
                arbitraryDatasetId.equals(retrievedDatasetDto.getDataSetId()));

        String newName = UUID.randomUUID().toString();
        retrievedDatasetDto.setDatasetName(newName);
        gobiiEnvelopeRestResourceForDatsetsets.put(DataSetDTO.class, new PayloadEnvelope<>(retrievedDatasetDto,
                GobiiProcessType.UPDATE));

        datasetResultEnvelope = gobiiEnvelopeRestResourceForDatsetsets
                .get(DataSetDTO.class);
        DataSetDTO reRetrievedDatasetDto = datasetResultEnvelope.getPayload().getData().get(0);
        Assert.assertEquals("The name was not really updated",
                newName,
                reRetrievedDatasetDto.getDatasetName());

        Assert.assertNotNull("The date of the modified and reretrieved dataset should still have a value",
                reRetrievedDatasetDto.getModifiedDate());

        Assert.assertEquals("The job ID of the modified and reretrieved dataset no longer matche the job",
                reRetrievedDatasetDto.getJobId(),
                newJobDto.getJobId());

        Assert.assertEquals("The modified_by value of the modified and reretrieved dataset should still match that the submitted by user of the job",
                reRetrievedDatasetDto.getModifiedBy(),
                newJobDto.getSubmittedBy());


    }


}
