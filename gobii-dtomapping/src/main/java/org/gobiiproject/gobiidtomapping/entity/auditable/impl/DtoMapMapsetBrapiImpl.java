package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import org.gobiiproject.gobiidao.resultset.access.RsMapsetBrapiDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapMapsetBrApi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoMapMapsetBrapiImpl implements DtoMapMapsetBrApi {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDatasetBrapiImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private RsMapsetBrapiDao rsMapsetBrapiDao;

    @Override
    public List<MapsetBrapiDTO> listMapset(Integer pageNum, Integer pageSize) {

        List<MapsetBrapiDTO> mapsetList = new ArrayList<>();

        Map<String, Object> sqlParams = new HashMap<>();

        try {

            sqlParams.put("pageNum", pageNum);

            sqlParams.put("pageSize", pageSize);

            mapsetList = dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_MAPSET_BRAPI_BYLIST, null, sqlParams);

            return mapsetList;

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
    public MapsetBrapiDTO getMapsetById(Integer mapSetId) {

        MapsetBrapiDTO returnVal = new MapsetBrapiDTO();

        ResultSet resultSet = rsMapsetBrapiDao.getMapsetByMapsetId(mapSetId);

        try {

            if(resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (SQLException e) {
            LOGGER.error("Error retrieving mapset details", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;


    }

}
