package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.MarkerBrapiService;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapMarkerBrapi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public class MarkerBrapiServiceImpl implements MarkerBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(MarkerBrapiServiceImpl.class);

    @Autowired
    private DtoMapMarkerBrapi dtoMapMarkerBrapi = null;

    @Override
    public List<MarkerBrapiDTO> getMarkers(Integer pageToken, Integer pageSize, MarkerBrapiDTO markerBrapiDTOFilter) throws GobiiDomainException {

        List<MarkerBrapiDTO> returnVal;

        try {

            returnVal = dtoMapMarkerBrapi.getList(pageToken, pageSize, markerBrapiDTOFilter);

        } catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;

    }

}
