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

    // these are in order of appearance in the
    // select clause of the underlying sql query
    // for ease of debugging
    private Integer dataSetId = 0;
    private String datasetName;
    private Integer experimentId;
    private String experimentName;
    private Integer projectId;
    private String projectName;
    private Integer protocolId;
    private String protocolName;
    private Integer platformId;
    private String platformName;
    private Integer callingAnalysisId;
    private String callingAnalysisName;
    private Integer piContactId;
    private String piEmail;
    private String piFirstName;
    private String piLastName;
    private String dataTable;
    private String dataFile;
    private String qualityTable;
    private String qualityFile;
    private Integer statusId;
    private Integer datatypeId;
    private String datatypeName;
    private Integer jobId;
    private Integer jobStatusId;
    private String jobStatusName;
    private Integer jobTypeId;
    private String jobTypeName;
    private Date jobSubmittedDate;
    private Integer totalSamples;
    private Integer totalMarkers;
    private List<Integer> analysesIds = new ArrayList<>();
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

    @GobiiEntityParam(paramName = "datasetName")
    public String getDatasetName() {
        return datasetName;
    }

    @GobiiEntityColumn(columnName = "datasetname")
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @GobiiEntityParam(paramName = "experimentId")
    public Integer getExperimentId() {
        return experimentId;
    }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    @GobiiEntityParam(paramName = "experimentName")
    public String getExperimentName() {
        return experimentName;
    }

    @GobiiEntityColumn(columnName = "experimentname")
    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() {
        return projectId;
    }

    @GobiiEntityColumn(columnName = "projectid")
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @GobiiEntityParam(paramName = "projectName")
    public String getProjectName() {
        return projectName;
    }

    @GobiiEntityColumn(columnName = "projectname")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    @GobiiEntityColumn(columnName = "callinganalysisid")
    public void setCallingAnalysisId(Integer callingAnalysisId) {
        this.callingAnalysisId = callingAnalysisId;
    }


    @GobiiEntityParam(paramName = "callingAnalysisName")
    public String getCallingAnalysisName() {
        return callingAnalysisName;
    }

    @GobiiEntityColumn(columnName = "callinganalysisname")
    public void setCallingAnalysisName(String callingAnalysisName) {
        this.callingAnalysisName = callingAnalysisName;
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

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    @GobiiEntityParam(paramName = "datatypeId")
    public Integer getDatatypeId() {
        return datatypeId;
    }

    @GobiiEntityColumn(columnName = "datatypeid")
    public void setDatatypeId(Integer datatypeId) {
        this.datatypeId = datatypeId;
    }

    @GobiiEntityParam(paramName = "datatypeName")
    public String getDatatypeName() {
        return datatypeName;
    }

    @GobiiEntityColumn(columnName = "datatypename")
    public void setDatatypeName(String datatypeName) {
        this.datatypeName = datatypeName;
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
    public Integer getTotalSamples() {
        return totalSamples;
    }

    @GobiiEntityColumn(columnName = "totalsamples")
    public void setTotalSamples(Integer totalSamples) {
        this.totalSamples = totalSamples;
    }

    @GobiiEntityParam(paramName = "totalMarkers")
    public Integer getTotalMarkers() {
        return totalMarkers;
    }

    @GobiiEntityColumn(columnName = "totalmarkers")
    public void setTotalMarkers(Integer totalMarkers) {
        this.totalMarkers = totalMarkers;
    }

    @GobiiEntityParam(paramName = "piEmail")
    public String getPiEmail() {
        return piEmail;
    }

    @GobiiEntityColumn(columnName = "piemail")
    public void setPiEmail(String piEmail) {
        this.piEmail = piEmail;
    }

    @GobiiEntityParam(paramName = "piFirstName")
    public String getPiFirstName() {
        return piFirstName;
    }

    @GobiiEntityColumn(columnName = "pifirstname")
    public void setPiFirstName(String piFirstName) {
        this.piFirstName = piFirstName;
    }

    @GobiiEntityParam(paramName = "piLastName")
    public String getPiLastName() {
        return piLastName;
    }

    @GobiiEntityColumn(columnName = "pilastname")
    public void setPiLastName(String piLastName) {
        this.piLastName = piLastName;
    }

    @GobiiEntityParam(paramName = "piContactId")
    public Integer getPiContactId() {
        return piContactId;
    }

    @GobiiEntityColumn(columnName = "picontactid")
    public void setPiContactId(Integer piContactId) {
        this.piContactId = piContactId;
    }

    @GobiiEntityParam(paramName = "protocolId")
    public Integer getProtocolId() {
        return protocolId;
    }

    @GobiiEntityColumn(columnName = "protocolid")
    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }

    @GobiiEntityParam(paramName = "protocolName")
    public String getProtocolName() {
        return protocolName;
    }

    @GobiiEntityColumn(columnName = "protocolname")
    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    @GobiiEntityParam(paramName = "platformId")
    public Integer getPlatformId() {
        return platformId;
    }

    @GobiiEntityColumn(columnName = "platformid")
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    @GobiiEntityParam(paramName = "platformName")
    public String getPlatformName() {
        return platformName;
    }

    @GobiiEntityColumn(columnName = "platformname")
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
