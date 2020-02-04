package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
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
public class MarkerLinkageGroupDaoTest {

    @Autowired
    private MarkerLinkageGroupDao markerLinkageGroupDao;

    @Test
    public void testGetMarkerLinkageGroups() {

        Integer pageSize = 100;

        List<MarkerLinkageGroup> markerLinkageGroups = markerLinkageGroupDao.getMarkerLinkageGroups(
                0, pageSize,
                null, null,
                null);

        assertTrue("Empty dnasample list",markerLinkageGroups.size() > 0);
        assertTrue("dnaSamples result list size not equal to the page size",
                markerLinkageGroups.size() <= pageSize);

    }


}
