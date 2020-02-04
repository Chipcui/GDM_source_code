package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 7/12/2019.
 */
public interface RsDatasetBrapiDao {

    ResultSet getDatasetByDatasetId(Integer datasetId) throws GobiiDaoException;
}
