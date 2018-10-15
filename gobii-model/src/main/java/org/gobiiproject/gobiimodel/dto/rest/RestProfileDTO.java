package org.gobiiproject.gobiimodel.dto.rest;

import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.simpleframework.xml.Element;

public class RestProfileDTO extends DTOBase {

    public RestProfileDTO() {}

    public RestProfileDTO(RestResourceId restResourceId, RestMethodType restMethodType, String templateParameter, Integer max) {
        this.restResourceId = restResourceId;
        this.restMethodType = restMethodType;
        this.templateParameter = templateParameter;
        this.max = max;
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }


    @Element
    private RestResourceId restResourceId;

    @Element
    private RestMethodType restMethodType;

    @Element
    private String templateParameter;

    @Element
    private Integer max;

    public RestResourceId getRestResourceId() {
        return restResourceId;
    }

    public void setRestResourceId(RestResourceId restResourceId) {
        this.restResourceId = restResourceId;
    }

    public RestMethodType getRestMethodType() {
        return restMethodType;
    }

    public void setRestMethodType(RestMethodType restMethodType) {
        this.restMethodType = restMethodType;
    }

    public String getTemplateParameter() {
        return templateParameter;
    }

    public void setTemplateParameter(String templateParameter) {
        this.templateParameter = templateParameter;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
