package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for file-system specific information used in conversation with the server.
 * Created by Phil on 4/12/2016.
 */
public class MatrixUrlDTO extends DTOBase {

    private String url = null;
    private String fileName = null;
    private String md5Sum = null;

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMd5Sum() {
        return md5Sum;
    }

    public void setMd5Sum(String md5Sum) {
        this.md5Sum = md5Sum;
    }
}
