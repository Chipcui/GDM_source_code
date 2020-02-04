package org.gobiiproject.gobidomain.services.impl.sampletracking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobidomain.CvIdCvTermMapper;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapProject;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class ProjectServiceImpl implements ProjectService<ProjectDTO> {

    Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);


    @Autowired
    private ContactService contactService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CvDao cvDao;

    /**
     * Creates the Project with given details.
     * @param newProjectDto
     * @return
     * @throws GobiiDomainException
     */
    @Override
    public ProjectDTO createProject(ProjectDTO newProjectDto) throws GobiiDomainException {

        try {

            Project newProject = new Project();

            ModelMapper.mapDtoToEntity(newProjectDto, newProject);

            //Setting created by contactId
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

            Integer contactId = this.contactService.getContactByUserName(userName).getContactId();

            newProject.setCreatedBy(contactId);

            //Setting created date
            newProject.setCreatedDate(new Date(new Date().getTime()));

            // Set the Status of the project as newly created by getting it respective cvId
            List<Cv> statusCvList = cvDao.getCvsByCvTermAndCvGroup(
                    "new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                    GobiiCvGroupType.GROUP_TYPE_SYSTEM);

            //As CV term is unique under its CV group, there should be only
            //one cv for term "new" under group "status"
            if(statusCvList.size() > 0) {
                Cv statusCv = statusCvList.get(0);
                newProject.getStatus().setCvId(statusCv.getCvId());
            }

            if(newProjectDto.getProperties() != null & !newProjectDto.getProperties().isEmpty()) {

                List<Cv> cvList = cvDao.getCvListByCvGroup(
                        CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);

                newProject.setProperties(
                        CvIdCvTermMapper.mapCvTermsToCvId(cvList, newProjectDto.getProperties())
                );

            }

            Integer createdProjectId = projectDao.createProject(newProject);

            if(createdProjectId > 0) {

                return this.getProjectById(createdProjectId);
            }
            else {
                throw new GobiiException("Failed creating project. System Error.");
            }
        }
        catch(GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

    }


    @Override
    public ProjectDTO replaceProject(Integer projectId, ProjectDTO newProject)
    throws GobiiDomainException{
        return newProject;
    }

    @Override
    public List<ProjectDTO> getProjectsForLoadedDatasets() throws GobiiDomainException {

        List<ProjectDTO> returnVal = null;

        return returnVal;
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId)
    throws GobiiDomainException {

        ProjectDTO returnVal = new ProjectDTO();

        try {

            Project project = projectDao.getProjectById(projectId);

            if (project == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Project not found for given id.");
            }

            ModelMapper.mapEntityToDto(project, returnVal);


            if(project.getProperties() != null && project.getProperties().size() > 0) {
                List<Cv> cvList = cvDao.getCvListByCvGroup(
                        CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);
                returnVal.setProperties(CvIdCvTermMapper.mapCvIdToCvTerms(cvList, project.getProperties()));
            }

            return returnVal;
        }
        catch (GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Override
    public List<ProjectDTO> getProjects() {
        List<ProjectDTO> returnVal = new ArrayList<>();
        return returnVal;
    }


    @Override
    public List<ProjectDTO> getProjects(Integer pageNum, Integer pageSize) {

        List<ProjectDTO> returnVal = new ArrayList<>();

        try {


            List<Project> projectList = projectDao.listProjects(pageNum, pageSize, null);

            List<Cv> cvList = cvDao.getCvListByCvGroup(
                    CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);


            for(Project project : projectList) {

                if (project != null) {

                    ProjectDTO projectDto = new ProjectDTO();

                    ModelMapper.mapEntityToDto(project, projectDto);



                    if (project.getProperties() != null && project.getProperties().size() > 0) {
                        projectDto.setProperties(CvIdCvTermMapper.mapCvIdToCvTerms(cvList, project.getProperties()));
                    }

                    returnVal.add(projectDto);
                }
            }

            return returnVal;
        }
        catch (GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

}
