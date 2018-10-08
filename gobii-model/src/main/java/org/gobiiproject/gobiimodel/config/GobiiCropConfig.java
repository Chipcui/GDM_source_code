package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.ServerType;
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
    private Map<ServerType, ServerBase> serversByServerType = new HashMap<>();

    public GobiiCropConfig() {
    }

    public void addServer(ServerType serverType,
                          String host,
                          String contextPath,
                          Integer port,
                          String userName,
                          String password,
                          boolean decrypt) {

        
        ServerBase serverBase = this.serversByServerType.get(serverType);
        if (serverBase == null) {

            serverBase = new ServerBase();
            this.serversByServerType.put(serverType, serverBase);

        }

        serverBase
                .setServerType(serverType)
                .setHost(host)
                .setContextPath(contextPath)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password)
                .setDecrypt(decrypt);
    }

    public GobiiCropConfig setServersByServerType(Map<ServerType, ServerBase> serversByServerType) {
        this.serversByServerType = serversByServerType;
        return this;
    }

    public ServerBase getServer(ServerType serverType) {
        ServerBase returnVal = this.serversByServerType.get(serverType);
        return returnVal;
    } // getServer()

    public Collection<ServerBase> getServers() {
        return this.serversByServerType.values();
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
