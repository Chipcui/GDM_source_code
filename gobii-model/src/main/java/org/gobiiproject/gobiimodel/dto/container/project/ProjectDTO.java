package org.gobiiproject.gobiimodel.dto.container.project;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.StoredProcParamVal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectDTO extends DtoMetaData {

    public ProjectDTO() {}

    public ProjectDTO(ProcessType processType) {
        super(processType);
    }

    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private int projectId;

    private String projectName;
    private String projectCode;
    private String projectDescription;
    private int piContact;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer projectStatus;

    @StoredProcParamVal(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @StoredProcParamVal(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    @StoredProcParamVal(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @StoredProcParamVal(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @StoredProcParamVal(paramName = "projectStatus")
    public Integer getProjectStatus() {
        return projectStatus;
    }
    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    @StoredProcParamVal(paramName = "projectId")
    public int getProjectId() {
        return projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @StoredProcParamVal(paramName = "projectName")
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @StoredProcParamVal(paramName = "projectCode")
    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @StoredProcParamVal(paramName = "projectDescription")
    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    @StoredProcParamVal(paramName = "piContact")
    public int getPiContact() {
        return piContact;
    }
    public void setPiContact(int piContact) {
        this.piContact = piContact;
    }



    private Map<String, String> principleInvestigators = null;
    public Map<String, String> getPrincipleInvestigators() {
        return principleInvestigators;
    }
    public void setPrincipleInvestigators(Map<String, String> principleInvestigators) {
        this.principleInvestigators = principleInvestigators;
    }

    private List<ProjectProperty> properties = new ArrayList<>();
    public List<ProjectProperty> getProperties() {
        return properties;
    }
    public void setProperties(List<ProjectProperty> properties) {
        this.properties = properties;
    }



}
