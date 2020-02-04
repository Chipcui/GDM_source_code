package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;

import javax.persistence.Tuple;
import java.util.List;

public interface MarkerDao {

    List<Marker> getMarkers(Integer pageSize, Integer rowOffset,
                            Integer markerId, Integer datasetId);

    Marker getMarkerById(Integer markerId);

    List<Marker> getMarkersByMarkerIdCursor(Integer markerId,
                                            Integer datasetId,
                                            Integer pageSize);

    List<Marker> getMarkersByDatasetId(Integer datasetId,
                                       Integer pageSize, Integer rowOffset);

    List<Marker> getMarkersWithStartAndStop(Integer pageSize, Integer rowOffset,
                            Integer markerId, Integer datasetId);

    List<Marker> getMarkersByMarkerIds(List<Integer> markerIds);
    List<Marker> getMarkersByMarkerNames(List<String> markerNames);

}
