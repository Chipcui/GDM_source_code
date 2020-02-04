package org.gobiiproject.gobiisampletrackingdao;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpDef;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpWorker;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Data Access Object(DAO) for Project.
 */
public class ProjectDaoImpl implements ProjectDao {

    Logger LOGGER = LoggerFactory.getLogger(ProjectDao.class);

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected SpWorker spWorker;

    /**
     * Uses createprojectwithprops stored function in postgres database.
     * Stored procedure arguments by index are below, (1-based index)
     * 1 - projectname
     * 2 - projectcode
     * 3 - projectdescription
     * 4 - picontact
     * 5 - createdby
     * 6 - createddate
     * 7 - modifiedby
     * 8 - modifieddate
     * 9 - projectstatus
     * 10 - props
     *
     * Stored function returns integer project id.
     *
     * @param newProject - Project entity to be created.
     * @return projectId - Id of the newly created project
     */
    @Override
    @Transactional
    public Integer createProject(Project newProject) {

        Integer returnVal = 0;

        try {

            SpDef spDef = new SpDef("{call createprojectwithprops(?,?,?,?,?,?,?,?,?,?)}")
                    .addParamDef(1, String.class, newProject.getProjectName())
                    .addParamDef(2, String.class, newProject.getProjectCode())
                    .addParamDef(3, String.class, newProject.getProjectDescription())
                    .addParamDef(4, Integer.class, newProject.getPiContactId())
                    .addParamDef(5, Integer.class, newProject.getCreatedBy())
                    .addParamDef(6, Date.class, newProject.getCreatedDate())
                    .addParamDef(7, Integer.class, newProject.getModifiedBy())
                    .addParamDef(8, Date.class, newProject.getModifiedDate())
                    .addParamDef(9, Integer.class, newProject.getStatus().getCvId())
                    .addParamDef(10, JsonNode.class, newProject.getProperties());

            spWorker.run(spDef);

            returnVal = spWorker.getResult();

        }
        catch (ConstraintViolationException constraintViolation) {

            String errorMsg;

            GobiiValidationStatusType statusType = GobiiValidationStatusType.BAD_REQUEST;

            // Postgresql error code for Unique Constraint Violation is 23505
            if(constraintViolation.getSQLException() != null) {

                if(constraintViolation.getSQLException().getSQLState().equals("23505")) {

                    statusType = GobiiValidationStatusType.ENTITY_ALREADY_EXISTS;

                    errorMsg = "Project already exists";

                }
                else {

                    errorMsg = "Invalid request or Missing Required fields.";

                }

            }
            else {
                errorMsg = constraintViolation.getMessage();
            }
            throw (new GobiiDaoException(
                    GobiiStatusLevel.ERROR,
                    statusType,
                    errorMsg)
            );
        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }

        return returnVal;

    }

    @Override
    public Project getProjectById(Integer projectId) {

        List<Project> projectList;

        try {

            projectList = em
                    .createNativeQuery(
                            "SELECT * FROM project WHERE project_id = ?", Project.class)
                    .setParameter(1, projectId)
                    .getResultList();



            if (projectList.size() == 0) {
                return null;
            } else if (projectList.size() > 1) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                        "Multiple resources found. Violation of Unique Project Id constraint." +
                                " Please contact your Data Administrator to resolve this. " +
                                "Changing underlying database schemas and constraints " +
                                "without consulting GOBii Team is not recommended.");

            }
        }
        catch(Exception e) {

           LOGGER.error(e.getMessage(), e);

           throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                   GobiiValidationStatusType.UNKNOWN,
                   e.getMessage());
        }

        return projectList.get(0);

    }

    @Override
    public Project getProjectByName(String projectName) {
        Project project = new Project();
        return project;
    }

    @Override
    public List<Project> listProjects(Integer pageNum, Integer pageSize, Map<String, String> projectQuery) {

        List<Project> projectList = new ArrayList<>();

        try {

            if(pageNum == null || pageSize == null) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.UNKNOWN,
                        "Required page size and page number.");
            }

            projectList = em
                    .createNativeQuery(
                            "SELECT * FROM project LIMIT ? OFFSET ? ", Project.class)
                    .setParameter(1, pageSize)
                    .setParameter(2, pageNum*pageSize)
                    .getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }

        return projectList;
    }

    @Override
    public Integer updateProject(Project newProject) {
        return 0;
    }


}
