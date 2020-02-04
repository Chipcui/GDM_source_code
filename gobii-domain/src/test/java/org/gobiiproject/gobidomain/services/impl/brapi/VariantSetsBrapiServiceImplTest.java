package org.gobiiproject.gobidomain.services.impl.brapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DatasetDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@WebAppConfiguration
public class VariantSetsBrapiServiceImplTest {

    @InjectMocks
    private VariantSetsBrapiServiceImpl variansetService;

    @Mock
    private DatasetDaoImpl datasetDao;

    @Mock
    private CvDaoImpl cvDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    Random random = new Random();

    ObjectMapper mapper = new ObjectMapper();


    private Analysis createMockAnalysis(Integer analysisId) {

        Analysis analysis = new Analysis();

        analysis.setAnalysisId(analysisId);
        analysis.setAnalysisName("test-analysis-"+analysisId.toString());
        analysis.getType().setTerm("calling");

        return analysis;

    }


    private List<Dataset> getMockDatasets(Integer listSize) {

        List<Dataset> returnVal = new ArrayList();


        for(int i = 0; i < listSize; i++) {

            Dataset dataset = new Dataset();

            dataset.setDatasetId(i+1);

            dataset.setDatasetName(RandomStringUtils.random(7, true, true));

            dataset.getExperiment().setExperimentId(i);

            dataset.getCallingAnalysis().getReference().setReferenceId(i);

            dataset.getCallingAnalysis().getType().setCvId(i*10);

            dataset.getCallingAnalysis().getType().setTerm(
                    RandomStringUtils.random(7, true, true));

            //Only one analysis added in mock, so, just check the mapping for that one is correct
            Analysis analysis = new Analysis();

            analysis.setDescription(RandomStringUtils.random(7, true, true));
            analysis.setAnalysisName(RandomStringUtils.random(7, true, true));
            analysis.setAnalysisId(i);

            dataset.setCallingAnalysis(analysis);

            dataset.getMappedAnalyses().add(analysis);

            Integer[] analyses = {i};

            dataset.setAnalyses(analyses);

            dataset.setCreatedDate(new Date(random.nextLong()));
            dataset.setModifiedDate(new Date(random.nextLong()));

            dataset.setDnaRunCount(100);
            dataset.setMarkerCount(1000);


            returnVal.add(dataset);
        }


        return returnVal;
   }

    @Test
    public void testMainFieldsMapping() throws Exception {

        final Integer pageSize = 1000;

        List<Dataset> datasetsMock = getMockDatasets(pageSize);

        when (
                datasetDao.listDatasets(any(Integer.TYPE), any(Integer.TYPE), any(Integer.TYPE))
        ).thenReturn(datasetsMock);


        List<VariantSetDTO> variantSets = variansetService.listVariantSets(
                0, pageSize, null);

        assertEquals("Size mismatch", datasetsMock.size(), variantSets.size());

        for(int i = 0; i < 10; i++) {

            Integer assertIndex = new Random().nextInt(1000);

            assertEquals("variansetName check failed",
                    datasetsMock.get(assertIndex).getDatasetName(),
                    variantSets.get(assertIndex).getVariantSetName());

            assertEquals("variansetid check failed",
                    datasetsMock.get(assertIndex).getDatasetId(),
                    variantSets.get(assertIndex).getVariantSetDbId());

            assertEquals("studyDbId check failed",
                    datasetsMock.get(assertIndex).getExperiment().getExperimentId(),
                    variantSets.get(assertIndex).getStudyDbId());

            assertEquals("referenceDbId check failed",
                    datasetsMock.get(assertIndex).getCallingAnalysis().getReference().getReferenceId(),
                    variantSets.get(assertIndex).getReferenceSetDbId());

            assertEquals("created check failed",
                    datasetsMock.get(assertIndex).getCreatedDate(),
                    variantSets.get(assertIndex).getCreated());

            assertEquals("updated check failed",
                    datasetsMock.get(assertIndex).getModifiedDate(),
                    variantSets.get(assertIndex).getUpdated());

            assertEquals("analysis size check",
                    datasetsMock.get(assertIndex).getMappedAnalyses().size(),
                    variantSets.get(assertIndex).getAnalyses().size());

            //Only one analysis added in mock, so, just check the mapping for that one is correct
            assertTrue("Analysis got mapped to the Variantset Analysis",
                    variantSets.get(assertIndex).getAnalyses().iterator().hasNext());

            Analysis analysis = datasetsMock.get(assertIndex).getMappedAnalyses().iterator().next();

            AnalysisBrapiDTO analysisBrapiDTO = variantSets.get(assertIndex).getAnalyses().iterator().next();

            assertEquals("check analysisDbId is mapped",
                    analysis.getAnalysisId(),
                    analysisBrapiDTO.getAnalysisDbId());


            assertEquals("check analysisName is mapped",
                    analysis.getAnalysisName(),
                    analysisBrapiDTO.getAnalysisName());

            assertEquals("check analysis description is mapped",
                    analysis.getDescription(),
                    analysisBrapiDTO.getDescription());



        }

    }



}
