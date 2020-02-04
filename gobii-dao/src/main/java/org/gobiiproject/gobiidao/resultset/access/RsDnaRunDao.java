package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public interface RsDnaRunDao {

    ResultSet getDnaRunForDnaRunId(Integer dnaRunId) throws GobiiDaoException;

}
