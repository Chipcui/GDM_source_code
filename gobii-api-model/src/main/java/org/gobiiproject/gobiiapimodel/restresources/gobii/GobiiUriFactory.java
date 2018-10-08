package org.gobiiproject.gobiiapimodel.restresources.gobii;


import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.RestRequestId;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

/**
 * Created by Phil on 9/7/2016.
 */
public class GobiiUriFactory {

    private GobiiControllerType gobiiControllerType;
    private String cropContextRoot;
    private String domain = null;
    private Integer port = null;

    public GobiiUriFactory(String domain, Integer port, String cropContextRoot, GobiiControllerType gobiiControllerType) {

        this(cropContextRoot, gobiiControllerType);
        this.domain = domain;
        this.port = port;
    }

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

    public RestUri resourceColl(RestRequestId restRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                restRequestId.getResourcePath());

        return returnVal;

    } //

    public static RestUri resourceColl(String contextRoot, RestRequestId restRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(contextRoot,
                GobiiControllerType.GOBII.getControllerPath(),
                restRequestId.getResourcePath());

        return returnVal;

    } //

    public RestUri resourceByUriIdParam(RestRequestId restRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                restRequestId.getResourcePath())
                .addUriParam(paramName);
    } //

    public RestUri resourceByUriIdParamName(String paramName, RestRequestId restRequestId) throws Exception {

        return new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                restRequestId.getResourcePath())
                .addUriParam(paramName);
    } //

    public static RestUri resourceByUriIdParam(String contextRoot, RestRequestId restRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(contextRoot,
                GobiiControllerType.GOBII.getControllerPath(),
                restRequestId.getResourcePath())
                .addUriParam(paramName);
    } //


    public RestUri childResourceByUriIdParam(RestRequestId parentRestRequestId, RestRequestId childRestRequestId) throws Exception {

        String paramName = "id";
        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                parentRestRequestId.getResourcePath())
                .addUriParam(paramName)
                .appendSegment(childRestRequestId);

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_CONTACT_SEARCH.getResourcePath())
                .addQueryParam("email")
                .addQueryParam("lastName")
                .addQueryParam("firstName")
                .addQueryParam("userName");

        return returnVal;

    } //

    public RestUri cvGroupByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_CVGROUP.getResourcePath())
                .addUriParam("groupName")
                .addQueryParam("cvGroupTypeId");

        return returnVal;
    }

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_MARKER_SEARCH.getResourcePath())
                .addQueryParam("name");

        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_NAMES.getResourcePath())
                .addUriParam("entity")
                .addQueryParam("filterType")
                .addQueryParam("filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_FILE_LOAD.getResourcePath())
                .addUriParam("directoryName")
                .addQueryParam("fileFormat");

        return returnVal;

    } //

    public RestUri fileForJob(String jobId,
                              GobiiFileProcessDir gobiiFileProcessDir,
                              String fileName) throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_FILES.getResourcePath())
                .addUriParam("gobiiJobId", jobId)
                .addUriParam("destinationType", gobiiFileProcessDir.toString())
                .addQueryParam("fileName", fileName);

        return returnVal;
    }

    public RestUri file(GobiiFileProcessDir gobiiFileProcessDir,
                        String fileName) throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_FILES.getResourcePath())
                .addUriParam("destinationType", gobiiFileProcessDir.toString())
                .addQueryParam("fileName", fileName);

        return returnVal;
    }

    public RestUri entityCount(GobiiEntityNameType gobiiEntityNameType) throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_ENTITIES.getResourcePath())
                .addUriParam("entityName", gobiiEntityNameType.toString().toLowerCase())
                .appendSegment(RestRequestId.GOBII_COUNT);

        return returnVal;
    }

    public RestUri entityChildCount(GobiiEntityNameType gobiiEntityNameTypeParent,
                                    GobiiEntityNameType gobiiEntityNameTypeChild,
                                    Integer parentId) throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_ENTITIES.getResourcePath())
                .addUriParam("entityNameParent", gobiiEntityNameTypeParent.toString().toLowerCase())
                .addUriParam("parentId", parentId.toString())
                .addUriParam("entityNameChild", gobiiEntityNameTypeChild.toString())
                .appendSegment(RestRequestId.GOBII_COUNT);

        return returnVal;
    }

    public RestUri entityLastModified(GobiiEntityNameType gobiiEntityNameType) throws Exception {

        RestUri returnVal = new RestUri(this.domain,
                this.port,
                this.cropContextRoot,
                this.gobiiControllerType.getControllerPath(),
                RestRequestId.GOBII_ENTITIES.getResourcePath())
                .addUriParam("entityName", gobiiEntityNameType.toString().toLowerCase())
                .appendSegment(RestRequestId.GOBII_LAST_MODIFIED);

        return returnVal;
    }

    public RestUri pagedList(RestRequestId restRequestId,
                             Integer pageSize,
                             Integer pageNo,
                             String queryId) throws Exception {

        RestUri returnVal = this.resourceColl(restRequestId)
                .addQueryParam("pageSize", pageSize.toString())
                .addQueryParam("pageNo", pageNo.toString())
                .addQueryParam("queryId", queryId);

        return returnVal;
    }

}


