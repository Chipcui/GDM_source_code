package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiServerType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the web server configuration properties necessary for a given
 * crop. In addition, it contains GobiiCropDbConfig instances for the databae configurations
 * for the specific crop.
 */
@Root
public class GobiiCropConfig {

    public GobiiCropConfig(String cropType, boolean isActive) {
        this.gobiiCropType = cropType;
        this.isActive = isActive;
    }

    @Element(required = false)
    private String gobiiCropType;

    @Element(required = false)
    private boolean isActive;

    @ElementMap(required = false)
    private Map<GobiiServerType, ServerBase> serverBasesByCropServerType = new HashMap<>();

    public GobiiCropConfig() {
    }

    public void addServer(GobiiServerType gobiiServerType,
                          String host,
                          String contextPath,
                          Integer port,
                          String userName,
                          String password,
                          boolean decrypt) {

        
        ServerBase serverBase = this.serverBasesByCropServerType.get(gobiiServerType);
        if (serverBase == null) {

            serverBase = new ServerBase();
            this.serverBasesByCropServerType.put(gobiiServerType, serverBase);

        }

        serverBase
                .setGobiiServerType(gobiiServerType)
                .setHost(host)
                .setContextPath(contextPath)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password)
                .setDecrypt(decrypt);
    }

    public GobiiCropConfig setServerBasesByCropServerType(Map<GobiiServerType, ServerBase> serverBasesByCropServerType) {
        this.serverBasesByCropServerType = serverBasesByCropServerType;
        return this;
    }

    public ServerBase getServer(GobiiServerType gobiiServerType) {
        ServerBase returnVal = this.serverBasesByCropServerType.get(gobiiServerType);
        return returnVal;
    } // getServer()

    public Collection<ServerBase> getCropConfigs() {
        return this.serverBasesByCropServerType.values();
    }


    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiCropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public GobiiCropConfig setActive(boolean active) {
        isActive = active;
        return this;
    }
}
