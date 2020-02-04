package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class DnaSampleDaoTest {

    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Test
    public void testGetDnaSamples() {

        Integer pageSize = 10;

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(pageSize, null);

        assertTrue("Empty dnasample list",dnaSamples.size() > 0);
        assertTrue("dnaSamples result list size not equal to the page size",
                dnaSamples.size() == pageSize);

    }

    @Test
    public void testGetDnaSampleByDnaSampleId() {

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(10, null);

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

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(10, null);

        assertTrue(dnaSamples.size() > 0);

        if(dnaSamples.size() > 0) {

            Integer germplasmQueryId = dnaSamples.get(0).getGermplasm().getGermplasmId();

            assertNotNull("germplasmId should not be null", germplasmQueryId);

            List<DnaSample> dnaSamplesByGermplasmId = dnaSampleDao.getDnaSamplesByGermplasmId(
                    germplasmQueryId,
                    100,
                    null);

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

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(100, 0);

        assertTrue(dnaSamples.size() > 0);

        if(dnaSamples.size() > 0) {

            String queryGermplasmCode = dnaSamples.get(0).getGermplasm().getExternalCode();

            assertNotNull("germplasmId should not be null", queryGermplasmCode);

            List<DnaSample> dnaSamplesByGermplasmCode = dnaSampleDao.getDnaSamplesByGermplasmExternalCode(
                    queryGermplasmCode,
                    100,
                    null);

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
