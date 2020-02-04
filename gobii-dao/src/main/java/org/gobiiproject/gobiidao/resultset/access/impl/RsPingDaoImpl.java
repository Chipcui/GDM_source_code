package org.gobiiproject.gobiidao.resultset.access.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPingDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp.SpGetDbConnectionMetaData;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel on 4/27/2016.
 */
public class RsPingDaoImpl implements RsPingDao {

    Logger LOGGER = LoggerFactory.getLogger(RsPingDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, String> getPingResponses(List<String> pingRequests) throws GobiiDaoException {

        Map<String, String> returnVal = null;

        try {


            Map<String, Object> parameters = new HashMap<>();
            SpGetDbConnectionMetaData spGetDbConnectionMetaData = new SpGetDbConnectionMetaData(parameters);

            storedProcExec.doWithConnection(spGetDbConnectionMetaData);

            returnVal =
                    parameters
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(e -> e.getKey(),
                                    e -> e.toString()));


        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving role names", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    }
}
