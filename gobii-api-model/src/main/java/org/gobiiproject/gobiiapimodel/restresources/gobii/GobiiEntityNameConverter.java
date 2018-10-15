package org.gobiiproject.gobiiapimodel.restresources.gobii;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 10/18/2016.
 */
public class GobiiEntityNameConverter {

    public static RestUri toServiceRequestId(String contextRoot,
                                             GobiiEntityNameType gobiiEntityNameType)
            throws GobiiException {


        RestUri returnVal;
        try {
            switch (gobiiEntityNameType) {

                case ANALYSIS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_ANALYSIS);
                    break;

                case CONTACT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_CONTACTS);
                    break;

                case DATASET:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_DATASETS);
                    break;

                case DNASAMPLE:
                    
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_DNASAMPLES);

                    break;

                case DNARUN:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_DNARUN);
                    break;

                case LINKAGE_GROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_LINKAGEGROUP);
                    break;

                case CV:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_CV);
                    break;

                case CVGROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_CV);
                    break;

                case PROJECT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_PROJECTS);
                    break;

                case ORGANIZATION:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_ORGANIZATION);
                    break;

                case PLATFORM:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_PLATFORM);
                    break;

                case MANIFEST:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_MANIFEST);
                    break;

                case MAPSET:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_MAPSET);
                    break;

                case MARKER:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_MARKERS);
                    break;

                case MARKER_GROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_MARKERGROUP);
                    break;

                case EXPERIMENT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_EXPERIMENTS);
                    break;

                case REFERENCE:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_REFERENCE);
                    break;

                case ROLE:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_ROLES);
                    break;

                case PROTOCOL:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_PROTOCOL);
                    break;

                case VENDOR_PROTOCOL:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestResourceId.GOBII_PROTOCOL)
                            .appendSegment(RestResourceId.GOBII_VENDORS);
                    break;

                default:
                    throw new GobiiException("Unknown GobiiEntityTypeName: " + gobiiEntityNameType.toString());

            }
        } catch (Exception e) {
            throw new GobiiException(e);
        }

        return returnVal;
    }
}
