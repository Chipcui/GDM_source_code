package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.IntegerArrayConverter;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;
import org.gobiiproject.gobiimodel.entity.JpaConverters.StringArrayConverter;

import javax.persistence.*;

/**
 * Model for Dataset Entity.
 * Represents the database table dataset.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "dataset")
public class Dataset {

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

    @Column(name="analyses")
    @Convert(converter = IntegerArrayConverter.class)
    private Integer[] analyses;

    @Column(name="data_table")
    private String dataTable;

    @Column(name="data_file")
    private String dataFile;

    @Column(name="quality_table")
    private String qualityTable;

    @Column(name="quality_file")
    private String quality_file;

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
    private Job job;

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

    public String getQuality_file() {
        return quality_file;
    }

    public void setQuality_file(String quality_file) {
        this.quality_file = quality_file;
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
}
