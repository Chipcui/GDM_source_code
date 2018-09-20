package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiServerType;
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
    private GobiiServerType gobiiServerType = null;

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

    public ServerBase(GobiiServerType gobiiServerType,
                      String host,
                      String contextPath,
                      Integer port,
                      boolean isActive,
                      String userName,
                      String password,
                      boolean decrypt) {

        this.gobiiServerType = gobiiServerType;
        this.host = host;
        this.contextPath = contextPath;
        this.port = port;
        this.isActive = isActive;
        this.userName = userName;
        this.password = password;
        this.decrypt = decrypt;

    }

    public ServerBase(GobiiServerType gobiiServerType,
                      String host,
                      String contextPath,
                      Integer port,
                      boolean isActive,
                      boolean decrypt) {

        this.gobiiServerType = gobiiServerType;
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


    public GobiiServerType getGobiiServerType() {
        return gobiiServerType;
    }

    public ServerBase setGobiiServerType(GobiiServerType gobiiServerType) {
        this.gobiiServerType = gobiiServerType;
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
