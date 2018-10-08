package org.gobiiproject.gobiiapimodel.restresources.common;

import org.gobiiproject.gobiimodel.types.RestMethodTypes;

import java.util.ArrayList;
import java.util.List;

public class GobiiCallProfileDTO {

    public GobiiCallProfileDTO(String methodName,
                               List<RestMethodTypes> supportedVerbs,
                               Integer maxPostPut,
                               Integer maxGet) {
        this.methodName = methodName;
        this.supportedVerbs = supportedVerbs;
        this.maxPostPut = maxPostPut;
        this.maxGet = maxGet;
    }

    String methodName;
    List<RestMethodTypes> supportedVerbs = new ArrayList<>();
    Integer maxPostPut = 100;
    Integer maxGet = 500;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<RestMethodTypes> getSupportedVerbs() {
        return supportedVerbs;
    }

    public void setSupportedVerbs(List<RestMethodTypes> supportedVerbs) {
        this.supportedVerbs = supportedVerbs;
    }

    public Integer getMaxPostPut() {
        return maxPostPut;
    }

    public void setMaxPostPut(Integer maxPostPut) {
        this.maxPostPut = maxPostPut;
    }

    public Integer getMaxGet() {
        return maxGet;
    }

    public void setMaxGet(Integer maxGet) {
        this.maxGet = maxGet;
    }
}
