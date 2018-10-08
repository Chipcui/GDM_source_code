package org.gobiiproject.gobiiapimodel.types;

import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.LineUtils;

/**
 * Created by Phil on 5/13/2016.
 */


public enum RestRequestId {

    URL_AUTH("auth", ServerType.GOBII_WEB),
    URL_PING("ping", ServerType.GOBII_WEB),
    URL_PROJECTS("projects", ServerType.GOBII_WEB),
    URL_NAME_ID_LIST_DEPRECATED("nameidlist", ServerType.GOBII_WEB),
    URL_NAMES("names", ServerType.GOBII_WEB),
    URL_FILE_LOAD("files/loader", ServerType.GOBII_WEB),
    URL_FILE_LOAD_INSTRUCTIONS("instructions/loader", ServerType.GOBII_WEB),
    URL_FILE_EXTRACTOR_INSTRUCTIONS("instructions/extractor", ServerType.GOBII_WEB),
    URL_FILE_EXTRACTOR_JOBS("instructions/extractor/jobs", ServerType.GOBII_WEB),
    URL_FILE_LOADER_JOBS("instructions/loader/jobs", ServerType.GOBII_WEB),
    URL_DISPLAY("displays", ServerType.GOBII_WEB),
    URL_CV("cvs", ServerType.GOBII_WEB),
    URL_CVGROUP("cvgroups", ServerType.GOBII_WEB),
    URL_CONTACTS("contacts", ServerType.GOBII_WEB),
    URL_CONTACT_SEARCH("contact-search", ServerType.GOBII_WEB),
    URL_ORGANIZATION_SEARCH("organization-search", ServerType.GOBII_WEB),
    URL_REFERENCE("references", ServerType.GOBII_WEB),
    URL_EXPERIMENTS("experiments", ServerType.GOBII_WEB),
    URL_DATASETS("datasets", ServerType.GOBII_WEB),
    URL_DATASETTYPES("datasets/types", ServerType.GOBII_WEB),
    URL_ANALYSIS("analyses", ServerType.GOBII_WEB),
    URL_MARKERGROUP("markergroups", ServerType.GOBII_WEB),
    URL_MANIFEST("manifests", ServerType.GOBII_WEB),
    URL_ORGANIZATION("organizations", ServerType.GOBII_WEB),
    URL_PLATFORM("platforms", ServerType.GOBII_WEB),
    URL_MAPSET("mapsets", ServerType.GOBII_WEB),
    URL_CONFIGSETTINGS("configsettings", ServerType.GOBII_WEB),
    URL_ROLES("roles", ServerType.GOBII_WEB),
    URL_MARKERS("markers", ServerType.GOBII_WEB),
    URL_MARKER_SEARCH("marker-search", ServerType.GOBII_WEB),
    URL_CALLS("calls", ServerType.GOBII_WEB),
    URL_STUDIES_SEARCH("studies-search", ServerType.GOBII_WEB),
    URL_STUDIES("studies", ServerType.GOBII_WEB),
    URL_GERMPLASM("germplasm", ServerType.GOBII_WEB),
    URL_OBSERVATION_VARIABLES("observationVariables", ServerType.GOBII_WEB),
    URL_PROTOCOL("protocols", ServerType.GOBII_WEB),
    URL_VENDORS("vendors", ServerType.GOBII_WEB),
    URL_ALLELE_MATRIX_SEARCH("allelematrix-search", ServerType.GOBII_WEB),
    URL_ALLELE_MATRIX_SEARCH_STATUS("allelematrix-search/status", ServerType.GOBII_WEB),
    URL_ALLELE_MATRICES("allelematrices", ServerType.GOBII_WEB),
    URL_FILES("files", ServerType.GOBII_WEB),
    URL_JOB("jobs", ServerType.GOBII_WEB),
    URL_ENTITIES("entities", ServerType.GOBII_WEB),
    URL_COUNT("count", ServerType.GOBII_WEB),
    URL_LAST_MODIFIED("lastmodified", ServerType.GOBII_WEB),
    URL_VERTICES("vertices", ServerType.GOBII_WEB),
    URL_VALUES("values",ServerType.GOBII_WEB);


    private String requestPath;
    private ServerType serverType;

    RestRequestId(String requestPath, ServerType serverType) {
        this.requestPath = requestPath;
        this.serverType = serverType;
    }

    public String getResourcePath() {
        return this.requestPath;
    }

    public ServerType getServerType() {
        return this.serverType;
    }

    public String getRequestUrl(String contextRoot, String secondaryPath) throws Exception {

        String returnVal = "";


        if (!LineUtils.isNullOrEmpty(contextRoot)) {
            returnVal = contextRoot;
        }

        if (!LineUtils.isNullOrEmpty(secondaryPath)) {
            returnVal += secondaryPath;
        }

        returnVal += this.getResourcePath();

        return returnVal;
    }

}
