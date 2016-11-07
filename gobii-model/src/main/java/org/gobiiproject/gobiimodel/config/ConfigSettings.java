package org.gobiiproject.gobiimodel.config;


import java.util.List;
import java.util.stream.Collectors;

import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates all the configuration data with which the rest of the system interacts.
 * It is intended to eliminate the need for other system components to interact directly with (or
 * even know the format of the) underlying configuration mechanism. At this time, that maechanism
 * happens to be a configuration file. In the future, it could be a database. As a general container
 * for this functionality, it delegates most of its functionality to component classes. In particular,
 * it consumes:
 * * A ConfigValues instance, which contains the actual configuration data;
 * * A ConfigValuesFactory, which knows how to create a ConfigValues instance.
 * This form of organization enables this class to function as a dependency firewall between the actual
 * format of the data and the rest of the system.
 *
 *
 */
public class ConfigSettings {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigSettings.class);


    private String configFileFqpn;

    public ConfigSettings() {
        try {
            configValues = ConfigValuesFactory.read(null);
        } catch (Exception e) {
            LOGGER.error("Error instancing ConfigValues with null fqpn", e);
        }
    }

    ConfigValues configValues = null;

    public ConfigSettings(String configFileWebPath) {

        try {
            configValues = ConfigValuesFactory.read(configFileWebPath);
            this.configFileFqpn = configFileWebPath;
        } catch (Exception e) {
            LOGGER.error("Error instancing ConfigValues with fqpn: " + configFileWebPath, e);

        }
    } // ctor

    private ConfigSettings(ConfigValues configValues) {
        this.configValues = configValues;
    }

    public static ConfigSettings makeNew(String userFqpn) throws Exception {

        ConfigSettings returnVal = null;

        ConfigValues configValues = ConfigValuesFactory.makeNew(userFqpn);
        if (configValues != null) {
            returnVal = new ConfigSettings(configValues);
            returnVal.configFileFqpn = userFqpn;
        }

        return returnVal;

    } //

    public static ConfigSettings read(String userFqpn) throws Exception {

        ConfigSettings returnVal = null;

        ConfigValues configValues = ConfigValuesFactory.read(userFqpn);
        if (configValues != null) {
            returnVal = new ConfigSettings(configValues);
            returnVal.configFileFqpn = userFqpn;
        }

        return returnVal;

    } //

    public void commit() throws Exception {
        ConfigValuesFactory.commitConfigValues(this.configValues, this.configFileFqpn);
    }

    public String getProcessingPath(String cropType, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {
        return this.configValues.getProcessingPath(cropType, gobiiFileProcessDir);
    }

    public void setCrop(String gobiiCropType,
                        boolean isActive,
                        String serviceDomain,
                        String serviceAppRoot,
                        Integer servicePort) {

        this.configValues.setCrop(gobiiCropType, isActive, serviceDomain, serviceAppRoot, servicePort);
    }

    public void removeCrop(String cropId ) throws Exception {
        this.configValues.removeCrop(cropId);
    }

    public CropConfig getCropConfig(String gobiiCropType) {

        return (this.configValues.getCropConfig(gobiiCropType));
    }

    public List<CropConfig> getActiveCropConfigs() throws Exception {

        return (this.configValues.getActiveCropConfigs());
    }

    public TestExecConfig getTestExecConfig() {

        return this.configValues.getTestExecConfig();
    }

    public void setTestExecConfig(TestExecConfig testExecConfig) {
        this.configValues.setTestExecConfig(testExecConfig);
    }

    public List<String> getActiveCropTypes() throws Exception {
        return this
                .configValues
                .getActiveCropConfigs()
                .stream()
                .filter(c -> c.isActive() == true)
                .map(CropConfig::getGobiiCropType)
                .collect(Collectors.toList());
    }

    public CropConfig getCurrentCropConfig() {

        return this.configValues.getCurrentCropConfig();
    }


    public void setCurrentGobiiCropType(String currentGobiiCropType) {
        this.configValues.setCurrentGobiiCropType(currentGobiiCropType);

    }

    public String getCurrentGobiiCropType() {
        return this.configValues.getCurrentGobiiCropType();
    }

    public String getDefaultGobiiCropType() {
        return this.configValues.getDefaultGobiiCropType();
    }


    public void setDefaultGobiiCropType(String defaultGobiiCropType) throws Exception {

        this.configValues.setDefaultGobiiCropType(defaultGobiiCropType);
    }

    public String getEmailSvrPassword() {

        return this.configValues.getEmailSvrPassword();
    }

    public String getEmailSvrHashType() {
        return this.configValues.getEmailSvrHashType();
    }

    public String getEmailSvrUser() {

        return this.configValues.getEmailSvrUser();
    }

    public String getEmailSvrDomain() {

        return this.configValues.getEmailSvrDomain();
    }

    public String getEmailSvrType() {

        return this.configValues.getEmailSvrType();
    }

    public Integer getEmailServerPort() {

        return this.configValues.getEmailServerPort();
    }

    public void setEmailSvrType(String emailSvrType) {
        this.configValues.setEmailSvrType(emailSvrType);
    }

    public void setEmailSvrDomain(String emailSvrDomain) {
        this.configValues.setEmailSvrDomain(emailSvrDomain);
    }

    public void setEmailSvrUser(String emailSvrUser) {
        this.configValues.setEmailSvrUser(emailSvrUser);
    }

    public void setEmailSvrHashType(String emailSvrHashType) {
        this.configValues.setEmailSvrHashType(emailSvrHashType);
    }

    public void setEmailSvrPassword(String emailSvrPassword) {
        this.configValues.setEmailSvrPassword(emailSvrPassword);
    }

    public void setEmailSvrPort(Integer emailServerPort) {
        this.configValues.setEmailSvrPort(emailServerPort);
    }

    public boolean isIflIntegrityCheck() {
        return this.configValues.isIflIntegrityCheck();
    }

    public void setIflIntegrityCheck(boolean iflIntegrityCheck) {
        this.configValues.setIflIntegrityCheck(iflIntegrityCheck);
    }

    public String getFileSystemRoot() {

        return this.configValues.getFileSystemRoot();
    }

    public void setFileSystemRoot(String fileSystemRoot) {

        this.configValues.setFileSystemRoot(fileSystemRoot);
    }

    public String getFileSystemLog() {
        return this.configValues.getFileSystemLog();
    }

    public void setFileSystemLog(String fileSystemLog) {
        this.configValues.setFileSystemLog(fileSystemLog);
    }

    public String getFileSysCropsParent() {

        return this.configValues.getFileSysCropsParent();
    }

    public void setFileSysCropsParent(String fileSysCropsParent) {
        this.configValues.setFileSysCropsParent(fileSysCropsParent);
    }



}
