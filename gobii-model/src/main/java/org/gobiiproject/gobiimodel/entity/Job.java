package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.IntegerArrayConverter;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;
import java.util.Date;

/**
 * Model for Dataset Entity.
 * Represents the database table dataset.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "job")
public class Job {

    @Id
    @Column(name="job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(name="name")
    private String jobName;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "cv_id")
    private Cv type = new Cv();

    @ManyToOne
    @JoinColumn(name = "payload_type_id", referencedColumnName = "cv_id")
    private Cv payloadType = new Cv();

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

    @Column(name="submitted_by")
    private Integer submittedBy;

    @Column(name="submitted_date")
    private Date submittedData;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Cv getType() {
        return type;
    }

    public void setType(Cv type) {
        this.type = type;
    }

    public Cv getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(Cv payloadType) {
        this.payloadType = payloadType;
    }

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
    }

    public Integer getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Integer submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Date getSubmittedData() {
        return submittedData;
    }

    public void setSubmittedData(Date submittedData) {
        this.submittedData = submittedData;
    }
}
