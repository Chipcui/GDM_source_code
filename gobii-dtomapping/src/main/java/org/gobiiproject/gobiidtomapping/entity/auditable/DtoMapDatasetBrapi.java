package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/10/2019.
 */
public interface DtoMapDatasetBrapi {

    DataSetBrapiDTO get(Integer datasetId) throws GobiiDtoMappingException;
    List<DataSetBrapiDTO> getList(Integer pageToken, Integer pageSize, DataSetBrapiDTO dataSetBrapiDTOFilter) throws GobiiDtoMappingException;
}
