package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiCropServerType;
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
public class GobiiCropConfig extends ServerBase {


    @Element(required = false)
    private String gobiiCropType;

    @ElementMap(required = false)
    private Map<GobiiCropServerType, ServerBase> serverBasesByCropServerType = new HashMap<>();

    public GobiiCropConfig() {
    }

    public GobiiCropConfig(String gobiiCropType,
                           String host,
                           String contextPath,
                           Integer port,
                           boolean isActive,
                           boolean decrypt) {

        super(GobiiCropServerType.WEB, host, contextPath, port, isActive, decrypt);
        this.gobiiCropType = gobiiCropType;
    }

    public void setServerBase(GobiiCropServerType gobiiCropServerType,
                              String host,
                              String dbName,
                              Integer port,
                              String userName,
                              String password) {

        ServerBase serverBase = this.serverBasesByCropServerType.get(gobiiCropServerType);
        if (serverBase == null) {

            serverBase = new ServerBase();
            this.serverBasesByCropServerType.put(gobiiCropServerType, serverBase);

        }

        serverBase
                .setGobiiCropServerType(gobiiCropServerType)
                .setHost(host)
                .setContextPath(dbName)
                .setPort(port)
                .setUserName(userName)
                .setPassword(password);
    }

    public GobiiCropConfig setHost(String host) {
        super.setHost(host);
        return this;
    }

    public GobiiCropConfig setPort(Integer port) {
        super.setPort(port);
        return this;
    }

    public GobiiCropConfig setServerBasesByCropServerType(Map<GobiiCropServerType, ServerBase> serverBasesByCropServerType) {
        this.serverBasesByCropServerType = serverBasesByCropServerType;
        return this;
    }

    public GobiiCropConfig setActive(boolean active) {
        super.setActive(active);
        return this;
    }


    public GobiiCropConfig setContextPath(String contextPath) {
        super.setContextPath(contextPath);
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiCropConfig setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public void addCropDbConfig(GobiiCropServerType gobiiCropServerTypee, GobiiCropDbConfig gobiiCropDbConfig) {
        serverBasesByCropServerType.put(gobiiCropServerTypee, gobiiCropDbConfig);

    } // addCropDbConfig()

    public ServerBase getServerBase(GobiiCropServerType gobiiCropServerType) {
        ServerBase returnVal = this.serverBasesByCropServerType.get(gobiiCropServerType);
        return returnVal;
    } // getServerBase()

    public Collection<ServerBase> getCropConfigs() {
        return this.serverBasesByCropServerType.values();
    }

}
