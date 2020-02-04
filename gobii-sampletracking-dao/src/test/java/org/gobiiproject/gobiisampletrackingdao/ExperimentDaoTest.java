package org.gobiiproject.gobiisampletrackingdao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class ExperimentDaoTest {

    @Autowired
    private ExperimentDao experimentDao;

    @Test
    public void createNewExperiment() {

        Experiment newExperiment = new Experiment();

        newExperiment.setProjectId(2);

        newExperiment.setExperimentName("test");

        newExperiment.setExperimentCode("test");

        newExperiment.getStatus().setCvId(57);

        Integer newExperimentId = experimentDao.createExperiment(newExperiment);

        assertTrue(newExperimentId > 0);

    }





}
