package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDatasetBrapiDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp.SpGetDatasetByDatasetIdBrapi;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 7/12/2019.
 */
public class RsDatasetBrapiDaoImpl implements RsDatasetBrapiDao {

    Logger LOGGER = LoggerFactory.getLogger(RsDatasetBrapiDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDatasetByDatasetId(Integer datasetId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("datasetId", datasetId);
            SpGetDatasetByDatasetIdBrapi spGetDatasetByDatasetIdBrapi = new SpGetDatasetByDatasetIdBrapi(parameters);
            storedProcExec.doWithConnection(spGetDatasetByDatasetIdBrapi);
            returnVal = spGetDatasetByDatasetIdBrapi.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving dataset", e.getSQL(), e.getSQLException());
            throw new GobiiDaoException(e.getSQLException());
        }

        return returnVal;
    }
}
