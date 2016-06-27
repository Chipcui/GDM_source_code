package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Phil on 5/25/2016.
 */
public class DataSourceSelector extends AbstractRoutingDataSource {


    // http://stackoverflow.com/questions/958593/how-do-i-gain-access-to-the-data-source-in-spring
    Logger LOGGER = LoggerFactory.getLogger(DataSourceSelector.class);


    private ThreadLocal<HttpServletRequest> currentRequest;


    @Override
    protected Object determineCurrentLookupKey() {

        Object returnVal = null;

        try {
//            HttpServletRequest request = currentRequest.get();
//            CropRequestAnalyzer cropRequestAnalyzer = new CropRequestAnalyzer(request);
//            cropRequestAnalyzer.getGobiiCropType().toString();
            return GobiiCropType.RICE.toString();

        }
        catch( Exception e) {
            LOGGER.error("Exception analysizing gobii crop type", e);

        }

        return returnVal;
    }

    public ThreadLocal<HttpServletRequest> getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(ThreadLocal<HttpServletRequest> currentRequest) {
        this.currentRequest = currentRequest;
    }
}
