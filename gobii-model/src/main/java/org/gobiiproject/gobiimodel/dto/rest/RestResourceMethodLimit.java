package org.gobiiproject.gobiimodel.dto.rest;

import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.simpleframework.xml.Element;

/***
 * Defines the limit of items that a REST method will allow. The interpretation of this limit
 * by a consumer of this class will, of course, depend on the verb (for GET, it will be a retrieval
 * limit, for POST and PUT, it will be a limit on the number of items that the resource will accept
 * from the client)
 */
public class RestResourceMethodLimit {


    public RestResourceMethodLimit() {}

    public RestResourceMethodLimit(RestMethodType restMethodType, Integer resourceMax) {
        this.restMethodType = restMethodType;
        this.resourceMax = resourceMax;
    }

    @Element(required = false)
    private RestMethodType restMethodType;

    @Element(required = false)
    Integer resourceMax = 100; //reasonable default


    public RestMethodType getRestMethodType() {
        return restMethodType;
    }

    public void setRestMethodType(RestMethodType restMethodType) {
        this.restMethodType = restMethodType;
    }

    public Integer getResourceMax() {
        return resourceMax;
    }

    public void setResourceMax(Integer resourceMax) {
        this.resourceMax = resourceMax;
    }
}

