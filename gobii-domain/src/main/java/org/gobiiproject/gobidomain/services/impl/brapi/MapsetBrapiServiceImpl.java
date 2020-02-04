package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LinkageGroupBrapiService;
import org.gobiiproject.gobidomain.services.MapsetBrapiService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapMapsetBrApi;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MapsetBrapiServiceImpl implements MapsetBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(MapsetBrapiServiceImpl.class);

    @Autowired
    private DtoMapMapsetBrApi dtoMapMapsetBrApi;

    @Autowired
    private LinkageGroupBrapiService linkageGroupBrapiService;


    /**
     * Gets the list of Genome Maps in the database by Page Number and Page Size
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Specified Genome Maps DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<MapsetBrapiDTO> getMapSets(
            Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<MapsetBrapiDTO> returnVal = new ArrayList<>();

        try {

            return dtoMapMapsetBrApi.listMapset(pageNum, pageSize);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

    }

    /**
     * Gets the Genome Map by map Id. The linkage group list in the map is paged.
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return MapSetBrapiDTO
     * @throws GobiiDomainException
     */
    @Override
    public MapsetBrapiDTO getMapSet(
            Integer mapSetId,
            Integer pageNum,
            Integer pageSize) throws GobiiDomainException {

        MapsetBrapiDTO returnVal = new MapsetBrapiDTO();

        try {

            returnVal = dtoMapMapsetBrApi.getMapsetById(mapSetId);

            if(returnVal.getMapDbId() > 0) {

                returnVal.setLinkageGroups(
                        linkageGroupBrapiService.getLinkageGroupsByMapId(returnVal.getMapDbId(), pageNum, pageSize));

            }



            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified mapsetId ("
                                + mapSetId
                                + ") does not match an existing mapset ");
            }
            return returnVal;
        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

    }

}
