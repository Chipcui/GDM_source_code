package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsPlatformDao {


    ResultSet getPlatformNames() throws GobiiDaoException;
    ResultSet getPlatformNamesByTypeId(Integer platformId) throws GobiiDaoException;
    ResultSet getPlatformDetailsByPlatformId(Integer platformId) throws GobiiDaoException;
    ResultSet getPlatformDetailsByVendorProtocolId(Integer vendorProtocolId) throws GobiiDaoException;
    Integer createPlatform(Map<String,Object> parameters) throws GobiiDaoException;
    void updatePlatform(Map<String, Object> parameters) throws GobiiDaoException;
    Integer createUpdatePlatformProperty(Map<String, Object> parameters) throws GobiiDaoException;
    ResultSet getProperties(Integer platformId ) throws GobiiDaoException;
}
