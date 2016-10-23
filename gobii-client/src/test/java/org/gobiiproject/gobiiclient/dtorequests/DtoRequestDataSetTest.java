// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;


import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestDataSetTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        uriFactory = new UriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetDataSet() throws Exception {


//        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
//        DataSetDTO dataSetDTORequest = new DataSetDTO();
//        dataSetDTORequest.setDataSetId(2);
//        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);


        RestUri projectsUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        projectsUri.setParamValue("id", "2");
        RestResource<DataSetDTO> restResourceForProjects = new RestResource<>(projectsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = restResourceForProjects
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertNotEquals(null, dataSetDTOResponse.getDataFile());
        Assert.assertNotNull(dataSetDTOResponse.getCallingAnalysisId());
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertTrue(dataSetDTOResponse
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(null))
                .toArray().length == 0);


    } //


    @Test
    public void testCreateDataSet() throws Exception {


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        // ******** make analyses we'll need for the new data set
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO callingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(callingAnalysisDTO));

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
            AnalysisDTO createdAnalysis = dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(createdAnalysis));
            analysisIds.add(createdAnalysis.getAnalysisId());
        }


        // ********** make raw data set dto and add anlyses
        //DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        RestUri projectsCollUri = uriFactory
                .resourceColl(ServiceRequestId.URL_DATASETS);
        RestResource<DataSetDTO> restResourceForDataSetPost = new RestResource<>(projectsCollUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = restResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTORequest, GobiiProcessType.CREATE));

        //DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertTrue(dataSetDTOResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOResponse.getAnalysesIds().size() > 0);
        Assert.assertTrue(dataSetDTOResponse.getTypeId() > 0);


//        DataSetDTO dataSetDTOReRequest = new DataSetDTO();
//        dataSetDTOReRequest.setDataSetId(dataSetDTOResponse.getDataSetId());
//        //DataSetDTO dataSetDTOReResponse = dtoRequestDataSet.process(dataSetDTOReRequest);


        RestUri projectsByIdUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        RestResource<DataSetDTO> restResourceForDataSetGet = new RestResource<>(projectsByIdUri);
        restResourceForDataSetGet.setParamValue("id", dataSetDTOResponse.getDataSetId().toString());
        resultEnvelope = restResourceForDataSetGet
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        DataSetDTO dataSetDTOReResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, dataSetDTOReResponse);
        Assert.assertTrue(dataSetDTOReResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOReResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOReResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOReResponse.getAnalysesIds().size() > 0);
        Assert.assertTrue(0 == dataSetDTOReResponse
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(null))
                .count());

    }

    @Test
    public void UpdateDataSet() throws Exception {

        // ******** make analyses we'll need for the new data set
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO newCallingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newCallingAnalysisDTO));

        List<AnalysisDTO> analysesToCreate = new ArrayList<>();
        List<AnalysisDTO> analysesNew = new ArrayList<>();
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        2,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        3,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        4,
                        entityParamValues));

        List<Integer> analysisIds = new ArrayList<>();
        for (AnalysisDTO currentAnalysis : analysesToCreate) {
            AnalysisDTO newAnalysis = dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newAnalysis));
            analysesNew.add(newAnalysis);
            analysisIds.add(newAnalysis.getAnalysisId());
        }


        //DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();

        // create a new aataSet for our test
        DataSetDTO newDataSetDto = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        newCallingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        //DataSetDTO newDataSetDTOResponse = dtoRequestDataSet.process(newDataSetDto);


        RestUri projectsCollUri = uriFactory
                .resourceColl(ServiceRequestId.URL_DATASETS);
        RestResource<DataSetDTO> restResourceForDataSetPost = new RestResource<>(projectsCollUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = restResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(newDataSetDto, GobiiProcessType.CREATE));


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        DataSetDTO newDataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
//        DataSetDTO dataSetDTORequest = new DataSetDTO();
//        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
//        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);

        RestUri projectsByIdUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        RestResource<DataSetDTO> restResourceForDataSetById = new RestResource<>(projectsByIdUri);
        restResourceForDataSetById.setParamValue("id", newDataSetDTOResponse.getDataSetId().toString());
        resultEnvelope = restResourceForDataSetById
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        DataSetDTO dataSetDTOReceived = resultEnvelope.getPayload().getData().get(0);

        // so this would be the typical workflow for the client app
        String newDataFile = UUID.randomUUID().toString();
        dataSetDTOReceived.setDataFile(newDataFile);
        Integer anlysisIdRemovedFromList = dataSetDTOReceived.getAnalysesIds().remove(0);
        Integer newCallingAnalysisId = anlysisIdRemovedFromList;
        dataSetDTOReceived.setCallingAnalysisId(newCallingAnalysisId);


        //DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTOReceived);

        resultEnvelope = restResourceForDataSetById
                .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        restResourceForDataSetById.setParamValue("id", dataSetDTOResponse.getDataSetId().toString());
        resultEnvelope = restResourceForDataSetById
                .get(DataSetDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dtoRequestDataSetReRetrieved = resultEnvelope.getPayload().getData().get(0);

//        dataSetDTORequest.setGobiiProcessType(GobiiProcessType.READ);
//        dataSetDTORequest.setDataSetId(dataSetDTOResponse.getDataSetId());
//        DataSetDTO dtoRequestDataSetReRetrieved =
//                dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataSetId().equals(dataSetDTOReceived.getDataSetId()));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataFile().equals(newDataFile));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getCallingAnalysisId().equals(newCallingAnalysisId));
        Assert.assertTrue(dtoRequestDataSetReRetrieved
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(anlysisIdRemovedFromList))
                .toArray().length == 0);
    }


    // This test is marked ignore for now because of the fact that when an entity has an array
    //column (roles in this case), the array array values are not store in the JDBC result set
    //the way that they are with other values. Rather, it seems that jdbc wants to retrieve the
    //values only when you ask for them. This kind of makes sense because who the hell knows how
    //capacious and cajunga huge the array could be? The problem is that in our contact, because of
    //the frameworkization that we've wrappeda round the application of column values to DTOs, by the
    //time we ask for the array value, the conenection is closed, and we get an exception to that
    //effect. We need to figure out a solution to this issue. Note that this is oddly not an issue
    //when retrieving a single row by ID. Only when there are multiple such rows.
    @Ignore
    public void getDataSets() throws Exception {

        RestUri restUriDataSet = DtoRequestDataSetTest.uriFactory.resourceColl(ServiceRequestId.URL_DATASETS);
        RestResource<DataSetDTO> restResource = new RestResource<>(restUriDataSet);
        PayloadEnvelope<DataSetDTO> resultEnvelope = restResource
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<DataSetDTO> dataSetDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(dataSetDTOList);
        Assert.assertTrue(dataSetDTOList.size() > 0);
        Assert.assertNotNull(dataSetDTOList.get(0).getName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == dataSetDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (dataSetDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, dataSetDTOList.size());

        } else {
            for (int idx = 0; idx < dataSetDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }
        for (Integer currentIdx : itemsToTest) {
            DataSetDTO currentDataSetDto = dataSetDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriDataSetForGetById = DtoRequestDataSetTest
                    .uriFactory
                    .RestUriFromUri(currentLink.getHref());
            RestResource<DataSetDTO> restResourceForGetById = new RestResource<>(restUriDataSetForGetById);
            PayloadEnvelope<DataSetDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(DataSetDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            DataSetDTO dataSetDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentDataSetDto.getName().equals(dataSetDTOFromLink.getName()));
            Assert.assertTrue(currentDataSetDto.getDataSetId().equals(dataSetDTOFromLink.getDataSetId()));
        }

    }
}
