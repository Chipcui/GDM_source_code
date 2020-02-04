package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LinkageGroupBrapiService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapLinkageGroupBrApi;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.LinkageGroupBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LinkakgeGroupBrapiServiceImpl implements LinkageGroupBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(LinkakgeGroupBrapiServiceImpl.class);

    @Autowired
    private DtoMapLinkageGroupBrApi dtoMapLinkageGroupBrApi;

    /**
     * Gets the list of LinkageGroups in the database by Page Number and Page Size
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Specified Linkage Group DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<LinkageGroupBrapiDTO> getLinkageGroups(
            Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<LinkageGroupBrapiDTO> returnVal = new ArrayList<>();

        try {

            return dtoMapLinkageGroupBrApi.listLinkageGroup(pageNum, pageSize);

        } catch (Exception e) {

            LOGGER.error("Gobii service error: Unknown system error. ", e);
            throw new GobiiDomainException(e);

        }

    }

    /**
     * Gets the list of LinkageGroups in the database by MapId, Page Number and Page Size
     * @param mapId - Linkage Group Id
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Linkage Group DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<LinkageGroupBrapiDTO> getLinkageGroupsByMapId(Integer mapId,
            Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<LinkageGroupBrapiDTO> returnVal = new ArrayList<>();

        try {

            return dtoMapLinkageGroupBrApi.listLinkageGroupByMapId(mapId, pageNum, pageSize);

        } catch (Exception e) {

            LOGGER.error("Gobii service error: Unknown system error", e);
            throw new GobiiDomainException(e);

        }

    }

}
