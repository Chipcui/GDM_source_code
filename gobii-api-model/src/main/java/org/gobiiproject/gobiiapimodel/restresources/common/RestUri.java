package org.gobiiproject.gobiiapimodel.restresources.common;

import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 9/7/2016.
 */
public class RestUri {

    public static char URL_SEPARATOR = '/';
    private final String DELIM_PARAM_BEGIN = "{";
    private final String DELIM_PARAM_END = "}";

    private String contextPath;
    private String contextRoot;

    private String requestTemplate;
    private Map<String, ResourceParam> paramMap = new HashMap<>();
    private List<ResourceParam> resourceParams = new ArrayList<>();

    private Map<String,String> httpHeaders = new HashMap<>();

    public RestUri(String contextRoot, String contextPath, String resourcePath) throws Exception {
        this.contextRoot = this.delimitSegment(contextRoot);
        this.contextPath = contextPath;
        this.requestTemplate = this.contextRoot + this.contextPath + resourcePath;

        this.httpHeaders.put(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE_JSON);

        this.httpHeaders.put(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE_JSON);

    }

    public RestUri(String restUri) {
        this.requestTemplate = restUri;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public String getResourcePath() throws Exception {

        String returnVal;

        Integer idxOfLastDelimiter = this.requestTemplate.length() - 1;
        if (this.requestTemplate.charAt(idxOfLastDelimiter) == URL_SEPARATOR) {
            returnVal = requestTemplate.substring(0, idxOfLastDelimiter);
        } else {
            returnVal = this.requestTemplate;
        }

        return RestUri.URL_SEPARATOR
                + returnVal
                .replace(this.contextPath, "")
                .replace(this.contextRoot, "");
    }

    private String delimitSegment(String segment) {

        String returnVal = segment;

        if (null != returnVal) {
            if (returnVal.lastIndexOf(RestUri.URL_SEPARATOR) != returnVal.length() - 1) {
                returnVal = returnVal + RestUri.URL_SEPARATOR;
            }
        }

        return returnVal;
    }



    public List<ResourceParam> getRequestParams() {
        return this.resourceParams
                .stream()
                .filter(getParam -> getParam.getResourceParamType().equals(ResourceParam.ResourceParamType.QueryParam))
                .collect(Collectors.toList());
    }


    public RestUri addQueryParam(String name) {
        this.addParam(ResourceParam.ResourceParamType.QueryParam, name);
        return this;
    }


    public RestUri addUriParam(String name) {
        this.addParam(ResourceParam.ResourceParamType.UriParam, name);
        return this;
    }

    private RestUri addParam(ResourceParam.ResourceParamType resourceParamType,
                             String name) {

        if (resourceParamType.equals(ResourceParam.ResourceParamType.UriParam)) {
            this.appendPathVariable(name);
        }

        ResourceParam resourceParam = new ResourceParam(resourceParamType, name, null);
        this.paramMap.put(resourceParam.getName(), resourceParam);
        this.resourceParams.add(resourceParam);

        return this;

    }

    public RestUri setParamValue(String paramName, String value) throws Exception {

        if (null == this.paramMap.get(paramName)) {
            throw new Exception("Specified parameter does not exist: " + paramName);
        }

        this.paramMap.get(paramName).setValue(value);

        return this;
    }

    public RestUri appendSegment(GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        this.requestTemplate = this.delimitSegment(this.requestTemplate);
        String segment = gobiiServiceRequestId.getResourcePath();

        this.requestTemplate += this.delimitSegment(segment);

        return this;
    }

    public RestUri appendPathVariable(String paramName) {

        this.requestTemplate = this.delimitSegment(this.requestTemplate);

        this.requestTemplate += this.DELIM_PARAM_BEGIN + paramName + this.DELIM_PARAM_END;

        return this;

    }

    public RestUri withHttpHeader(String headerName, String headerValue) {
        this.httpHeaders.put(headerName,headerValue);
        return this;
    }


    public String makeUrl() throws Exception {

        String returnVal = this.requestTemplate; // in case there are no path variables

        List<ResourceParam> uriParams = resourceParams
                .stream()
                .filter(getRequestParam -> getRequestParam.getResourceParamType()
                        .equals(ResourceParam.ResourceParamType.UriParam))
                .collect(Collectors.toList());

        for (ResourceParam currentParam : uriParams) {
            String paramToReplace = this.DELIM_PARAM_BEGIN
                    + currentParam.getName()
                    + this.DELIM_PARAM_END;

            if (this.requestTemplate.contains(paramToReplace)) {

                if (false == LineUtils.isNullOrEmpty(currentParam.getValue())) {

                    returnVal = returnVal.replace(paramToReplace, currentParam.getValue());
                } else {
                    throw new Exception("The path variable parameter "
                            + paramToReplace
                            + " does not have a value");
                }
            } else {
                throw new Exception("The request template "
                        + this.requestTemplate
                        + " does not contain the path path variable "
                        + paramToReplace);
            }
        }

        if (returnVal.contains(this.DELIM_PARAM_BEGIN)) {
            String missingParameter = returnVal
                    .substring(
                            returnVal.indexOf(this.DELIM_PARAM_BEGIN),
                            returnVal.indexOf(this.DELIM_PARAM_END)
                    );

            throw new Exception("There is no parameter for uri parameter " + missingParameter);
        }

        return returnVal;

    } // makeUrl

} // class RestUri
