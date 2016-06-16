package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.container.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 5/13/2016.
 */
public final class ClientContext {


    private static Logger LOGGER = LoggerFactory.getLogger(TypedRestRequest.class);

    // configure as a singleton
    // this may not be effective if more thn one classloader is used
    private static ClientContext clientContext = null;

    public synchronized static void resetConfiguration() {
        clientContext = null;
    }

    public synchronized static ClientContext getInstance(String gobiiUrl, boolean initConfigFromServer) throws Exception {

        if (null == clientContext) {

            clientContext = new ClientContext();

            if (initConfigFromServer) {
                if (!LineUtils.isNullOrEmpty(gobiiUrl)) {

                    if ('/' != gobiiUrl.charAt(gobiiUrl.length()-1)) {
                        gobiiUrl = gobiiUrl + '/';
                    }

                    URL url = null;
                    try {
                        url = new URL(gobiiUrl);
                    } catch (Exception e) {
                        throw new Exception("Error retrieving server configuration due to invalid url: "
                                + e.getMessage()
                                + "; url must be in this format: http://host:port/context-root");
                    }

                    clientContext = clientContext.makeFromServer(url);

                } else {
                    throw new Exception("initConfigFromServer is specfied, but the gobiiUrl parameter is null or empty");
                }

            } else {

                LOGGER.error("The client context is being configured from properties file "
                        + " instead of from server configuration; the client will require "
                        + "binary update when configuration changes; "
                        + "set gobiiUrl and initConfigFromServer params to initialize from server");

                clientContext = clientContext.makeFromProperties();
            }

            clientContext.gobiiCropTypes.addAll(clientContext
                    .serverConfigs
                    .keySet());
        }



        return clientContext;
    }

    private ClientContext makeFromProperties() throws Exception {

        ClientContext returnVal = new ClientContext();

        ConfigSettings configSettings = new ConfigSettings();

        returnVal.defaultGobiiCropType = configSettings.getDefaultGobiiCropType();
        returnVal.currentGobiiCropType = returnVal.defaultGobiiCropType;

        for (CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

            ServerConfig currentServerConfig = new ServerConfig(currentCropConfig);

            returnVal.serverConfigs.put(currentCropConfig.getGobiiCropType(),
                    currentServerConfig);
        }

        return returnVal;
    }

    private ClientContext makeFromServer(URL url) throws Exception {

        ClientContext returnVal = new ClientContext();

        String host = url.getHost();
        String context = url.getPath();
        Integer port = url.getPort();


        if (LineUtils.isNullOrEmpty(host)) {
            throw new Exception("The specified URL does not contain a host name: " + url.toString());
        }

        if (LineUtils.isNullOrEmpty(context)) {
            throw new Exception("The specified URL does not specify the context path for the Gobii server : " + url.toString());
        }

        if (port <= 0) {
            throw new Exception("The specified URL does not contain a valid port id: " + url.toString());
        }

        // first authenticate
        // you can't use login() from here -- it assumes that ClientContext has already been constructed
        String authPath = Urls.getRequestUrl(ControllerType.EXTRACTOR,
                ServiceRequestId.URL_AUTH,
                context);
        HttpCore httpCore = new HttpCore(host, port);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        returnVal.userToken = httpCore.getTokenForUser(authPath, userDetail.getUserName(), userDetail.getPassword());

        // now get the settings
        String settingsPath = Urls.getRequestUrl(ControllerType.LOADER,
                ServiceRequestId.URL_CONFIGSETTINGS,
                context);
        ConfigSettingsDTO configSettingsDTORequest = new ConfigSettingsDTO();
        TypedRestRequest<ConfigSettingsDTO> typedRestRequest = new TypedRestRequest<>(host, port, ConfigSettingsDTO.class);
        ConfigSettingsDTO configSettingsDTOResponse = typedRestRequest.getTypedHtppResponseForDto(settingsPath,
                configSettingsDTORequest,
                returnVal.userToken);

        if (configSettingsDTOResponse.getDtoHeaderResponse().isSucceeded()) {

            returnVal.defaultGobiiCropType = configSettingsDTOResponse.getDefaultCrop();
            returnVal.serverConfigs = configSettingsDTOResponse.getServerConfigs();

        } else {
            throw new Exception("Unable to get server configuration from "
                    + url.toString()
                    + configSettingsDTOResponse.getDtoHeaderResponse().getStatusMessages().get(0).getMessage());
        }

        return returnVal;
    }


    private ClientContext() throws Exception {


    }

    public enum ProcessMode {Asynch, Block}

    private Map<GobiiCropType, ServerConfig> serverConfigs = new HashMap<>();


    private ProcessMode processMode = ProcessMode.Asynch;
    private String userToken = null;

    GobiiCropType currentGobiiCropType = GobiiCropType.TEST;

    GobiiCropType defaultGobiiCropType = GobiiCropType.TEST;
    List<GobiiCropType> gobiiCropTypes = new ArrayList<>();


    public String getCurrentCropDomain() {
        return serverConfigs.get(this.currentGobiiCropType).getDomain();
    }

    public String getCurrentCropContextRoot() {
        return serverConfigs.get(this.currentGobiiCropType).getContextRoot();
    }


    public Integer getCurrentCropPort() {
        return serverConfigs.get(this.currentGobiiCropType).getPort();
    }


    public List<GobiiCropType> getCropTypeTypes() {
        return gobiiCropTypes;
    }


    public GobiiCropType getCurrentClientCropType() {
        return this.currentGobiiCropType;
    }

    public void setCurrentClientCrop(GobiiCropType currentClientCrop) {
        this.currentGobiiCropType = currentClientCrop;
    }

    public GobiiCropType getDefaultCropType() {
        return this.defaultGobiiCropType;
    }

    public String getFileLocationOfCurrenCrop(GobiiFileLocationType gobiiFileLocationType) {
        return this.serverConfigs.get(this.currentGobiiCropType).getFileLocations().get(gobiiFileLocationType);
    }

    public boolean login(String userName, String password) throws Exception {
        boolean returnVal = true;

        try {
            String authUrl = Urls.getRequestUrl(ControllerType.EXTRACTOR,
                    ServiceRequestId.URL_AUTH);

            HttpCore httpCore = new HttpCore(this.getCurrentCropDomain(),
                    this.getCurrentCropPort());

            userToken = httpCore.getTokenForUser(authUrl, userName, password);
        } catch (Exception e) {
            LOGGER.error("Authenticating", e);
            throw new Exception(e);
        }

        return returnVal;
    }

    public String getUserToken() {
        return userToken;
    }


}
