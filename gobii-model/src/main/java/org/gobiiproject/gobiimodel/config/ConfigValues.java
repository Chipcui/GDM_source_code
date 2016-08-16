package org.gobiiproject.gobiimodel.config;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Phil on 5/5/2016.
 */
class ConfigValues {

    @ElementList
    List<CropConfig> cropConfigsToSerialize = new ArrayList<>();

    private Map<GobiiCropType, CropConfig> cropConfigs = new HashMap<>();

    private GobiiCropType currentGobiiCropType = GobiiCropType.TEST;

    @Element
    private GobiiCropType defaultGobiiCropType = GobiiCropType.TEST; // default crop

    @Element
    private String emailSvrType;

    @Element
    private String emailSvrDomain;

    @Element
    private String emailSvrUser;

    @Element
    private String emailSvrHashType;

    @Element
    private String emailSvrPassword;

    @Element
    private Integer emailServerPort = 0;

    @Element
    private boolean iflIntegrityCheck = false;

    @Element
    private String fileSystemRoot;


    public CropConfig getCropConfig(GobiiCropType gobiiCropType) {

        CropConfig returnVal = null;
        returnVal = getCropConfigs().get(gobiiCropType);

        return returnVal;
    }

    public List<CropConfig> getActiveCropConfigs() {
        return getCropConfigs()
                .values()
                .stream()
                .filter(c -> c.isActive())
                .collect(Collectors.toList());
    }

    public CropConfig getCurrentCropConfig() {
        return getCropConfig(getCurrentGobiiCropType());
    }


    public void setCurrentGobiiCropType(GobiiCropType currentGobiiCropType) {
        this.currentGobiiCropType = currentGobiiCropType;

    }

    public GobiiCropType getCurrentGobiiCropType() {
        return currentGobiiCropType;
    }

    public GobiiCropType getDefaultGobiiCropType() {
        return defaultGobiiCropType;
    }


    public void setDefaultGobiiCropType(GobiiCropType defaultGobiiCropType) {
        this.defaultGobiiCropType = defaultGobiiCropType;
    }

    public List<CropConfig> getCropConfigsToSerialize() {
        return cropConfigsToSerialize;
    }

    public void setCropConfigsToSerialize(List<CropConfig> cropConfigsToSerialize) {
        this.cropConfigsToSerialize = cropConfigsToSerialize;
    }

    public Map<GobiiCropType, CropConfig> getCropConfigs() {

        if (null == cropConfigs) {

            cropConfigs = new HashMap<>();

        }

        if (0 == cropConfigs.size()){
            for (CropConfig currentCropConfig : cropConfigsToSerialize) {
                cropConfigs.put(currentCropConfig.getGobiiCropType(), currentCropConfig);
            }
        }

        return cropConfigs;
    }

    public void setCropConfigs(Map<GobiiCropType, CropConfig> cropConfigs) {
        this.cropConfigs = cropConfigs;
    }

    public String getEmailSvrType() {
        return emailSvrType;
    }

    public void setEmailSvrType(String emailSvrType) {
        this.emailSvrType = emailSvrType;
    }

    public String getEmailSvrDomain() {
        return emailSvrDomain;
    }

    public void setEmailSvrDomain(String emailSvrDomain) {
        this.emailSvrDomain = emailSvrDomain;
    }

    public String getEmailSvrUser() {
        return emailSvrUser;
    }

    public void setEmailSvrUser(String emailSvrUser) {
        this.emailSvrUser = emailSvrUser;
    }

    public String getEmailSvrHashType() {
        return emailSvrHashType;
    }

    public void setEmailSvrHashType(String emailSvrHashType) {
        this.emailSvrHashType = emailSvrHashType;
    }

    public String getEmailSvrPassword() {
        return emailSvrPassword;
    }

    public void setEmailSvrPassword(String emailSvrPassword) {
        this.emailSvrPassword = emailSvrPassword;
    }

    public Integer getEmailServerPort() {
        return emailServerPort;
    }

    public void setEmailSvrPort(Integer emailServerPort) {
        this.emailServerPort = emailServerPort;
    }

    public boolean isIflIntegrityCheck() {
        return iflIntegrityCheck;
    }

    public void setIflIntegrityCheck(boolean iflIntegrityCheck) {
        this.iflIntegrityCheck = iflIntegrityCheck;
    }

    public String getFileSystemRoot() {
        return fileSystemRoot;
    }

    public void setFileSystemRoot(String fileSystemRoot) {
        this.fileSystemRoot = fileSystemRoot;
    }

}
