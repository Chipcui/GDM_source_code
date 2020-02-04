package org.gobiiproject.gobiisampletrackingdao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class ProjectDaoTest {

    @Autowired
    private ProjectDao projectDao;

    @Test
    public void createNewProject() {

        Project newProject = new Project();

        newProject.setPiContactId(1);

        newProject.setProjectName("test10");

        newProject.getStatus().setCvId(1);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode testProps = mapper.createObjectNode();

        ((ObjectNode) testProps).put("4", "test9");

        newProject.setProperties(testProps);

        Integer newProjectId = projectDao.createProject(newProject);

        assertTrue(newProjectId > 0);

    }


    @Test
    public void getProjectWithExistingId() {

        // get the ID of the newly created Project
        Project project = projectDao.getProjectById(1);

        assertTrue(project.getProjectId() > 0);

        assertTrue(project.getProjectId().equals(1));


    }



}
