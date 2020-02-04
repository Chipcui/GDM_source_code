package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.hibernate.type.IntegerType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Test cases for DnaRunDaoImpl
 * TODO: The dataset test are written with knowledge of undelying data in
 *   api.gobii.org. Need to refactor in future with Test databases and setup data
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class DnaRunDaoTest {

    @Autowired
    private DnaRunDao dnaRunDao;

    @Test
    public void testGetDnaRuns() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        List<DnaRun> dnaruns = dnaRunDao.getDnaRuns(
                pageSize, rowOffset,
                null, null);

        assertTrue("Empty dnaRun list: ",dnaruns.size() > 0);

        if(dnaruns.size() > 0) {

            pageSize = dnaruns.size() - 1;

            List<DnaRun> dnaRunsPaged = dnaRunDao.getDnaRuns(
                    pageSize, rowOffset,
                    null, null);


            assertTrue("dnarun result list size not equal to the page size",
                    dnaRunsPaged.size() == pageSize);
        }
    }

    @Test
    public void testGetDnaRunsByDatasetId() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        List<DnaRun> dnaruns = dnaRunDao.getDnaRuns(
                pageSize, rowOffset,
                null, null);

        assertTrue("Empty dnaRun list: ",dnaruns.size() > 0);

        Integer dnaRunWithDatasetIndex = 0;

        while((dnaruns.get(dnaRunWithDatasetIndex).getDatasetDnaRunIdx() == null
                || dnaruns.get(dnaRunWithDatasetIndex).getDatasetDnaRunIdx().size() == 0)
                && dnaRunWithDatasetIndex < dnaruns.size()) {

            dnaRunWithDatasetIndex++;

        }

        assertTrue("No DnaRuns with a dataset mapped to it",
                dnaRunWithDatasetIndex < dnaruns.size());

        if(dnaRunWithDatasetIndex < dnaruns.size()) {


            Integer datasetId = Integer.parseInt(
                    dnaruns.get(dnaRunWithDatasetIndex).getDatasetDnaRunIdx().fieldNames().next());

            List<DnaRun> dnarunsByDatasetId = dnaRunDao.getDnaRunsByDatasetId(datasetId, null, null);

            assertTrue("Empty dnarun list for given dataset id", dnarunsByDatasetId.size() > 0);

            for(DnaRun dnaRun : dnarunsByDatasetId) {
                assertTrue("Fetch by dataset id not working",
                        dnaRun.getDatasetDnaRunIdx().has(datasetId.toString()));
            }

        }


    }

    @Test
    public void getDnaRunsByDnaRunIds() {

        // TODO: Hardcoded list from api.gobii.ord:gobii-dev dnarun table
        //  Need to replace this with a standard setupcalss functionality in future.
        List<Integer> dnarunIds = new ArrayList<>(Arrays.asList(
                6, 7, 8, 9, 10,
                11, 12, 13, 14,
                15
        ));

        List<DnaRun> dnaruns = dnaRunDao.getDnaRunsByDanRunIds(dnarunIds);

        assertTrue(dnaruns.size() <= dnarunIds.size());

        for(DnaRun dnaRun : dnaruns) {
            assertTrue(dnarunIds.contains(dnaRun.getDnaRunId()));
        }

    }


    @Test
    public void getDnaRunsByDnaRunNames() {

        // TODO: Hardcoded list from api.gobii.ord:gobii-dev dnarun table
        //  Need to replace this with a standard setupcalss functionality in future.
        List<String> dnarunNames = new ArrayList<>(Arrays.asList(
                "WL18PVSD000001",
                "WL18PVSD000002",
                "WL18PVSD000003",
                "WL18PVSD000004",
                "WL18PVSD000005",
                "WL18PVSD000006",
                "WL18PVSD000007",
                "WL18PVSD000008",
                "WL18PVSD000009",
                "WL18PVSD000010",
                "WL18PVSD000011",
                "WL18PVSD000012",
                "WL18PVSD000013",
                "WL18PVSD000014",
                "WL18PVSD000015"
        ));

        List<DnaRun> dnaruns = dnaRunDao.getDnaRunsByDanRunNames(dnarunNames);

        assertTrue(dnaruns.size() <= dnarunNames.size());

        for(DnaRun dnaRun : dnaruns) {
            assertTrue(dnarunNames.contains(dnaRun.getDnaRunName()));
        }


    }


}
