package org.gobiiproject.gobiiapimodel.restresources.gobii;


import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;

/**
 * Created by Phil on 9/7/2016.
 */
public class GobiiUriFactory {

    private GobiiControllerType gobiiControllerType;
    private String cropContextRoot;

    public GobiiUriFactory(String cropContextRoot, GobiiControllerType gobiiControllerType) {

        this.gobiiControllerType = gobiiControllerType;
        this.cropContextRoot = cropContextRoot;
    }

    public GobiiUriFactory(String cropContextRoot) {

        this.gobiiControllerType = GobiiControllerType.GOBII;
        this.cropContextRoot = cropContextRoot;
    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri);
    }

    public RestUri resourceColl(GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                gobiiServiceRequestId.getResourcePath());

        return returnVal;

    } //

    public static RestUri resourceColl(String contextRoot, GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(contextRoot,
                GobiiControllerType.GOBII.getControllerPath(),
                gobiiServiceRequestId.getResourcePath());

        return returnVal;

    } //

    public RestUri resourceByUriIdParam(GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                gobiiServiceRequestId.getResourcePath())
                .addUriParam(paramName);
    } //

    public RestUri resourceByUriIdParamName(String paramName, GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        return new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                gobiiServiceRequestId.getResourcePath())
                .addUriParam(paramName);
    } //

    public static RestUri resourceByUriIdParam(String contextRoot, GobiiServiceRequestId gobiiServiceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(contextRoot,
                GobiiControllerType.GOBII.getControllerPath(),
                gobiiServiceRequestId.getResourcePath())
                .addUriParam(paramName);
    } //


    public RestUri childResourceByUriIdParam(GobiiServiceRequestId parentGobiiServiceRequestId, GobiiServiceRequestId childGobiiServiceRequestId) throws Exception {

        String paramName = "id";
        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                parentGobiiServiceRequestId.getResourcePath())
                .addUriParam(paramName)
                .appendSegment(childGobiiServiceRequestId);

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                GobiiServiceRequestId.URL_CONTACT_SEARCH.getResourcePath())
                .addQueryParam("email")
                .addQueryParam("lastName")
                .addQueryParam("firstName")
                .addQueryParam("userName");

        return returnVal;

    } //

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                GobiiServiceRequestId.URL_MARKER_SEARCH.getResourcePath())
                .addQueryParam("name");

        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                GobiiServiceRequestId.URL_NAMES.getResourcePath())
                .addUriParam("entity")
                .addQueryParam("filterType")
                .addQueryParam("filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                GobiiServiceRequestId.URL_FILE_LOAD.getResourcePath())
                .addUriParam("directoryName")
                .addQueryParam("fileFormat");

        return returnVal;

    } //
}


