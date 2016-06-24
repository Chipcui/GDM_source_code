package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Phil on 5/25/2016.
 */
public class DataSourceSelector extends AbstractRoutingDataSource {

    Logger LOGGER = LoggerFactory.getLogger(DataSourceSelector.class);
    private ThreadLocal<HttpServletRequest> currentRequest;

    @Override
    protected Object determineCurrentLookupKey() {

        Object returnVal = null;

        try {

            HttpServletRequest servletRequest = currentRequest.get();

            CropRequestAnalyzer.getGobiiCropType(servletRequest).toString();

        }
        catch( Exception e) {
            LOGGER.error("Exception analysizing gobii crop type", e);

        }

        return returnVal;
    }
}
