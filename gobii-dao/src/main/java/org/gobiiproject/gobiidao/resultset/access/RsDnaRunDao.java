package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public interface RsDnaRunDao {

    ResultSet getDnaRunForDnaRunId(Integer dnaRunId) throws GobiiDaoException;
    ResultSet createSearchQuery(Map<String, Object> parameters) throws GobiiDaoException;

}
