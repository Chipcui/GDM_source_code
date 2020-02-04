package org.gobiiproject.gobiimodel.entity;

import javax.persistence.*;

/**
 * Model for Experiment Entity.
 * Represents database table Experiment.
 */
@Entity
@Table(name = "experiment")
public class Experiment extends BaseEntity{


    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentCode() {
        return experimentCode;
    }

    public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getManifestId() {
        return manifestId;
    }

    public void setManifestId(Integer manifestId) {
        this.manifestId = manifestId;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public Integer getVendorProtocolId() {
        return vendorProtocolId;
    }

    public void setVendorProtocolId(Integer vendorProtocolId) {
        this.vendorProtocolId = vendorProtocolId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_id")
    private Integer experimentId;

    @Column(name="name")
    private String experimentName;

    @Column(name="code")
    private String experimentCode;

    @Column(name="project_id")
    private Integer projectId;

    @Column(name="manifest_id")
    private Integer manifestId;

    @Column(name="data_file")
    private String dataFile;

    @Column(name="vendor_protocol_id")
    private Integer vendorProtocolId;

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
