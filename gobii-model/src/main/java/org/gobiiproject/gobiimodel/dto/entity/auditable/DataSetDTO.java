package org.gobiiproject.gobiimodel.dto.entity.auditable;


import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class DataSetDTO extends DTOBaseAuditable {

    public DataSetDTO() {
        super(GobiiEntityNameType.DATASET);
    }

    @Override
    public Integer getId() {
        return this.dataSetId;
    }

    @Override
    public void setId(Integer id) {
        this.dataSetId = id;
    }

    private Integer dataSetId = 0;
    private String name;
    private Integer experimentId;
    private Integer callingAnalysisId;
//    private AnalysisDTO callingAnalysis;
    private String dataTable;
    private String dataFile;
    private String qualityTable;
    private String qualityFile;
    private Integer statusId;
    private Integer typeId;
    private Integer jobId;
    private Integer jobStatusId;
    private String jobStatusName;
    private Integer jobTypeId;
    private String jobTypeName;
    private Date jobSubmittedDate;
    private List<Integer> analysesIds = new ArrayList<>();
    private Long totalSamples;
    private Long totalMarkers;
  //  private List<AnalysisDTO> analyses = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();

    @GobiiEntityParam(paramName = "dataSetId")
    public Integer getDataSetId() {
        return dataSetId;
    }

    @GobiiEntityColumn(columnName = "dataset_id")
    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "experimentId")
    public Integer getExperimentId() {
        return experimentId;
    }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

//    public AnalysisDTO getCallingAnalysis() {
//        return callingAnalysis;
//    }
//    public void setCallingAnalysis(AnalysisDTO callingAnalysis) {
//        this.callingAnalysis = callingAnalysis;
//    }

    @GobiiEntityParam(paramName = "callingAnalysisId")
    public Integer getCallingAnalysisId() {
        return callingAnalysisId;
    }

    @GobiiEntityColumn(columnName = "callinganalysis_id")
    public void setCallingAnalysisId(Integer callingAnalysisId) {
        this.callingAnalysisId = callingAnalysisId;
    }


    @GobiiEntityParam(paramName = "dataTable")
    public String getDataTable() {
        return dataTable;
    }

    @GobiiEntityColumn(columnName = "data_table")
    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    @GobiiEntityParam(paramName = "dataFile")
    public String getDataFile() {
        return dataFile;
    }

    @GobiiEntityColumn(columnName = "data_file")
    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    @GobiiEntityParam(paramName = "qualityTable")
    public String getQualityTable() {
        return qualityTable;
    }

    @GobiiEntityColumn(columnName = "quality_table")
    public void setQualityTable(String qualityTable) {
        this.qualityTable = qualityTable;
    }

    @GobiiEntityParam(paramName = "qualityFile")
    public String getQualityFile() {
        return qualityFile;
    }

    @GobiiEntityColumn(columnName = "quality_file")
    public void setQualityFile(String qualityFile) {
        this.qualityFile = qualityFile;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatusId() {
        return statusId;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }


    @GobiiEntityParam(paramName = "datasetAnalysIds")
    public List<Integer> getAnalysesIds() {
        return analysesIds;
    }

    @GobiiEntityColumn(columnName = "analyses")
    public void setAnalysesIds(List<Integer> analysesIds) {
        this.analysesIds = analysesIds;
    }


//    public List<AnalysisDTO> getAnalyses() {
//        return analyses;
//    }
//    public void setAnalyses(List<AnalysisDTO> analyses) {
//        this.analyses = analyses;
//    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    @GobiiEntityParam(paramName = "typeId")
    public Integer getTypeId() {
        return typeId;
    }

    @GobiiEntityColumn(columnName = "type_id")
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @GobiiEntityParam(paramName = "jobId")
    public Integer getJobId() { return jobId; }

    @GobiiEntityColumn(columnName = "job_id")
    public void setJobId(Integer jobId) { this.jobId = jobId; }

    @GobiiEntityParam(paramName = "jobStatusId")
    public Integer getJobStatusId() {
        return jobStatusId;
    }

    @GobiiEntityColumn(columnName = "jobstatusid")
    public void setJobStatusId(Integer jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    @GobiiEntityParam(paramName = "jobStatusName")
    public String getJobStatusName() {
        return jobStatusName;
    }

    @GobiiEntityColumn(columnName = "jobstatusname")
    public void setJobStatusName(String jobStatusName) {
        this.jobStatusName = jobStatusName;
    }


    @GobiiEntityParam(paramName = "jobTypeId")
    public Integer getJobTypeId() {
        return jobTypeId;
    }

    @GobiiEntityColumn(columnName = "jobtypeid")
    public void setJobTypeId(Integer jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    @GobiiEntityParam(paramName = "jobTypeName")
    public String getJobTypeName() {
        return jobTypeName;
    }

    @GobiiEntityColumn(columnName = "jobtypename")
    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    @GobiiEntityParam(paramName = "jobSubmittedDate")
    public Date getJobSubmittedDate() {
        return jobSubmittedDate;
    }

    @GobiiEntityColumn(columnName = "jobsubmitteddate")
    public void setJobSubmittedDate(Date jobSubmittedDate) {
        this.jobSubmittedDate = jobSubmittedDate;
    }

    @GobiiEntityParam(paramName = "totalSamples")
    public Long getTotalSamples() {
        return totalSamples;
    }

    @GobiiEntityColumn(columnName = "totalsamples")
    public void setTotalSamples(Long totalSamples) {
        this.totalSamples = totalSamples;
    }

    @GobiiEntityParam(paramName = "totalMarkers")
    public Long getTotalMarkers() {
        return totalMarkers;
    }

    @GobiiEntityColumn(columnName = "totalmarkers")
    public void setTotalMarkers(Long totalMarkers) {
        this.totalMarkers = totalMarkers;
    }
}
