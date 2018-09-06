package org.gobiiproject.gobiiweb.spring;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.types.GobiiCropServerType;
import org.gobiiproject.gobiiweb.DataSourceSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 8/16/2016.
 */

@Configuration
public class ConfigSupplement {

    @Autowired
    private ThreadLocal<HttpServletRequest> currentRequest;


    @Bean(name="dataSourceMulti")
    public DataSourceSelector dataSourceMulti() throws Exception {

        DataSourceSelector returnVal = new DataSourceSelector();
        returnVal.setCurrentRequest(currentRequest);

        ConfigSettings configSettings = new ConfigSettings();
        Map<Object,Object> targetDataSources = new HashMap<>();
        for (GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs()) {

            DriverManagerDataSource currentDataSource = new DriverManagerDataSource();

            currentDataSource.setDriverClassName("org.postgresql.Driver");

            currentDataSource.setUrl(currentGobiiCropConfig
                    .getCropDbConfig(GobiiCropServerType.POSTGRESQL)
                    .getJdbcConnectionString());

            currentDataSource.setUsername(currentGobiiCropConfig
                    .getCropDbConfig(GobiiCropServerType.POSTGRESQL)
                    .getUserName());

            currentDataSource.setUsername(currentGobiiCropConfig
                    .getCropDbConfig(GobiiCropServerType.POSTGRESQL)
                    .getUserName());


            currentDataSource.setPassword(currentGobiiCropConfig
                    .getCropDbConfig(GobiiCropServerType.POSTGRESQL)
                    .getPassword());

            targetDataSources.put(currentGobiiCropConfig.getGobiiCropType(),currentDataSource);

        } // iterate crop configs

        returnVal.setTargetDataSources(targetDataSources);

        return returnVal;

    }


}
