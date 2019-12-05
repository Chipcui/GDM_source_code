package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class DnaSampleDaoTest {

    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Test
    public void testGetDnaSamples() {

        Integer pageSize = 10;

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(0, pageSize);

        assertTrue("Empty dnasample list",dnaSamples.size() > 0);
        assertTrue("dnaSamples result list size not equal to the page size",
                dnaSamples.size() == pageSize);

    }

    @Test
    public void testGetDnaSampleByDnaSampleId() {

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(0, 10);

        assertTrue(dnaSamples.size() > 0);

        if(dnaSamples.size() > 0) {

            Integer dnaSampleQueryId = dnaSamples.get(0).getDnaSampleId();

            assertNotNull("dnaSampleId should not be null", dnaSampleQueryId);

            DnaSample dnaSamplesByDnaSampleId = dnaSampleDao.getDnaSampleByDnaSampleId(dnaSampleQueryId);


            assertNotNull(dnaSamplesByDnaSampleId);

            assertEquals("retreived dnaSample entity does not have dnaSampleId queried for",
                    dnaSamplesByDnaSampleId.getDnaSampleId(),
                    dnaSampleQueryId);

        }

    }

    @Test
    public void testGetDnaSamplesByGermplasmId() {

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(0, 10);

        assertTrue(dnaSamples.size() > 0);

        if(dnaSamples.size() > 0) {

            Integer germplasmQueryId = dnaSamples.get(0).getGermplasm().getGermplasmId();

            assertNotNull("germplasmId should not be null", germplasmQueryId);

            List<DnaSample> dnaSamplesByGermplasmId = dnaSampleDao.getDnaSamplesByGermplasmId(
                    0,
                    100,
                    germplasmQueryId);

            //Assert that there is only one dnaSample for dnaSampleId
            assertTrue("atleast one dnasample should be there as per test preparation",
                    dnaSamplesByGermplasmId.size() > 0);

            for(DnaSample dnaSampleResult : dnaSamplesByGermplasmId) {

                assertEquals("retreived dnaSample entity does not have the germplasmId queried for",
                        dnaSampleResult.getGermplasm().getGermplasmId(),
                        germplasmQueryId);
            }

        }

    }

    @Test
    public void testGetDnaSamplesByGermplasmExternalCode() {

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(0, 10);

        assertTrue(dnaSamples.size() > 0);

        if(dnaSamples.size() > 0) {

            String queryGermplasmCode = dnaSamples.get(0).getGermplasm().getExternalCode();

            assertNotNull("germplasmId should not be null", queryGermplasmCode);

            List<DnaSample> dnaSamplesByGermplasmCode = dnaSampleDao.getDnaSamplesByGermplasmExternalCode(
                    0,
                    100,
                    queryGermplasmCode);

            //Assert that there is only one dnaSample for dnaSampleId
            assertTrue("atleast one dnasample should be there as per test preparation",
                    dnaSamplesByGermplasmCode.size() > 0);

            for(DnaSample dnaSampleResult : dnaSamplesByGermplasmCode) {

                assertEquals("retreived dnaSample entity does not have the germplasmId queried for",
                        dnaSampleResult.getGermplasm().getExternalCode(),
                        queryGermplasmCode);
            }

        }

    }


}