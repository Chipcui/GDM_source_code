package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;

/**
 * Model for Project Entity.
 * Represents the database table project.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @Column(name="project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(name="name")
    private String projectName;

    @Column(name="pi_contact")
    private Integer piContactId;

    @Column(name="code")
    private String projectCode;

    @Column(name="description")
    private String projectDescription;


    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode properties = JsonNodeFactory.instance.objectNode();

    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public Integer getPiContactId() {
        return this.piContactId;
    }

    public void setPiContactId(Integer piContactId) {
        this.piContactId = piContactId;
    }

    public String getProjectCode() {

        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectDescription() {
        return this.projectDescription;
    }

    public void  setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public JsonNode getProperties() {
        return this.properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

}
