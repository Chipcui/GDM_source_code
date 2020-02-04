package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 7/3/2019.
 */


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType", "datasetMarkerIndex"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarkerBrapiDTO extends DTOBase {

    private int variantDbId;
    private String variantName;
    private List<Integer> variantSetDbIds = new ArrayList<>();
    private String variantType;
    private String referenceName;
    private Map<String, Object> datasetMarkerIndex;
    private String platformName;
    private String linkageGroupName;
    private BigDecimal start;
    protected BigDecimal stop;
    private String mapSetName;
    private Integer mapSetId;

    @Override
    public Integer getId() { return this.variantDbId; }

    @Override
    public void setId(Integer id) { this.variantDbId = id; }

    @GobiiEntityParam(paramName = "variantDbId")
    @JsonProperty("variantDbId")
    public Integer getVariantDbId() { return this.variantDbId; }

    @GobiiEntityColumn(columnName = "marker_id")
    public void setVariantDbId(Integer variantDbId) { this.variantDbId = variantDbId; }

    @GobiiEntityParam(paramName = "variantName")
    @JsonProperty("variantName")
    public String getVariantName() { return this.variantName; }

    @GobiiEntityColumn(columnName = "name")
    public void setVariantName(String variantName) { this.variantName = variantName; }

    @GobiiEntityParam(paramName = "variantSetDbIds")
    public List<Integer> getVariantSetDbId() { return this.variantSetDbIds; }

    public void setVariantSetDbId(List<Integer> variantSetDbIds) { this.variantSetDbIds = variantSetDbIds; }

    @GobiiEntityParam(paramName = "variantType")
    public String getVariantType() { return this.variantType; }

    public void setVariantType(String variantType) { this.variantType = variantType; }

    @GobiiEntityParam(paramName = "referenceName")
    public String getReferenceName() { return this.referenceName; }

    @GobiiEntityColumn(columnName = "reference_name")
    public void setReferenceName(String referenceName) { this.referenceName = referenceName; }

    @GobiiEntityColumn(columnName = "dataset_marker_idx")
    public void setDatasetMarkerIndex(Map<String, Object> datasetMarkerIndex) {
        this.datasetMarkerIndex = datasetMarkerIndex;
    }

    @GobiiEntityParam(paramName = "platformName")
    public String getPlatformName() { return this.platformName; }

    @GobiiEntityColumn(columnName = "platform_name")
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    @GobiiEntityParam(paramName = "linkageGroupName")
    public String getLinkageGroupName() { return this.linkageGroupName; }

    @GobiiEntityColumn(columnName = "linkage_group_name")
    public void setLinkageGroupName(String linkageGroupName) { this.linkageGroupName = linkageGroupName; }

    @GobiiEntityParam(paramName = "start")
    public BigDecimal getStart() { return this.start; }

    @GobiiEntityColumn(columnName = "start")
    public void setStart(BigDecimal start) { this.start = start; }

    @GobiiEntityParam(paramName = "stop")
    public BigDecimal getStop() { return this.stop; }

    public BigDecimal getLocation() { return this.stop; }

    @GobiiEntityColumn(columnName = "stop")
    public void setStop(BigDecimal stop) { this.stop = stop; }

    @GobiiEntityParam(paramName = "mapSetName")
    @JsonProperty("mapName")
    public String getMapSetName() { return this.mapSetName; }

    @GobiiEntityColumn(columnName = "mapset_name")
    public void setMapSetName(String mapSetName) { this.mapSetName = mapSetName; }

    @GobiiEntityParam(paramName = "mapSetId")
    @JsonProperty("mapDbId")
    public Integer getMapSetId() { return this.mapSetId; }

    @GobiiEntityColumn(columnName = "mapset_id")
    public void setMapSetId(Integer mapSetId) { this.mapSetId = mapSetId; }

    public Map<String, Object> getDatasetMarkerIndex() {
        return this.datasetMarkerIndex;
    }

}
