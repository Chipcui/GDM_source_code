package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapProjectImpl implements DtoMapProject {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsProjectDao rsProjectDao;

    public ProjectDTO getProject(ProjectDTO projectDTO) throws GobiiDtoMappingException {


        ProjectDTO returnVal = new ProjectDTO();

        try {

            ResultSet resultSet = rsProjectDao.getProjectDetailsForProjectId(projectDTO.getProjectId());

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException("There are more than one project records for project id: " + projectDTO.getProjectId()));
                }


                retrievedOneRecord = true;

                int projectId = resultSet.getInt("project_id");
                String projectName = resultSet.getString("name");
                String projectCode = resultSet.getString("code");
                String projectDescription = resultSet.getString("description");
                int piContact = resultSet.getInt("pi_contact");

                returnVal.setProjectId(projectId);
                returnVal.setProjectName(projectName);
                returnVal.setProjectCode(projectCode);
                returnVal.setProjectDescription(projectDescription);
                returnVal.setPiContact(piContact);
            }


        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }


        return returnVal;
    }
}
