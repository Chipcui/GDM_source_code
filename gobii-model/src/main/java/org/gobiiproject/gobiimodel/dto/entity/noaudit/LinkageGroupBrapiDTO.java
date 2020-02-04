package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

import java.math.BigDecimal;

/**
 * Brapi DTO for LinkageGroups
 */
public class LinkageGroupBrapiDTO {


    private String linkageGroupName;

    private BigDecimal maxPosition;

    private Long markerCount;

    public String getLinkageGroupName() {
        return linkageGroupName;
    }

    @GobiiEntityColumn(columnName = "linkage_group_name")
    public void setLinkageGroupName(String linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public BigDecimal getMaxPosition() {
        return maxPosition;
    }

    @GobiiEntityColumn(columnName = "max_position")
    public void setMaxPosition(BigDecimal maxPosition) {
        this.maxPosition = maxPosition;
    }

    public Long getMarkerCount() {
        return markerCount;
    }

    @GobiiEntityColumn(columnName = "marker_count")
    public void setMarkerCount(Long markerCount) {
        this.markerCount = markerCount;
    }

}
