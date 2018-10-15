package org.gobiiproject.gobiimodel.dto.rest;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.simpleframework.xml.ElementMap;

import java.util.EnumMap;

/***
 * Collection of RestResourceMethodLimit by REST verb (POST, PUT, GET, DELETE, etc.)
 * The consumer of this class will be able to add, modify, and retrieve the resource limit
 * for a given verb.
 */
public class RestResourceMethodLimitColl {


    public RestResourceMethodLimitColl() {}

    @ElementMap
    private EnumMap<RestMethodType, RestResourceMethodLimit> limitsByMethodType = new EnumMap<>(RestMethodType.class);

    public boolean isMethodDefined(RestMethodType restMethodType) {
        return this.limitsByMethodType.containsKey(restMethodType);
    }

    public void setMethodLimit(RestMethodType restMethodType, Integer max) {

        // if it doesn't exist, add it
        if (!this.limitsByMethodType.containsKey(restMethodType)) {
            this.limitsByMethodType.put(restMethodType, new RestResourceMethodLimit(
                    restMethodType, max
            ));
        } else {
            this.limitsByMethodType.get(restMethodType).setResourceMax(max);
        }
    }

    public Integer getMethodLimit(RestMethodType restMethodType) {

        // throw instead of providing a potentially meaningless default
        if (!this.limitsByMethodType.containsKey(restMethodType)) {
            throw  new GobiiException("There is no limit set for REST verb " + restMethodType);
        }

        return this.limitsByMethodType.get(restMethodType).getResourceMax();
    }
}
