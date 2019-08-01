package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsSearchQueryDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp.SpGetSearchQuery;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 7/30/2019.
 */
public class RsSearchQueryDaoImpl implements RsSearchQueryDao {

    Logger LOGGER = LoggerFactory.getLogger(RsSearchQueryDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    public ResultSet getSearchQuery(String searchResultsDbId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("searchResultsDbId", searchResultsDbId);
            SpGetSearchQuery spGetSearchQuery = new SpGetSearchQuery(parameters);
            storedProcExec.doWithConnection(spGetSearchQuery);
            returnVal = spGetSearchQuery.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error retrieving search query", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

}
