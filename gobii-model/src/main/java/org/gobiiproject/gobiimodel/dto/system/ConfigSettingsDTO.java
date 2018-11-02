package org.gobiiproject.gobiimodel.dto.system;

import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.ServerCapabilityType;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class ConfigSettingsDTO extends DTOBase {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {


    }

    private Map<String, ServerConfigItem> serverConfigs = new LinkedHashMap<>();

    public Map<String, ServerConfigItem> getServerConfigs() {
        return serverConfigs;
    }

    public void setServerConfigs(Map<String, ServerConfigItem> serverConfigs) {
        this.serverConfigs = serverConfigs;
    }

    private Map<ServerCapabilityType, Boolean> serverCapabilities = new HashMap<>();

    public Map<ServerCapabilityType, Boolean> getServerCapabilities() {
        return serverCapabilities;
    }

    public void setServerCapabilities(Map<ServerCapabilityType, Boolean> serverCapabilities) {
        this.serverCapabilities = serverCapabilities;
    }


    private Integer maxUploadSizeMbytes;

    public Integer getMaxUploadSizeMbytes() {
        return maxUploadSizeMbytes;
    }

    public void setMaxUploadSizeMbytes(Integer maxUploadSizeMbytes) {
        this.maxUploadSizeMbytes = maxUploadSizeMbytes;
    }
}
