package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.access.RsMarkerBrapiDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapMarkerBrapi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public class DtoMapMarkerBrapiImpl implements DtoMapMarkerBrapi {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMarkerBrapiImpl.class);

    @Autowired
    private RsMarkerBrapiDao rsMarkerBrapiDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Transactional
    @Override
    public MarkerBrapiDTO get(Integer markerId) throws GobiiDtoMappingException {

        MarkerBrapiDTO returnVal = new MarkerBrapiDTO();

        try {
            ResultSet resultSet = rsMarkerBrapiDao.getMarkerByMarkerId(markerId);
            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

               if (returnVal.getDatasetMarkerIndex().size() > 0) {

                    for (String dataSetId : returnVal.getDatasetMarkerIndex().keySet()) {

                        if (dataSetId != null) {
                            returnVal.getVariantSetDbId().add(Integer.parseInt(dataSetId));
                        }
                    }
                }

                if (resultSet.next()) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "Multiple resources found. Violation of unique Marker ID constraint." +
                                    " Please contact your Data Administrator to resolve this. " +
                                    "Changing underlying database schemas and constraints " +
                                    "without consulting GOBii Team is not recommended.");
                }

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Marker not found for given id.");
            }
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
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public List<MarkerBrapiDTO> getList(Integer pageToken, Integer pageNum, Integer pageSize, MarkerBrapiDTO markerBrapiDTOFilter) throws GobiiDtoMappingException {

        List<MarkerBrapiDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if (pageToken != null) {
                sqlParams.put("pageToken", pageToken);
            }

            if(pageNum != null) {
                sqlParams.put("pageNum", pageNum);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }

            if (markerBrapiDTOFilter != null) {

                if (markerBrapiDTOFilter.getVariantDbId() != null && markerBrapiDTOFilter.getVariantDbId() != 0) {
                    sqlParams.put("variantDbId", markerBrapiDTOFilter.getVariantDbId());
                }

                if (markerBrapiDTOFilter.getVariantSetDbId().size() > 0) {
                    if (markerBrapiDTOFilter.getVariantSetDbId().get(0) != 0) {
                        sqlParams.put("variantSetDbId", markerBrapiDTOFilter.getVariantSetDbId().get(0));
                    }
                }

                if (markerBrapiDTOFilter.getMapSetId() != null && markerBrapiDTOFilter.getMapSetId() != 0) {
                    sqlParams.put("mapSetId", markerBrapiDTOFilter.getMapSetId());
                }

                if (markerBrapiDTOFilter.getMapSetName() != null) {
                    sqlParams.put("mapSetName", markerBrapiDTOFilter.getMapSetName());
                }

                if (markerBrapiDTOFilter.getLinkageGroupName() != null) {
                    sqlParams.put("linkageGroupName", markerBrapiDTOFilter.getLinkageGroupName());
                }
            }

            returnVal = (List<MarkerBrapiDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_MARKER_ALL_BRAPI,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

            for(MarkerBrapiDTO currentDnaRunDTO : returnVal) {

                if (currentDnaRunDTO.getDatasetMarkerIndex().size() > 0) {

                    for (String dataSetId : currentDnaRunDTO.getDatasetMarkerIndex().keySet()) {

                        if (dataSetId != null) {
                            currentDnaRunDTO.getVariantSetDbId().add(Integer.parseInt(dataSetId));
                        }
                    }
                }
            }
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

        return returnVal;
    }

}
