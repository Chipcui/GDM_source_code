package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.dto.rest.RestResourceProfile;
import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

@Root
public class ServerConfig {

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

    public ServerConfig() {
    }

    public ServerConfig(ServerType serverType,
                        String host,
                        String contextPath,
                        Integer port,
                        boolean isActive,
                        String userName,
                        String password,
                        boolean decrypt,
                        EnumMap<RestResourceId, RestResourceProfile> resourceProfilesByRestRequestId) {

        this.serverType = serverType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;
        this.resourceProfilesByRestRequestId = resourceProfilesByRestRequestId;
    }

    public ServerConfig(ServerType serverType,
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

    public ServerConfig setDecrypt(boolean decrypt) {
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

    public ServerConfig setUserName(String userName) {
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

    public ServerConfig setPassword(String password) {
        this.password = password;
        return this;
    }


    public ServerType getServerType() {
        return serverType;
    }

    public ServerConfig setServerType(ServerType serverType) {
        this.serverType = serverType;
        return this;
    }

    public ServerConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public ServerConfig setPort(Integer port) {
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

    public ServerConfig setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getContextPath() {
        return this.getContextPath(true);
    }

    public String getContextPath(boolean terminate) {

        String returnVal = this.contextPath;

        if (terminate && !LineUtils.isNullOrEmpty(returnVal)) {
            returnVal = LineUtils.terminateDirectoryPath(returnVal);
        }
        return returnVal;
    }

    public ServerConfig setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }


    @ElementMap(required = false)
    EnumMap<RestResourceId, RestResourceProfile> resourceProfilesByRestRequestId = new EnumMap<>(RestResourceId.class);

    @Element(required = false)
    Integer statusCheckIntervalSecs = 0;

    @Element(required = false)
    Integer maxStatusCheckMins = 0;


    public Integer getStatusCheckIntervalSecs() {
        return statusCheckIntervalSecs;
    }

    public ServerConfig setStatusCheckIntervalSecs(Integer statusCheckIntervalSecs) {
        this.statusCheckIntervalSecs = statusCheckIntervalSecs;
        return this;
    }

    public Integer getMaxStatusCheckMins() {
        return maxStatusCheckMins;
    }

    public ServerConfig setMaxStatusCheckMins(Integer maxStatusCheckMins) {
        this.maxStatusCheckMins = maxStatusCheckMins;
        return this;
    }


    List<ServerType> nonUpdatableServerTypes = Arrays.asList(ServerType.GOBII_WEB,
            ServerType.GOBII_PGSQL,
            ServerType.GOBII_COMPUTE);


    public boolean isResourceProfileDefined(RestResourceId restResourceId, RestMethodType restMethodType) {

        return this.resourceProfilesByRestRequestId.containsKey(restResourceId)
                && this.resourceProfilesByRestRequestId.get(restResourceId).isRestMethodDefined(restMethodType);
    }

    public boolean isResourceProfileDefined(RestResourceId restResourceId, RestMethodType restMethodType, String templateParameter) {

        return this.resourceProfilesByRestRequestId.containsKey(restResourceId)
                && this.resourceProfilesByRestRequestId.get(restResourceId).isRestMethodDefined(restMethodType,templateParameter);
    }

    private RestResourceProfile getResourceProfile(RestResourceId restResourceId) {

        if (!this.resourceProfilesByRestRequestId.containsKey(restResourceId)) {
            throw new GobiiException("There is no call profile for restResourceId " + restResourceId.getResourcePath());
        }

        return this.resourceProfilesByRestRequestId.get(restResourceId);
    }

    public EnumMap<RestResourceId, RestResourceProfile> getResourceProfilesByRestRequestId() {
        return resourceProfilesByRestRequestId;
    }

    public void setResourceProfilesByRestRequestId(EnumMap<RestResourceId, RestResourceProfile> resourceProfilesByRestRequestId) {
        this.resourceProfilesByRestRequestId = resourceProfilesByRestRequestId;
    }

    public Integer getRestResourceLimit(RestResourceId restResourceId, RestMethodType restMethodType) {

        return this.getResourceProfile(restResourceId).getMethodLimit(restMethodType);
    }

    public Integer getRestResourceLimit(RestResourceId restResourceId,
                                        RestMethodType restMethodType,
                                        String templateParameter) {

        return this.getResourceProfile(restResourceId).getMethodLimit(restMethodType,
                templateParameter);
    }

    public void setRestResourceLimit(RestResourceId restResourceId,
                                        RestMethodType restMethodType,
                                        Integer max) {

        this.getResourceProfile(restResourceId).setMethodLimit(restMethodType, max);
    }

    public void setRestResourceLimit(RestResourceId restResourceId,
                                        RestMethodType restMethodType,
                                        String templateParameter,
                                        Integer max) {

        this.getResourceProfile(restResourceId).setMethodLimit(restMethodType,
                templateParameter,max);
    }

    public String getCallResourcePath(RestResourceId restResourceId) {
        return this.getResourceProfile(restResourceId).getRestResourceId().getResourcePath();
    }

    public void setCallResourcePath(RestResourceId restResourceId, String resourcePath) throws GobiiException {


        if (this.nonUpdatableServerTypes.contains(restResourceId.getServerType())) {
            throw new GobiiException("This server type does not allow dynamic configuration of resource paths: " + restResourceId.getServerType().toString());
        }

        this.getResourceProfile(restResourceId).getRestResourceId().setResourcePath(resourcePath);
    }

}
