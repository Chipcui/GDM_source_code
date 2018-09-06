package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiCropServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ServerBase {

    @Element(required = false)
    private boolean decrypt = false;

    @Element(required = false)
    private String userName = null;

    @Element(required = false)
    private String password = null;

    @Element(required = false)
    private GobiiCropServerType gobiiCropServerType = null;

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

    public ServerBase(GobiiCropServerType gobiiCropServerType,
                      String host,
                      String contextPath,
                      Integer port,
                      boolean isActive,
                      String userName,
                      String password,
                      boolean decrypt) {

        this.gobiiCropServerType = gobiiCropServerType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;

    }

    public ServerBase(GobiiCropServerType gobiiCropServerType,
                      String host,
                      String contextPath,
                      Integer port,
                      boolean isActive) {

        this.gobiiCropServerType = gobiiCropServerType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;

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

    public String getJdbcConnectionString() {

        String returnVal = "jdbc:"
                + this.gobiiCropServerType.toString().toLowerCase()
                + "://"
                + this.getHost()
                + ":"
                + this.getPort()
                + "/"
                + this.getContextPath();

        return (returnVal);
    }
    public GobiiCropServerType getGobiiCropServerType() {
        return gobiiCropServerType;
    }

    public ServerBase setGobiiCropServerType(GobiiCropServerType gobiiCropServerType) {
        this.gobiiCropServerType = gobiiCropServerType;
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


}
