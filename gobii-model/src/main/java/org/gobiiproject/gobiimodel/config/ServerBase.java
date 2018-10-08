package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

@Root
public class ServerBase {

    @Element(required = false)
    private boolean decrypt = false;

    @Element(required = false)
    private String userName = null;

    @Element(required = false)
    private String password = null;

    @Element(required = false)
    private ServerType serverType = null;

    @Element(required = false)
    private String host = "";

    @Element(required = false)
    private String contextPath = "";

    @Element(required = false)
    private Integer port = 0;

    @Element(required = false)
    private boolean isActive = false;

    public ServerBase() {
    }

    public ServerBase(ServerType serverType,
                      String host,
                      String contextPath,
                      Integer port,
                      boolean isActive,
                      String userName,
                      String password,
                      boolean decrypt) {

        this.serverType = serverType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;

    }

    public ServerBase(ServerType serverType,
                      String host,
                      String contextPath,
                      Integer port,
                      boolean isActive,
                      boolean decrypt) {

        this.serverType = serverType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.decrypt = decrypt;

    }

    public boolean isDecrypt() {
        return decrypt;
    }

    public ServerBase setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
        return this;
    }

    public String getUserName() {

        String returnVal = null;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.userName, null);
        } else {
            returnVal = this.userName;
        }

        return returnVal;
    }

    public ServerBase setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {

        String returnVal = null;

        if (this.decrypt) {
            returnVal = Decrypter.decrypt(this.password, null);
        } else {
            returnVal = this.password;
        }

        return returnVal;
    }

    public ServerBase setPassword(String password) {
        this.password = password;
        return this;
    }


    public ServerType getServerType() {
        return serverType;
    }

    public ServerBase setServerType(ServerType serverType) {
        this.serverType = serverType;
        return this;
    }

    public ServerBase setHost(String host) {
        this.host = host;
        return this;
    }

    public ServerBase setPort(Integer port) {
        this.port = port;
        return this;
    }

   public Integer getPort() {
        return port;
    }


    public String getHost() {

        return host;
    }

    public boolean isActive() {
        return isActive;
    }

    public ServerBase setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getContextPath() {
        return this.getContextPath(true);
    }

    public String getContextPath(boolean terminate) {

        String returnVal = this.contextPath;

        if( terminate && ! LineUtils.isNullOrEmpty(returnVal)) {
            returnVal = LineUtils.terminateDirectoryPath(returnVal);
        }
        return returnVal;
    }

    public ServerBase setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }



    // These are in the general base class only temporarily until these KDC properties
    // can be handled differently

    public enum KDCResource {
        QC_START,
        QC_STATUS_,
        QC_DOWNLOAD,
        QC_PURGE
    }

    @ElementMap(required = false)
    Map<ServerBase.KDCResource, String> kdcResources = new HashMap<>();

    @Element(required = false)
    Integer statusCheckIntervalSecs = 0;

    @Element(required = false)
    Integer maxStatusCheckMins = 0;


    public ServerBase addPath(ServerBase.KDCResource kdcResource, String resource) {
        this.kdcResources.put(kdcResource, resource);
        return this;
    }

    public String getPath(ServerBase.KDCResource kdcResource) {

        String returnVal = null;

        if (this.kdcResources.containsKey(kdcResource)) {
            returnVal = this.kdcResources.get(kdcResource);
        }

        return returnVal;
    }

    public Integer getStatusCheckIntervalSecs() {
        return statusCheckIntervalSecs;
    }

    public ServerBase setStatusCheckIntervalSecs(Integer statusCheckIntervalSecs) {
        this.statusCheckIntervalSecs = statusCheckIntervalSecs;
        return this;
    }

    public Integer getMaxStatusCheckMins() {
        return maxStatusCheckMins;
    }

    public ServerBase setMaxStatusCheckMins(Integer maxStatusCheckMins) {
        this.maxStatusCheckMins = maxStatusCheckMins;
        return this;
    }
}
