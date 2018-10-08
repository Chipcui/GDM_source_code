package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiimodel.types.GobiiServerType;
import org.gobiiproject.gobiimodel.types.GobiiFileNoticeType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class is essentially a POJO for the configuration data. It has a small
 * amount of logic for retrieval of directories. IN particuarl, given a GobiiFileProcessDir
 * value, it will provide the appropriate path for specific file processing locations. The properties
 * of this class are annotated with the simpleframework XML annotations for the purpose of
 * serialization. Aside from the collection of GobiiCropConfig instances, the proeprties of this class
 * are global to the configuration. There will be one GobiiCropConfig instance for every crop supported
 * by a given deployment.
 */
class
ConfigValues {


//    public ConfigValues() {
    //Declaritively add new specs here
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.RAW_USER_FILES,"files/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.HDF5_FILES, "hdf5/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.LOADER_INSTRUCTIONS, "loader/instructions/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES, "loader/digest/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.LOADER_INPROGRESS_FILES, "loader/inprogress/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.LOADER_DONE, "loader/done/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, "extractor/instructions/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_INPROGRESS, "extractor/inprogress/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_DONE, "extractor/done/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_OUTPUT, "extractor/output/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.QC_OUTPUT, "loader/qc/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.NOTICES, "notices/",true));
//        directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.GQL_PROCESS,"gql",true));
//          directorySpecList.add(new DirectorySpec(GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE, "extractors/postgres/gobii_mde/", false) );
//    }

    public ConfigValues() {
        this.globalServersByServerType.put(GobiiServerType.KDC,
                new ServerBase(GobiiServerType.KDC, "", "", null, true, "", "", false)
        );
        this.globalServersByServerType.put(GobiiServerType.OWN_CLOUD,
                new ServerBase(GobiiServerType.OWN_CLOUD, "", "", null, true, "", "", false)
        );
    } // ctor

    @Element(required = false)
    private TestExecConfig testExecConfig = new TestExecConfig();

    @ElementMap(required = false)
    private Map<GobiiServerType, ServerBase> globalServersByServerType = new HashMap<>();

    @ElementMap(required = false)
    private Map<GobiiFileNoticeType, String> noticeFileNames = new EnumMap<GobiiFileNoticeType, String>(GobiiFileNoticeType.class) {{

        put(GobiiFileNoticeType.CONFIDENTIALITY, "confidentiality.txt");

    }};

    @ElementMap(required = false)
    private Map<String, GobiiCropConfig> cropConfigs = new LinkedHashMap<>();

    @ElementMap(required = false)
    private Map<GobiiFileProcessDir, DirectorySpec> relativePaths = new EnumMap<GobiiFileProcessDir, DirectorySpec>(GobiiFileProcessDir.class) {{

        List<DirectorySpec> listOfSpecs = new ArrayList<>(Arrays.asList(
                new DirectorySpec(GobiiFileProcessDir.RAW_USER_FILES, "files/", true),
                new DirectorySpec(GobiiFileProcessDir.HDF5_FILES, "hdf5/", true),
                new DirectorySpec(GobiiFileProcessDir.LOADER_INSTRUCTIONS, "loader/instructions/", true),
                new DirectorySpec(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES, "loader/digest/", true),
                new DirectorySpec(GobiiFileProcessDir.LOADER_INPROGRESS_FILES, "loader/inprogress/", true),
                new DirectorySpec(GobiiFileProcessDir.LOADER_DONE, "loader/done/", true),
                new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, "extractor/instructions/", true),
                new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_INPROGRESS, "extractor/inprogress/", true),
                new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_DONE, "extractor/done/", true),
                new DirectorySpec(GobiiFileProcessDir.EXTRACTOR_OUTPUT, "extractor/output/", true),
                new DirectorySpec(GobiiFileProcessDir.QC_OUTPUT, "loader/qc/", true),
                new DirectorySpec(GobiiFileProcessDir.NOTICES, "notices/", true),
                new DirectorySpec(GobiiFileProcessDir.GQL_PROCESS, "gql", true),
                new DirectorySpec(GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE, "extractors/postgres/gobii_mde/", false)
        ));

        for (DirectorySpec currentSpec : listOfSpecs) {
            put(currentSpec.getGobiiFileProcessDir(), currentSpec);
        }

    }};

    private String currentGobiiCropType;

    @Element(required = false)
    private String emailSvrType;

    @Element(required = false)
    private String emailSvrDomain;

    @Element(required = false)
    private String emailSvrUser;

    @Element(required = false)
    private String emailSvrHashType;

    @Element(required = false)
    private String emailSvrPassword;

    @Element(required = false)
    private Integer emailServerPort = 0;

    @Element(required = false)
    private GobiiAuthenticationType gobiiAuthenticationType = GobiiAuthenticationType.TEST;

    @Element(required = false)
    private String ldapUserDnPattern;

    @Element(required = false)
    private String ldapUrl;

    @Element(required = false)
    private String ldapBindUser;

    @Element(required = false)
    private String ldapBindPassword;

    @Element(required = false)
    private boolean iflIntegrityCheck = false;

    @Element(required = false)
    private String fileSystemRoot;

    @Element(required = false)
    private String fileSysCropsParent = "crops/";

    @Element(required = false)
    private String fileSystemLog;

    @Element(required = false)
    private boolean isDecrypt = false;

    @Element(required = false)
    private String ldapUserForBackendProcs;

    @Element(required = false)
    private String ldapPasswordForBackendProcs;

    @Element(required = false)
    private boolean isAuthenticateBrapi = true;

    @Element(required = false)
    private Integer maxUploadSizeMbytes = 10;

    @Element(required = false)
    private boolean isProvidesBackend = true;

    public TestExecConfig getTestExecConfig() {
        return testExecConfig;
    }

    public ServerBase getGlobalServer(GobiiServerType gobiiServerType) throws Exception {

        ServerBase returnVal = null;

        if (this.globalServersByServerType.containsKey(gobiiServerType)) {

            returnVal = this.globalServersByServerType.get(gobiiServerType);
        }

        return returnVal;
    }

    public void setTestExecConfig(TestExecConfig testExecConfig) {
        this.testExecConfig = testExecConfig;
    }

    public boolean isCropDefined(String gobiiCropType) {
        return this.getCropConfigs().containsKey(gobiiCropType);
    }

    public GobiiCropConfig getCropConfig(String gobiiCropType) throws Exception {

        GobiiCropConfig returnVal = null;

        if (!getCropConfigs().containsKey(gobiiCropType)) {
            throw new Exception("There is no configuration defined for crop " + gobiiCropType);
        }

        returnVal = getCropConfigs().get(gobiiCropType);

        return returnVal;
    }


    private DirectorySpec getDirectorySpec(GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        DirectorySpec returnVal = null;


        List<DirectorySpec> directorySpecList = relativePaths.values()
                .stream()
                .filter(directorySpec -> directorySpec.getGobiiFileProcessDir().equals(gobiiFileProcessDir))
                .collect(Collectors.toList());

        if (directorySpecList.size() > 0) {
            returnVal = directorySpecList.get(0);
        }

        return returnVal;
    }

    public List<String> getLegalUpdloadDIrectories(String cropType) throws Exception {

        List<String> returnVal = new ArrayList<>();

        for (DirectorySpec currentDirectorySpec : relativePaths.values()) {

            if (currentDirectorySpec.isCropRelative) {
                String currentDirectory = this.getFullyQualifiedFilePath(cropType, currentDirectorySpec.getGobiiFileProcessDir());
                returnVal.add(currentDirectory);
            }

        }

        return returnVal;
    }

    /***
     * Retrieves the fully qualified path as identified by the GobiiFileProcessDir value. If the directory
     * is intended to be relative to a crop directory, the path will be so constructed, as long as a valid
     * cropType is supplied.
     * @param cropType
     * @param gobiiFileProcessDir
     * @return
     * @throws Exception
     */
    public String getFullyQualifiedFilePath(String cropType, GobiiFileProcessDir gobiiFileProcessDir) throws Exception {

        String returnVal;


        DirectorySpec directorySpec = this.getDirectorySpec(gobiiFileProcessDir);
        if (directorySpec == null) {
            throw new GobiiException("There is no directory entry for directory " + gobiiFileProcessDir.toString());
        }

        if (directorySpec.isCropRelative()) {

            if (cropType == null) {
                throw new Exception("The crop type specified for for crop-relative directory " + directorySpec.getGobiiFileProcessDir().toString() + " is null");
            }

            if (!cropConfigs.containsKey(cropType)) {
                throw new Exception("Unknown crop type: " + cropType);
            }

            String cropRoot = LineUtils.terminateDirectoryPath(this.getFileSysCropsParent());
            String crop = LineUtils.terminateDirectoryPath(cropType);
            String relativePatFromList = directorySpec.getRelativePath();
            String relativePath = LineUtils.terminateDirectoryPath(relativePatFromList);

            returnVal = cropRoot + crop + relativePath;

        } else {

            String rootPath = LineUtils.terminateDirectoryPath(this.getFileSystemRoot());
            String relativePatFromList = directorySpec.getRelativePath();
            String relativePath = LineUtils.terminateDirectoryPath(relativePatFromList);
            returnVal = rootPath + relativePath;
        } // if-else directory is relative to crop


        returnVal = returnVal.toLowerCase();

        return returnVal;
    } //

    public String getFileNoticePath(String cropType, GobiiFileNoticeType gobiiFileNoticeType) throws Exception {

        String returnVal = this.getFullyQualifiedFilePath(cropType, GobiiFileProcessDir.NOTICES)
                + this.noticeFileNames.get(gobiiFileNoticeType);

        return returnVal;
    }

    public List<GobiiCropConfig> getActiveCropConfigs() throws Exception {


        return getCropConfigs()
                .values()
                .stream()
                .filter(c -> c.isActive())
                .collect(Collectors.toList());
    }

    public GobiiCropConfig getCurrentCropConfig() throws Exception {
        return getCropConfig(getCurrentGobiiCropType());
    }


    public void setCurrentGobiiCropType(String currentGobiiCropType) {
        this.currentGobiiCropType = currentGobiiCropType;

    }

    public String getCurrentGobiiCropType() {
        return currentGobiiCropType;
    }


    public Map<String, GobiiCropConfig> getCropConfigs() {

        return this.cropConfigs;

    }

    public void setCropConfigs(Map<String, GobiiCropConfig> cropConfigs) {

        for (Map.Entry<String, GobiiCropConfig> entry : cropConfigs.entrySet()) {
            String lowerCaseCropType = entry.getValue().getGobiiCropType();
            entry.getValue().setGobiiCropType(lowerCaseCropType);
            this.cropConfigs.put(lowerCaseCropType, entry.getValue());
        }
    }

    public void setCrop(String gobiiCropType,
                        boolean isActive,
                        String serviceDomain,
                        String serviceAppRoot,
                        Integer servicePort) throws Exception {

        gobiiCropType = gobiiCropType.toLowerCase();

        GobiiCropConfig gobiiCropConfig;
        if (this.isCropDefined(gobiiCropType)) {
            gobiiCropConfig = this.getCropConfig(gobiiCropType);
        } else {
            gobiiCropConfig = new GobiiCropConfig();
            this.cropConfigs.put(gobiiCropType, gobiiCropConfig);
        }

        gobiiCropConfig
                .setGobiiCropType(gobiiCropType)
                .setActive(isActive)
                .addServer(GobiiServerType.GOBII_WEB,
                        serviceDomain,
                        serviceAppRoot,
                        servicePort,
                        null,
                        null,
                        false);
    }

    public void removeCrop(String cropId) throws Exception {

        if (!cropConfigs.containsKey(cropId)) {
            throw new Exception("The specified crop cannot be removed because it does not exist: " + cropId);
        }


        if ((!LineUtils.isNullOrEmpty(getTestExecConfig().getTestCrop())) &&
                getTestExecConfig().getTestCrop().equals(cropId)) {

            throw new Exception("Unable to remove crop " + cropId + " because it is the crop used for testing");
        }

        cropConfigs.remove(cropId);

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

        String returnVal;

        if (this.isDecrypt) {

            returnVal = Decrypter.decrypt(this.emailSvrUser, null);

        } else {

            returnVal = this.emailSvrUser;

        }

        return returnVal;
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

        String returnVal;

        if (this.isDecrypt) {

            returnVal = Decrypter.decrypt(this.emailSvrPassword, null);

        } else {

            returnVal = this.emailSvrPassword;

        }

        return returnVal;

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

    public GobiiAuthenticationType getGobiiAuthenticationType() {
        return gobiiAuthenticationType;
    }

    public void setGobiiAuthenticationType(GobiiAuthenticationType gobiiAuthenticationType) {
        this.gobiiAuthenticationType = gobiiAuthenticationType;
    }

    public String getLdapUserDnPattern() {
        return ldapUserDnPattern;
    }

    public void setLdapUserDnPattern(String ldapUserDnPattern) {
        this.ldapUserDnPattern = ldapUserDnPattern;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapBindUser() {

        String returnVal;

        if (this.isDecrypt) {

            returnVal = Decrypter.decrypt(this.ldapBindUser, null);

        } else {

            returnVal = this.ldapBindUser;

        }

        return returnVal;
    }

    public void setLdapBindUser(String ldapBindUser) {
        this.ldapBindUser = ldapBindUser;
    }

    public String getLdapBindPassword() {

        String returnVal;

        if (this.isDecrypt) {

            returnVal = Decrypter.decrypt(this.ldapBindPassword, null);

        } else {

            returnVal = this.ldapBindPassword;

        }

        return returnVal;

    }

    public void setLdapBindPassword(String ldapBindPassword) {
        this.ldapBindPassword = ldapBindPassword;
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

    public String getFileSystemLog() {
        return fileSystemLog;
    }

    public void setFileSystemLog(String fileSystemLog) {
        this.fileSystemLog = fileSystemLog;
    }

    public String getFileSysCropsParent() {

        String returnVal = LineUtils.terminateDirectoryPath(this.fileSystemRoot);
        returnVal += LineUtils.terminateDirectoryPath(this.fileSysCropsParent);
        return returnVal;
    }

    public void setFileSysCropsParent(String fileSysCropsParent) {
        this.fileSysCropsParent = fileSysCropsParent;
    }

    public boolean isDecrypt() {
        return isDecrypt;
    }

    // note that we set isDecrypt as a global property in ConfigValues and in \
    // each db config because the child db config objects don't have access to the
    // parent property. Moreover, in all cases, we only decrypt in the getters.
    // This is vital because there are multiple times that the config utility's options
    // are set and if we decrypt when we set the values, the decrypt flag would have to have
    // been set at the beginning of setting the config options, which is not a constraint we want
    // to impose.
    public void setDecrypt(boolean isDecrypt) {

        this.isDecrypt = isDecrypt;
        this.testExecConfig.setDecrypt(isDecrypt);

        for (GobiiCropConfig currentGobiiCropConfig : this.cropConfigs.values()) {

            for (ServerBase currentServerBase : currentGobiiCropConfig.getServers()) {
                currentServerBase.setDecrypt(isDecrypt);
            }
        }

        for( ServerBase currentServerBase : this.globalServersByServerType.values() ) {
            currentServerBase.setDecrypt(isDecrypt);
        }
    }

    public String getLdapUserForBackendProcs() {

        String returnVal;

        if (this.isDecrypt) {
            returnVal = Decrypter.decrypt(ldapUserForBackendProcs, null);
        } else {
            returnVal = ldapUserForBackendProcs;
        }

        return returnVal;
    }

    public void setLdapUserForBackendProcs(String ldapUserForBackendProcs) {
        this.ldapUserForBackendProcs = ldapUserForBackendProcs;
    }

    public String getLdapPasswordForBackendProcs() {
        String returnVal;

        if (this.isDecrypt) {
            returnVal = Decrypter.decrypt(ldapPasswordForBackendProcs, null);
        } else {
            returnVal = ldapPasswordForBackendProcs;
        }

        return returnVal;
    }

    public void setLdapPasswordForBackendProcs(String ldapPasswordForBackendProcs) {
        this.ldapPasswordForBackendProcs = ldapPasswordForBackendProcs;
    }

    public boolean isAuthenticateBrapi() {
        return isAuthenticateBrapi;
    }

    public void setAuthenticateBrapi(boolean authenticateBrapi) {
        isAuthenticateBrapi = authenticateBrapi;
    }


    public Integer getMaxUploadSizeMbytes() {
        return maxUploadSizeMbytes;
    }

    public void setMaxUploadSizeMbytes(Integer maxUploadSizeMbytes) {
        this.maxUploadSizeMbytes = maxUploadSizeMbytes;
    }

    public boolean isProvidesBackend() {
        return isProvidesBackend;
    }

    public void setProvidesBackend(boolean providesBackend) {
        isProvidesBackend = providesBackend;
    }

    public Map<GobiiServerType, ServerBase> getGlobalServersByServerType() {
        return globalServersByServerType;
    }

    public void setGlobalServersByServerType(Map<GobiiServerType, ServerBase> globalServersByServerType) {
        this.globalServersByServerType = globalServersByServerType;
    }
}
