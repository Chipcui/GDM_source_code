package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.access.impl.RsDnaRunDaoImpl;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class DtoMapDnaRunImpl implements DtoMapDnaRun {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDnaRunImpl.class);

    @Autowired
    private RsDnaRunDaoImpl rsDnaRunDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Transactional
    @Override
    public DnaRunDTO get(Integer dnaRunId) throws GobiiDtoMappingException {

        DnaRunDTO returnVal = new DnaRunDTO();

        try {
            ResultSet resultSet = rsDnaRunDao.getDnaRunForDnaRunId(dnaRunId);
            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                if (resultSet.next()) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "Multiple resources found. Violation of unique Dnarun ID constraint." +
                                    " Please contact your Data Administrato to resolve this. " +
                                    "Changing underlying database schemas and constraints " +
                                    "without consulting GOBii Team is not recommended.");
                }
            }
            else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dna run not found for given id.");
            }

        }
        catch (GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE);
            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public List<DnaRunDTO> getList(Integer pageToken, Integer pageSize) throws GobiiDtoMappingException {

        List<DnaRunDTO> returnVal;
        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if (pageToken != null) {
                sqlParams.put("pageToken", pageToken);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }

            returnVal = (List<DnaRunDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DNARUN_ALL,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
}