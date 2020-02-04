package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerBrapiDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp.SpGetMarkerByMarkerIdBrapi;
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
 * Created by VCalaminos on 7/7/2019.
 */
public class RsMarkerBrapiDaoImpl implements RsMarkerBrapiDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMarkerBrapiDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkerByMarkerId(Integer markerId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerId", markerId);
            SpGetMarkerByMarkerIdBrapi spGetMarkerByMarkerIdBrapi = new SpGetMarkerByMarkerIdBrapi(parameters);
            storedProcExec.doWithConnection(spGetMarkerByMarkerIdBrapi);
            returnVal = spGetMarkerByMarkerIdBrapi.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving MapSet", e.getSQL(), e.getSQLException());
            throw new GobiiDaoException(e.getSQLException());
        }

        return returnVal;
    }

}
