package org.gobiiproject.gobiimodel.dto.rest;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.HashMap;
import java.util.Map;

/***
 * Provides meta data pertaining to a specific REST resource, which is defined
 * by a RestResourceId value. The primary meta data at this time are the resource
 * limits, on a per-verb basis, which are encapsulated in RestResourceMethodLimitColl.
 *
 * Meta data are defined on a per RestResourceId basis. For example, there should be
 * only one instance of this class RestResourceId.DNASAMPLES. However, there are cases
 * where it is necessary to define resource limits for a rest resource _and_ its template
 * parameter. For example, RestResourceId.GOBII_NAMES has a significant number of template
 * parameter values (see GobiiEntityNameType). This class makes it possible to define and
 * retrieve a resource limit for a particular REST resource plus a given template paramter
 * value. There are also semantics in this class for dealing with the case where there are
 * no template parameters.
 */
public class RestResourceProfile {

    //default ctor needed for serialization
    public RestResourceProfile() {
    }

    // the default param is used for the case in which there are no TEMPLATE parameters
    // it will be used as the key for the resourceMethodCollsByTemplateParam that defines
    // the limit for the resource without template parameters.
    private final String DEFAULT_TEMPLATE_PARAM = "/";

    @ElementMap(required = false)
    private Map<String, RestResourceMethodLimitColl> resourceMethodCollsByTemplateParam = new HashMap<>();

    @Element(required = false)
    private RestResourceId restResourceId;

    @Element(required = false)
    private boolean hasTemplateParameters = false;

    /***
     * If a tempalte parameter is added later, the hasTemplateParams value will be reset.
     * @param restResourceId
     * @param hasTemplateParams
     */
    public RestResourceProfile(RestResourceId restResourceId, boolean hasTemplateParams) {
        this.restResourceId = restResourceId;
        if (!hasTemplateParams) {
            this.resourceMethodCollsByTemplateParam.put(DEFAULT_TEMPLATE_PARAM,
                    new RestResourceMethodLimitColl());
        }

        this.hasTemplateParameters = hasTemplateParams;
    }


    public RestResourceId getRestResourceId() {
        return restResourceId;
    }

    public void setRestResourceId(RestResourceId restResourceId) {
        this.restResourceId = restResourceId;
    }

    public boolean isHasTemplateParameters() {
        return hasTemplateParameters;
    }

    public void setHasTemplateParameters(boolean hasTemplateParameters) {
        this.hasTemplateParameters = hasTemplateParameters;
    }

    public boolean isRestMethodDefined(RestMethodType restMethodType) {
        return this.resourceMethodCollsByTemplateParam.containsKey(DEFAULT_TEMPLATE_PARAM) &&
                this.resourceMethodCollsByTemplateParam.get(DEFAULT_TEMPLATE_PARAM).isMethodDefined(restMethodType);
    }

    public boolean isRestMethodDefined(RestMethodType restMethodType, String templateParameter) {
        templateParameter = templateParameter.toUpperCase();
        return this.resourceMethodCollsByTemplateParam.containsKey(templateParameter)
                && this.resourceMethodCollsByTemplateParam.get(templateParameter).isMethodDefined(restMethodType);
    }

    /***
     * Default methhod overload for setting a resource limit without a template parameter
     * @param restMethodType
     * @param max
     */
    public void setMethodLimit(RestMethodType restMethodType, Integer max) {
        this.resourceMethodCollsByTemplateParam
                .get(DEFAULT_TEMPLATE_PARAM)
                .setMethodLimit(restMethodType, max);
    }


    public Integer getMethodLimit(RestMethodType restMethodType) {
        return this.resourceMethodCollsByTemplateParam
                .get(DEFAULT_TEMPLATE_PARAM)
                .getMethodLimit(restMethodType);
    }

    /***
     * Overload for specifying the resource limit with a given template parameter
     * @param restMethodType
     * @param templateParameter
     * @param max
     * @return
     */
    public RestResourceProfile setMethodLimit(RestMethodType restMethodType,
                                              String templateParameter,
                                              Integer max) {

        templateParameter = templateParameter.toUpperCase();

        if (!this.isHasTemplateParameters()) {
            this.setHasTemplateParameters(true);
        }

        if (!this.resourceMethodCollsByTemplateParam.containsKey(templateParameter)) {


            this.resourceMethodCollsByTemplateParam
                    .put(templateParameter, new RestResourceMethodLimitColl());
        }

        this.resourceMethodCollsByTemplateParam.get(templateParameter).setMethodLimit(restMethodType, max);

        return this;
    }

    public Integer getMethodLimit(RestMethodType restMethodType,
                                  String templateParameter) {

        templateParameter = templateParameter.toUpperCase();
        if (!this.resourceMethodCollsByTemplateParam
                .containsKey(templateParameter)) {
            throw new GobiiException("No call profile is defined for template parameter " + templateParameter);
        }

        return this.resourceMethodCollsByTemplateParam.get(templateParameter).getMethodLimit(restMethodType);
    }

}
