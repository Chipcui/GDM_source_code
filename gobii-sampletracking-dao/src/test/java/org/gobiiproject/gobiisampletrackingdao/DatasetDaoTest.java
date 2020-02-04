package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Dataset;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * This tests are created with knowledge of exisiting data in api.gobii.org:gobii-dev database
 * TODO: Setup class to create required data in the test database needs to be completed in future.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class DatasetDaoTest {

    @Autowired
    private DatasetDao datasetDao;


    @Test
    public void testListDatasets() {

        Integer testPageSize = 10;

        List<Dataset> datasets = datasetDao.listDatasets(
                testPageSize, null, null);

        //As test database table has ananlyses greater than 0, assert the same
        for( Dataset dataset : datasets) {

            assertTrue(dataset.getMappedAnalyses().size() > 0);
            assertTrue(dataset.getMarkerCount() >= 0);
            assertTrue(dataset.getDnaRunCount() >= 0);

        }

        assertTrue(datasets.size() <= testPageSize);

    }



    @Test
    public void testListDatasetsWithPageSize() {

        List<Dataset> datasets = datasetDao.listDatasets(10, null, null);

        assertTrue(datasets.size() == 10);

    }



    @Test
    public void testGetDatasetById() {

        List<Dataset> datasets = datasetDao.listDatasets(10, null, null);

        assertTrue(datasets.size() == 10);

        Integer datasetId = datasets.get(9).getDatasetId();

        Dataset dataset = datasetDao.getDatasetById(datasetId);

        assertTrue(dataset.getDatasetId() == datasetId);

    }

}
