package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapLinkageGroupBrApi;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapMapsetBrApi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.LinkageGroupBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoMapLinkageGroupBrapiImpl implements DtoMapLinkageGroupBrApi {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDatasetBrapiImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Override
    public List<LinkageGroupBrapiDTO> listLinkageGroup(Integer pageNum, Integer pageSize) {

        List<LinkageGroupBrapiDTO> linkageGroupsList = new ArrayList<>();

        Map<String, Object> sqlParams = new HashMap<>();

        try {

            sqlParams.put("pageNum", pageNum);

            sqlParams.put("pageSize", pageSize);

            linkageGroupsList = dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_LINKAGE_GROUP_BRAPI_BYLIST, null, sqlParams);

            return linkageGroupsList;

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

    }

    @Override
    public List<LinkageGroupBrapiDTO> listLinkageGroupByMapId(Integer mapId,
                                                              Integer pageNum, Integer pageSize) {

        List<LinkageGroupBrapiDTO> linkageGroupsList = new ArrayList<>();

        Map<String, Object> sqlParams = new HashMap<>();

        try {

            sqlParams.put("mapId", mapId);

            sqlParams.put("pageNum", pageNum);

            sqlParams.put("pageSize", pageSize);

            linkageGroupsList = dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_LINKAGE_GROUP_BY_MAP_BRAPI_BYLIST, null, sqlParams);

            return linkageGroupsList;

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

    }

}
