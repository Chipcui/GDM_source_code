package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;
import java.util.Date;

/**
 * Model for Analysis Entity.
 * Represents the database table dataset.
 *
 */
@Entity
@Table(name = "analysis")
public class Analysis extends BaseEntity {

    @Id
    @Column(name="analysis_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer analysisId;

    @Column(name = "name")
    private String analysisName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "cv_id")
    private Cv type = new Cv();

    @Column(name = "program")
    private String program;

    @Column(name = "sourcename")
    private String sourceName;

    @Column(name = "sourceversion")
    private String sourceVersion;

    @Column(name = "sourceuri")
    private String sourceUri;

    @ManyToOne
    @JoinColumn(name = "reference_id", referencedColumnName = "reference_id")
    private Reference reference = new Reference();

    @Column(name="parameters", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode parameters;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

    @Column(name = "timeexecuted")
    private Date timeExecuted;

    public Integer getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Integer analysisId) {
        this.analysisId = analysisId;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cv getType() {
        return type;
    }

    public void setType(Cv type) {
        this.type = type;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public JsonNode getParameters() {
        return parameters;
    }

    public void setParameters(JsonNode parameters) {
        this.parameters = parameters;
    }

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
    }

    public Date getTimeExecuted() {
        return timeExecuted;
    }

    public void setTimeExecuted(Date timeExecuted) {
        this.timeExecuted = timeExecuted;
    }
}
