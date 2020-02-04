package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public interface DtoMapMarkerBrapi {

    MarkerBrapiDTO get(Integer markerId) throws GobiiDtoMappingException;
    List<MarkerBrapiDTO> getList(Integer pageToken, Integer pageNum, Integer pageSize, MarkerBrapiDTO markerBrapiDTOFilter) throws GobiiDtoMappingException;
}
