package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public interface RsMapsetBrapiDao {

    ResultSet getMapsetByMapsetId(Integer mapsetId) throws GobiiDaoException;
}
