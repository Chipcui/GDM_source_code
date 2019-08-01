package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 6/25/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
        "datasetDnarunIndex", "germplasmProps", "sampleProps"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnaRunDTO extends DTOBase{

    private int callSetDbId;
    private Integer studyDbId;
    private Integer sampleDbId;
    private String callSetName;
    private String dnaRunCode;
    private List<Integer> variantSetIds = new ArrayList<>();
    private Integer germplasmDbId;
    private String germplasmName;
    private String germplasmExternalCode;
    private String germplasmType;
    private String species;
    private Map<String, Object> germplasmProps;
    private Map<String, Object> sampleProps;
    private String sampleName;
    private String sampleNum;
    private String wellRow;
    private String wellCol;
    private Map<String, Object> datasetDnarunIndex;
    private Map<String, String> additionalInfo = new HashMap<>();

    @Override
    public Integer getId() { return this.callSetDbId; }

    @Override
    public void setId(Integer id) { this.callSetDbId = id; }

    @GobiiEntityParam(paramName = "callSetDbId")
    public Integer getCallSetDbId() { return this.callSetDbId; }

    @GobiiEntityColumn(columnName = "dnarun_id")
    public void setCallSetDbId(Integer id) { this.callSetDbId = id; }

    @GobiiEntityParam(paramName = "studyDbId")
    public Integer getStudyDbId() { return this.studyDbId; }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setStudyDbId(Integer studyDbId) { this.studyDbId = studyDbId; }

    @GobiiEntityParam(paramName = "sampleDbId")
    public Integer getSampleDbId() { return this.sampleDbId; }

    @GobiiEntityColumn(columnName = "dnasample_id")
    public void setSampleDbId(Integer sampleDbId) { this.sampleDbId = sampleDbId; }

    @GobiiEntityParam(paramName = "callSetName")
    public String getCallSetName() { return this.callSetName; }

    @GobiiEntityColumn(columnName = "name")
    public void setCallSetName(String callSetName) { this.callSetName = callSetName; }

    @GobiiEntityParam(paramName = "dnaRunCode")
    public String getDnaRunCode() { return this.dnaRunCode; }

    @GobiiEntityColumn(columnName = "code")
    public void setDnaRunCode(String dnaRunCode) { this.dnaRunCode = dnaRunCode; }

    @GobiiEntityParam(paramName = "variantSetIds")
    public List<Integer> getVariantSetIds() { return this.variantSetIds; }

    public void setVariantSetIds(List<Integer> variantSetIds) { this.variantSetIds = variantSetIds; }

    @GobiiEntityParam(paramName = "additionalInfo")
    public Map<String, String> getAdditionalInfo() { return this.additionalInfo; }

    public void setAdditionalInfo(Map<String, String> additionalInfo) { this.additionalInfo = additionalInfo; }

    @GobiiEntityParam(paramName = "germplasmDbId")
    public Integer getGermplasmDbId() { return this.germplasmDbId; }

    @GobiiEntityColumn(columnName = "germplasm_id")
    public void setGermplasmDbId(Integer germplasmDbId) { this.germplasmDbId = germplasmDbId; }

    @GobiiEntityParam(paramName = "germplasmName")
    public String getGermplasmName() { return this.germplasmName; }

    @GobiiEntityColumn(columnName = "germplasm_name")
    public void setGermplasmName(String germplasmName) { this.germplasmName = germplasmName; }

    @GobiiEntityParam(paramName = "germplasmExternalCode")
    public String getGermplasmExternalCode() { return this.germplasmExternalCode; }

    @GobiiEntityColumn(columnName = "germplasm_external_code")
    public void setGermplasmExternalCode(String germplasmExternalCode) { this.germplasmExternalCode = germplasmExternalCode; }

    @GobiiEntityParam(paramName = "sampleName")
    public String getSampleName() { return this.sampleName; }

    @GobiiEntityColumn(columnName = "sample_name")
    public void setSampleName(String sampleName) { this.sampleName = sampleName; }

    @GobiiEntityParam(paramName = "germplasmType")
    public String getGermplasmType() { return this.germplasmType; }

    @GobiiEntityColumn(columnName = "germplasm_type")
    public void setGermplasmType(String germplasmType) { this.germplasmType = germplasmType; }

    @GobiiEntityParam(paramName = "species")
    public String getSpecies() { return this.species; }

    @GobiiEntityColumn(columnName = "species")
    public void setSpecies(String species) { this.species = species; }

    @GobiiEntityParam(paramName = "sampleNum")
    public String getSampleNum() { return this.sampleNum; }

    @GobiiEntityColumn(columnName = "num")
    public void setSampleNum(String num) { this.sampleNum = num; }

    @GobiiEntityParam(paramName = "wellRow")
    public String getWellRow() { return this.wellRow; }

    @GobiiEntityColumn(columnName = "well_row")
    public void setWellRow(String wellRow) { this.wellRow = wellRow; }

    @GobiiEntityParam(paramName = "wellCol")
    public String getWellCol() { return this.wellCol; }

    @GobiiEntityColumn(columnName = "well_col")
    public void setWellCol(String wellCol) { this.wellCol = wellCol; }

    @GobiiEntityColumn(columnName = "dataset_dnarun_idx")
    public void setDatasetDnarunIndex(Map<String, Object> datasetDnarunIndex) {
        this.datasetDnarunIndex = datasetDnarunIndex;
    }

    public Map<String, Object> getDatasetDnarunIndex() {
        return this.datasetDnarunIndex;
    }

    @GobiiEntityColumn(columnName = "germplasm_props")
    public void setGermplasmProps(Map<String, Object> germplasmProps) {
        this.germplasmProps = germplasmProps;
    }

    public Map<String, Object> getGermplasmProps() {
        return this.germplasmProps;
    }

    @GobiiEntityColumn(columnName = "sample_props")
    public void setSampleProps(Map<String, Object> sampleProps) {
        this.sampleProps = sampleProps;
    }

    public Map<String, Object> getSampleProps() {
        return this.sampleProps;
    }


}
