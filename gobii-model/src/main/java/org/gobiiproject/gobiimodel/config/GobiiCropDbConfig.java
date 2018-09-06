package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.types.GobiiCropServerType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This class contains the properties necessary to configure a database.
 */
@Root
public class GobiiCropDbConfig extends ServerBase {

    public GobiiCropDbConfig() {
    }

    public GobiiCropDbConfig(GobiiCropServerType gobiiCropServerType,
                             String host,
                             String dbName,
                             Integer port,
                             String userName,
                             String password,
                             boolean decrypt) {

        super(gobiiCropServerType, host,dbName,port,true,userName,password,decrypt);

    }


    public String getHost() {
        return super.getHost();
    }

    public GobiiCropDbConfig setHost(String host) {
        super.setHost(host);
        return this;
    }

    public String getContextPath() {
        return super.getContextPath(false);
    }

    public GobiiCropDbConfig setContextPath(String dbName) {
        super.setContextPath(dbName);
        return this;
    }

    public Integer getPort() {
        return super.getPort();
    }

    public GobiiCropDbConfig setPort(Integer port) {
        super.setPort(port);
        return this;
    }



}
