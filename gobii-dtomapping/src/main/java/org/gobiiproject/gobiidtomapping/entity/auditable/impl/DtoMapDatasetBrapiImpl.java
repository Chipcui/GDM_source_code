package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDatasetBrapiDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapAnalysisBrapi;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapDatasetBrapi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by VCalaminos on 7/10/2019.
 */
public class DtoMapDatasetBrapiImpl implements DtoMapDatasetBrapi {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDatasetBrapiImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private RsDatasetBrapiDao rsDatasetBrapiDao;

    @Autowired
    private DtoMapAnalysisBrapi dtoMapAnalysisBrapi;

    private List<AnalysisBrapiDTO> getAnalyses(DataSetBrapiDTO dataSetBrapiDTO, List<AnalysisBrapiDTO> analysisBrapiDTOS, List<AnalysisBrapiDTO> currentAnalysisList) {

        for (Integer currentAnalysisId : dataSetBrapiDTO.getAnalysisIds()) {

            if (currentAnalysisId > 0 && !currentAnalysisId.equals(dataSetBrapiDTO.getCallingAnalysisId())) {

                List<AnalysisBrapiDTO> result = analysisBrapiDTOS.stream()
                        .filter(a -> Objects.equals(a.getAnalysisDbId(), currentAnalysisId))
                        .collect(Collectors.toList());
                if (result.size() > 0) {
                    currentAnalysisList.add(result.get(0));
                }
            }
        }

        return currentAnalysisList;
    }

    @Transactional
    @Override
    public DataSetBrapiDTO get(Integer datasetId) throws GobiiDtoMappingException {

        DataSetBrapiDTO returnVal = new DataSetBrapiDTO();

        try {
            ResultSet resultSet = rsDatasetBrapiDao.getDatasetByDatasetId(datasetId);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                if (returnVal.getCallingAnalysisId() > 0 || returnVal.getAnalysisIds().size() > 0) {

                    List<AnalysisBrapiDTO> allAnalysisBrapiDTOS = dtoMapAnalysisBrapi.getList();
                    List<AnalysisBrapiDTO> analysisBrapiDTOList = returnVal.getAnalyses();

                    if (analysisBrapiDTOList == null) {
                        analysisBrapiDTOList = new ArrayList<>();
                    }

                    if (returnVal.getCallingAnalysisId() > 0) {

                        List<AnalysisBrapiDTO> result = allAnalysisBrapiDTOS.stream()
                                .filter(a -> Objects.equals(a.getAnalysisDbId(), returnVal.getCallingAnalysisId()))
                                .collect(Collectors.toList());

                        if (result.size() > 0) {
                            analysisBrapiDTOList.add(result.get(0));
                        }
                    }

                    if (returnVal.getAnalysisIds().size() > 0) {

                        analysisBrapiDTOList = getAnalyses(returnVal, allAnalysisBrapiDTOS, analysisBrapiDTOList);
                    }

                    returnVal.setAnalyses(analysisBrapiDTOList);

                }

                if (resultSet.next()) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "Multiple resources found. Violation of unique Dataset ID constraint." +
                                    " Please contact your Data Administrator to resolve this. " +
                                    "Changing underlying database schemas and constraints " +
                                    "without consulting GOBii Team is not recommended.");
                }

            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dataset not found for given id.");
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
    public List<DataSetBrapiDTO> getList(Integer pageToken, Integer pageSize, DataSetBrapiDTO dataSetBrapiDTOFilter) throws GobiiDtoMappingException {

        List<DataSetBrapiDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if (pageToken != null) {
                sqlParams.put("pageToken", pageToken);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }

            if (dataSetBrapiDTOFilter != null) {

                if (dataSetBrapiDTOFilter.getVariantSetDbId() != null && dataSetBrapiDTOFilter.getVariantSetDbId() != 0) {
                    sqlParams.put("variantSetDbId", dataSetBrapiDTOFilter.getVariantSetDbId());
                }

                if (dataSetBrapiDTOFilter.getVariantSetName() != null) {
                    sqlParams.put("variantSetName", dataSetBrapiDTOFilter.getVariantSetName());
                }

                if (dataSetBrapiDTOFilter.getStudyDbId() != null) {
                    sqlParams.put("studyDbId", dataSetBrapiDTOFilter.getStudyDbId());
                }

            }

            returnVal = (List<DataSetBrapiDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DATASET_ALL_BRAPI,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

            List<AnalysisBrapiDTO> allAnalysisBrapiDTOS = dtoMapAnalysisBrapi.getList();

            for (DataSetBrapiDTO currentDatasetDTO : returnVal) {
                List<AnalysisBrapiDTO> analysisBrapiDTOList = currentDatasetDTO.getAnalyses();

                if (analysisBrapiDTOList == null) {
                    analysisBrapiDTOList = new ArrayList<>();
                }

                if (currentDatasetDTO.getCallingAnalysisId() > 0) {

                    List<AnalysisBrapiDTO> result = allAnalysisBrapiDTOS.stream()
                            .filter(a -> Objects.equals(a.getAnalysisDbId(), currentDatasetDTO.getCallingAnalysisId()))
                            .collect(Collectors.toList());

                    if (result.size() > 0) {
                        analysisBrapiDTOList.add(result.get(0));
                    }
                }

                if (currentDatasetDTO.getAnalysisIds().size() > 0) {

                    analysisBrapiDTOList = getAnalyses(currentDatasetDTO, allAnalysisBrapiDTOS, analysisBrapiDTOList);
                }

                currentDatasetDTO.setAnalyses(analysisBrapiDTOList);
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
