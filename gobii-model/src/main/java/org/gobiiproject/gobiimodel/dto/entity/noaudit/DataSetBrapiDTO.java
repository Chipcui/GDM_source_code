package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/9/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType", "analysisIds", "callingAnalysisId"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSetBrapiDTO extends DTOBase {

    private int variantSetDbId;
    private Integer studyDbId;
    private String variantSetName;
    private String studyName;
    private List<AnalysisBrapiDTO> analyses;
    private List<Integer> analysisIds;
    private Integer callingAnalysisId;

    @Override
    public Integer getId() { return this.variantSetDbId; }

    @Override
    public void setId(Integer id) { this.variantSetDbId = id; }

    @GobiiEntityParam(paramName = "variantSetDbId")
    public Integer getVariantSetDbId() { return this.variantSetDbId; }

    @GobiiEntityColumn(columnName = "dataset_id")
    public void setVariantSetDbId(Integer variantSetDbId) { this.variantSetDbId = variantSetDbId; }

    @GobiiEntityParam(paramName = "variantSetName")
    public String getVariantSetName() { return this.variantSetName; }

    @GobiiEntityColumn(columnName = "variantset_name")
    public void setVariantSetName(String variantSetName) { this.variantSetName = variantSetName; }

    @GobiiEntityParam(paramName = "studyDbId")
    public Integer getStudyDbId() { return this.studyDbId; }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setStudyDbId(Integer studyDbId) { this.studyDbId = studyDbId; }

    @GobiiEntityParam(paramName = "studyName")
    public String getStudyName() { return this.studyName; }

    @GobiiEntityColumn(columnName = "study_name")
    public void setStudyName(String studyName) { this.studyName = studyName; }

    @GobiiEntityParam(paramName = "analyses")
    public List<AnalysisBrapiDTO> getAnalyses() { return this.analyses; }

    public void setAnalyses(List<AnalysisBrapiDTO> analyses) { this.analyses = analyses; }

    public Integer getCallingAnalysisId() { return this.callingAnalysisId; }

    @GobiiEntityColumn(columnName = "callinganalysis_id")
    public void setCallingAnalysisId(Integer callingAnalysisId) { this.callingAnalysisId = callingAnalysisId; }

    public List<Integer> getAnalysisIds() { return this.analysisIds; }

    @GobiiEntityColumn(columnName = "analysis_ids")
    public void setAnalysisIds(List<Integer> analysisIds) { this.analysisIds = analysisIds; }

}
