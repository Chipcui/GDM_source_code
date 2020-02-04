package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;

import java.util.List;

public interface MarkerLinkageGroupDao {

    List<MarkerLinkageGroup> getMarkerLinkageGroups(Integer pageNum, Integer pageSize,
                                                    Integer markerId, Integer linkageGroupId,
                                                    Integer datasetId);

}
