package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.rest.RestProfileDTO;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;

public class RestResourceLimits {


    /***
     * If a limit for the call exists, returns the limit; otherwise returns null
     * @param restResourceId
     * @param restMethodType
     * @param templateParameter
     * @return
     */
    public static Integer getResourceLimit(RestResourceId restResourceId,
                                           RestMethodType restMethodType,
                                           String templateParameter) throws Exception {
        Integer returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();
        ServerConfig serverConfigWeb = configSettings
                .getCropConfig(CropRequestAnalyzer.getGobiiCropType())
                .getServer(ServerType.GOBII_WEB);

        if (serverConfigWeb.isResourceProfileDefined(restResourceId,
                restMethodType,
                templateParameter)) {

            returnVal = serverConfigWeb.getRestResourceLimit(
                    restResourceId,
                    restMethodType,
                    templateParameter.toUpperCase());
        }

        return returnVal;

    } //getResourceLimit()


    /***
     * If a limit for the call exists, returns the limit; otherwise returns null
     * @param restResourceId
     * @param restMethodType
     * @return
     */
    public static Integer getResourceLimit(RestResourceId restResourceId,
                                           RestMethodType restMethodType) throws Exception {
        Integer returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();
        ServerConfig serverConfigWeb = configSettings
                .getCropConfig(CropRequestAnalyzer.getGobiiCropType())
                .getServer(ServerType.GOBII_WEB);

        if (serverConfigWeb.isResourceProfileDefined(restResourceId,
                restMethodType)) {

            returnVal = serverConfigWeb.getRestResourceLimit(
                    restResourceId,
                    restMethodType);
        }

        return returnVal;

    } //getResourceLimit()

    public static void setResourceLimit(RestProfileDTO restProfileDTO) throws Exception {

        ConfigSettings configSettings = new ConfigSettings();
        ServerConfig serverConfigWeb = configSettings
                .getCropConfig(CropRequestAnalyzer.getGobiiCropType())
                .getServer(ServerType.GOBII_WEB);


        if (LineUtils.isNullOrEmpty(restProfileDTO.getTemplateParameter())) {

            if (!serverConfigWeb
                    .isResourceProfileDefined(restProfileDTO.getRestResourceId(),
                            restProfileDTO.getRestMethodType())) {

                throw new GobiiException("There is no rest resource profile defined for "
                        + restProfileDTO.getRestResourceId().getResourcePath()
                        + " (" + restProfileDTO.getRestMethodType().toString() + ")"
                );

            } else {
                serverConfigWeb.setRestResourceLimit(
                        restProfileDTO.getRestResourceId(),
                        restProfileDTO.getRestMethodType(),
                        restProfileDTO.getMax());

            } // if-else the profile is defined

        } else {

            if (!serverConfigWeb
                    .isResourceProfileDefined(restProfileDTO.getRestResourceId(),
                            restProfileDTO.getRestMethodType(),
                            restProfileDTO.getTemplateParameter())) {

                throw new GobiiException("There is no rest resource profile defined for "
                        + restProfileDTO.getRestResourceId().getResourcePath()
                        + "/"
                        + restProfileDTO.getTemplateParameter()
                        + " (" + restProfileDTO.getRestMethodType().toString() + ")"
                );

            } else {
                serverConfigWeb.setRestResourceLimit(
                        restProfileDTO.getRestResourceId(),
                        restProfileDTO.getRestMethodType(),
                        restProfileDTO.getTemplateParameter(),
                        restProfileDTO.getMax());

            } // if-else the profile is defined

        } // if-else there is a template parameter

        configSettings.commit(true);

    } //set call limit
}
