package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapAnalysisBrapi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 7/11/2019.
 */
public class DtoMapAnalysisBrapiImpl implements DtoMapAnalysisBrapi {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapAnalysisBrapiImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Transactional
    @Override
    public List<AnalysisBrapiDTO> getList() throws GobiiDaoException {

        List<AnalysisBrapiDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            returnVal = (List<AnalysisBrapiDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_ANALYSES_ALL_BRAPI,
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
