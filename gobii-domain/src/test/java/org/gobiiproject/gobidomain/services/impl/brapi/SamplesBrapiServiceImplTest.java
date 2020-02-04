package org.gobiiproject.gobidomain.services.impl.brapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.entity.Germplasm;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class SamplesBrapiServiceImplTest {

    @InjectMocks
    private SamplesBrapiServiceImpl samplesBrapiService;

    @Mock
    private DnaSampleDaoImpl dnaSampleDao;

    @Mock
    private CvDaoImpl cvDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    Random random = new Random();

    ObjectMapper mapper = new ObjectMapper();

    private List<DnaSample> getMockDnaSamples(Integer listSize) {

        List<DnaSample> returnVal = new ArrayList();


        for(int i = 0; i < listSize; i++) {

            DnaSample dnaSample = new DnaSample();
            JsonNode properties = JsonNodeFactory.instance.objectNode();

            dnaSample.setDnaSampleId(i+1);
            dnaSample.setDnaSampleName(RandomStringUtils.random(7, true, true));
            dnaSample.setDnaSampleNum(RandomStringUtils.random(4, false, true));
            dnaSample.setDnaSampleUuid(UUID.randomUUID().toString());
            dnaSample.setProjectId(i);

            Germplasm germplasm = new Germplasm();
            germplasm.setGermplasmId(i);
            germplasm.setGermplasmName(RandomStringUtils.random(7, true, true));
            germplasm.setExternalCode(UUID.randomUUID().toString());

            dnaSample.setGermplasm(germplasm);


            dnaSample.setProperties(properties);

            returnVal.add(dnaSample);

        }



        return returnVal;
   }

   public List<Cv> createMockCvList() {

        List cvListMock = new ArrayList();

        Integer numberOfCvs = random.nextInt(9) + 1 ;

        for(int i = 0; i < numberOfCvs; i++) {

            Cv cv = new Cv();

            cv.setCvId(i);
            cv.setTerm(RandomStringUtils.random(5, true, false));

            cvListMock.add(cv);

        }

        return cvListMock;
    }

    public void addMockCvToSampleProperties(List<DnaSample> sampleList, List<Cv> cvList) {

        for(DnaSample dnaSample : sampleList) {

            Integer numberOfDnaSampleProperties = random.nextInt(cvList.size());

            JsonNode jsonbObject = JsonNodeFactory.instance.objectNode();


            for(int i = 0; i < numberOfDnaSampleProperties; i++) {

                Integer cvId = random.nextInt(cvList.size());

                ((ObjectNode) jsonbObject).put(cvId.toString(),
                        RandomStringUtils.random(4, true, true));

            }

            dnaSample.setProperties(jsonbObject);

        }

    }

    @Test
    public void testMainFieldsMapping() throws Exception {

        final Integer pageSize = 1000;

        List<DnaSample> samplesMock = getMockDnaSamples(pageSize);

        when (
                dnaSampleDao.getDnaSamples(any(Integer.TYPE), any(Integer.TYPE),
                        isNull(Integer.TYPE), any(Integer.TYPE),
                        any(Integer.TYPE), any(String.class))
        ).thenReturn(samplesMock);


        List<SamplesBrapiDTO> samplesBrapi = samplesBrapiService.getSamples(
                0, pageSize,
                null, null,
                null);

        assertEquals("Size mismatch", samplesMock.size(), samplesBrapi.size());

        for(int i = 0; i < 10; i++) {

            Integer assertIndex = new Random().nextInt(1000);

            assertEquals("germplasmDbId check failed",
                    samplesMock.get(assertIndex).getGermplasm().getGermplasmId(),
                    samplesBrapi.get(assertIndex).getGermplasmDbId());

            assertEquals("sampleDbId check failed!",
                    samplesMock.get(assertIndex).getDnaSampleId(),
                    samplesBrapi.get(assertIndex).getSampleDbId());

            assertEquals("observationUnitDbId check failed!",
                    samplesMock.get(assertIndex).getGermplasm().getExternalCode(),
                    samplesBrapi.get(assertIndex).getObservationUnitDbId());

            assertEquals("sampelName check failed!",
                    samplesMock.get(assertIndex).getDnaSampleName(),
                    samplesBrapi.get(assertIndex).getSampleName());


            assertEquals("sampleNum check failed!",
                    samplesMock.get(assertIndex).getDnaSampleNum(),
                    samplesBrapi.get(assertIndex).getWell());

            assertEquals("samplePUI check failed!",
                    samplesMock.get(assertIndex).getDnaSampleUuid(),
                    samplesBrapi.get(assertIndex).getSamplePUI());

            assertEquals("projectId check failed!",
                    samplesMock.get(assertIndex).getProjectId(),
                    samplesBrapi.get(assertIndex).getSampleGroupDbId());
        }

    }


    @Test
    public void testAdditionalInfoMapping() throws Exception {

        final Integer pageSize = 1000;

        List<DnaSample> samplesMock = getMockDnaSamples(pageSize);

        List<Cv> cvsMock = createMockCvList();

        addMockCvToSampleProperties(samplesMock, cvsMock);

        when (
                dnaSampleDao.getDnaSamples(any(Integer.TYPE), any(Integer.TYPE),
                        isNull(Integer.TYPE), any(Integer.TYPE),
                        any(Integer.TYPE), any(String.class))
        ).thenReturn(samplesMock);

        when (
                cvDao.getCvListByCvGroup(
                        any(String.class), any(GobiiCvGroupType.class))
        ).thenReturn(cvsMock);


        List<SamplesBrapiDTO> samplesBrapi = samplesBrapiService.getSamples(
                0, pageSize,
                null, null,
                null);

        assertEquals(samplesMock.size(), samplesBrapi.size());

        for(int i = 0; i < 10; i++) {

            Integer assertIndex = random.nextInt(pageSize);


            if (samplesMock.get(assertIndex).getProperties().size() > 0) {
                assertEquals("AdditionalInfor object size is not equal to persistance object",
                        samplesMock.get(assertIndex).getProperties().size(),
                        samplesBrapi.get(assertIndex).getAdditionalInfo().size());

                Map<String, Object> samplesPropertiesMap =  mapper.convertValue(
                        samplesMock.get(assertIndex).getProperties(),
                        new TypeReference<Map<String, Object>>(){});

                for(String cvId : samplesPropertiesMap.keySet()) {

                    String cvTerm = cvsMock.get(Integer.parseInt(cvId)).getTerm();

                    assertEquals(
                            "additionalInfo mapping failed",
                            samplesPropertiesMap.get(cvId).toString(),
                            samplesBrapi.get(assertIndex).getAdditionalInfo().get(cvTerm));

                }

            }
            else {
               assertNull(samplesBrapi.get(assertIndex).getAdditionalInfo());
            }
        }

    }


}
