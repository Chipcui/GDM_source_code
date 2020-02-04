package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

import java.util.List;

/**
 * Brapi DTO for Genome Maps List
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapsetBrapiDTO {


    private Integer mapDbId;

    private String comments;

    private String type;

    private String mapName;

    private Integer linkageGroupCount;

    public String getComments() {
        return comments;
    }

    public Integer getMapDbId() {
        return mapDbId;
    }

    public List<LinkageGroupBrapiDTO> getLinkageGroups() {
        return linkageGroups;
    }

    public void setLinkageGroups(List<LinkageGroupBrapiDTO> linkageGroups) {
        this.linkageGroups = linkageGroups;
    }

    @JsonProperty(value = "data")
    private List<LinkageGroupBrapiDTO> linkageGroups;

    @GobiiEntityColumn(columnName = "mapset_id")
    public void setMapDbId(Integer mapDbId) {
        this.mapDbId = mapDbId;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {


        if (this.type != null && this.type.toLowerCase().equals("physical")) {
            return "Mb";
        } else if (this.type != null && this.type.toLowerCase().equals("genetic")) {
            return "cM";
        }

        return "";
    }

    @GobiiEntityColumn(columnName = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getMapName() {
        return mapName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setMapName(String name) {
        this.mapName = name;
    }

    public Integer getLinkageGroupCount() {
        return linkageGroupCount;
    }

    @GobiiEntityColumn(columnName = "linkage_group_count")
    public void setLinkageGroupCount(Integer linkageGroupCount) {
        this.linkageGroupCount = linkageGroupCount;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    @GobiiEntityColumn(columnName = "marker_count")
    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    private Integer markerCount;

}
