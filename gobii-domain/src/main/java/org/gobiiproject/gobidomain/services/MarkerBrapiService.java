package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public interface MarkerBrapiService {

    MarkerBrapiDTO getMarkerById(Integer markerId) throws GobiiDomainException;
    List<MarkerBrapiDTO> getMarkers(Integer pageToken, Integer pageNum, Integer pageSize, MarkerBrapiDTO markerBrapiDTOFilter) throws GobiiDomainException;

}
