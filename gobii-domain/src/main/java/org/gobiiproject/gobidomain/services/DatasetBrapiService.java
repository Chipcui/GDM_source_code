package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/10/2019.
 */
public interface DatasetBrapiService {

    DataSetBrapiDTO getDatasetById(Integer datasetId) throws GobiiDomainException;
    List<DataSetBrapiDTO> getDatasets(Integer pageToken, Integer pageSize, DataSetBrapiDTO dataSetBrapiDTOFilter) throws GobiiDomainException;
}
