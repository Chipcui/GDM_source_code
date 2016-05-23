// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
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

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetDataSet() throws Exception {


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(2);
        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertNotEquals(null, dataSetDTOResponse.getDataFile());
        Assert.assertNotNull(dataSetDTOResponse.getCallingAnalysisId());
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertTrue(dataSetDTOResponse
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(null))
                .toArray().length == 0);

//        if (dataSetDTOResponse.getAnalyses() != null && dataSetDTOResponse.getAnalyses().size() > 0) {
//            Assert.assertNotEquals(null, dataSetDTOResponse.getAnalyses().get(0).getAnalysisId());
//            Assert.assertTrue(dataSetDTOResponse.getAnalyses().get(0).getAnalysisId() > 0);
//        }
//        Assert.assertTrue(dataSetDTOResponse.getProperties().size() > 0);

    } //


    @Test
    public void testCreateDataSet() throws Exception {


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        // ******** make analyses we'll need for the new data set
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO callingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(callingAnalysisDTO));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analyses) {
            dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(currentAnalysis));
        }

        List<Integer> analysisIds = analyses
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        // ********** make raw data set dto and add anlyses
        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);


        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertTrue(dataSetDTOResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOResponse.getAnalysesIds().size() > 0);
        Assert.assertTrue(dataSetDTOResponse.getTypeId() > 0);


        DataSetDTO dataSetDTOReRequest = new DataSetDTO();
        dataSetDTOReRequest.setDataSetId(dataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReResponse = dtoRequestDataSet.process(dataSetDTOReRequest);

        Assert.assertNotEquals(null, dataSetDTOReResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReResponse));
        Assert.assertTrue(dataSetDTOReResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOReResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOReResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOReResponse.getAnalysesIds().size() > 0);

    }

    @Test
    public void UpdateDataSet() throws Exception {

        // ******** make analyses we'll need for the new data set
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO newCallingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newCallingAnalysisDTO));

        List<AnalysisDTO> analysesToCreate = new ArrayList<>();
        List<AnalysisDTO> analysesNew = new ArrayList<>();
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analysesToCreate) {
            AnalysisDTO newAnalysis =  dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newAnalysis));
            analysesNew.add(newAnalysis);
        }

        List<Integer> analysisIds = analysesNew
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();

        // create a new aataSet for our test
        DataSetDTO newDataSetDto = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        newCallingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        DataSetDTO newDataSetDTOResponse = dtoRequestDataSet.process(newDataSetDto);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newDataSetDto));

        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReceived));


        // so this would be the typical workflow for the client app
        dataSetDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newDataFile = UUID.randomUUID().toString();
        dataSetDTOReceived.setDataFile(newDataFile);
        Integer anlysisIdRemovedFromList = dataSetDTOReceived.getAnalysesIds().remove(0);
        Integer newCallingAnalysisId = anlysisIdRemovedFromList;
        dataSetDTOReceived.setCallingAnalysisId(newCallingAnalysisId);


        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));

        dataSetDTORequest.setProcessType(DtoMetaData.ProcessType.READ);
        dataSetDTORequest.setDataSetId(dataSetDTOResponse.getDataSetId());
        DataSetDTO dtoRequestDataSetReRetrieved =
                dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestDataSetReRetrieved));

        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataSetId().equals(dataSetDTORequest.getDataSetId()));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataFile().equals(newDataFile));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getCallingAnalysisId().equals(newCallingAnalysisId));
        Assert.assertTrue(dtoRequestDataSetReRetrieved
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(anlysisIdRemovedFromList))
                .toArray().length == 0);
    }

    public void testNewColumnValues() throws Exception {


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        // ******** make analyses we'll need for the new data set
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO callingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(callingAnalysisDTO));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analyses) {
            dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(currentAnalysis));
        }

        List<Integer> analysisIds = analyses
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        // ********** make raw data set dto and add anlyses
        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);


        //These are nullable now:
        dataSetDTORequest.setDataTable(null);
        dataSetDTORequest.setDataFile(null);

        //And we added a name
        String name = UUID.randomUUID().toString();
        dataSetDTORequest.setName(name);

        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));

        DataSetDTO dataSetDTOReRequest = new DataSetDTO();
        dataSetDTOReRequest.setDataSetId(dataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReReuestResponse = dtoRequestDataSet.process(dataSetDTOReRequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReReuestResponse));
        dataSetDTOReReuestResponse.getName().equals(name);

    }


}
