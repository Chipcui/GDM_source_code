package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 7/30/2019.
 */
public interface RsSearchQueryDao {

    ResultSet getSearchQuery(String searchResultsDbId) throws GobiiDaoException;

}
