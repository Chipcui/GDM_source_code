package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Project;

import java.util.List;
import java.util.Map;

public interface DatasetDao {

    List<Dataset> listDatasets(Integer pageSize, Integer rowOffset, Integer datasetId);
    List<Dataset> listDatasetsByPageCursor(String pageCursor, Integer pageSize);
    Dataset getDatasetById(Integer datasetId);

}
