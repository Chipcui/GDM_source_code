package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.IntegerArrayConverter;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Model for Dataset Entity.
 * Represents the database table dataset.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "dataset")
public class Dataset extends BaseEntity {

    @Id
    @Column(name="dataset_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer datasetId;

    @Column(name="name")
    private String datasetName;

    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment = new Experiment();

    @ManyToOne
    @JoinColumn(name = "callinganalysis_id", referencedColumnName = "analysis_id")
    private Analysis callingAnalysis = new Analysis();

    @Column(name = "analyses")
    @Convert(converter = IntegerArrayConverter.class)
    private Integer[] analyses;

    @Transient
    private Set<Analysis> mappedAnalyses = new HashSet<>();

    @Column(name="data_table")
    private String dataTable;

    @Column(name="data_file")
    private String dataFile;

    @Column(name="quality_table")
    private String qualityTable;

    @Column(name="quality_file")
    private String qualityFile;

    @Column(name="scores")
    @Convert(converter = JsonbConverter.class)
    private JsonNode scores;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "cv_id")
    private Cv type = new Cv();

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "job_id")
    private Job job = new Job();

    @Transient
    private Integer markerCount;

    @Transient
    private Integer dnaRunCount;

    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Analysis getCallingAnalysis() {
        return callingAnalysis;
    }

    public void setCallingAnalysis(Analysis callingAnalysis) {
        this.callingAnalysis = callingAnalysis;
    }


    public Integer[] getAnalyses() {
        return analyses;
    }

    public void setAnalyses(Integer[] analyses) {
        this.analyses = analyses;
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getQualityTable() {
        return qualityTable;
    }

    public void setQualityTable(String qualityTable) {
        this.qualityTable = qualityTable;
    }

    public JsonNode getScores() {
        return scores;
    }

    public void setScores(JsonNode scores) {
        this.scores = scores;
    }

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
    }

    public Cv getType() {
        return type;
    }

    public void setType(Cv type) {
        this.type = type;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getQualityFile() {
        return qualityFile;
    }

    public void setQualityFile(String qualityFile) {
        this.qualityFile = qualityFile;
    }

    public Set<Analysis> getMappedAnalyses() {
        return mappedAnalyses;
    }

    public void setMappedAnalyses(Set<Analysis> mappedAnalyses) {
        this.mappedAnalyses = mappedAnalyses;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    public Integer getDnaRunCount() {
        return dnaRunCount;
    }

    public void setDnaRunCount(Integer dnaRunCount) {
        this.dnaRunCount = dnaRunCount;
    }
}
