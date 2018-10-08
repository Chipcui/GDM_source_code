package org.gobiiproject.gobiiapimodel.restresources.gobii;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.RestRequestId;
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
                            RestRequestId.GOBII_ANALYSIS);
                    break;

                case CONTACT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_CONTACTS);
                    break;

                case DATASET:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_DATASETS);
                    break;

                case CV:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_CV);
                    break;

                case CVGROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_CV);
                    break;

                case PROJECT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_PROJECTS);
                    break;

                case ORGANIZATION:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_ORGANIZATION);
                    break;

                case PLATFORM:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_PLATFORM);
                    break;

                case MANIFEST:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_MANIFEST);
                    break;

                case MAPSET:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_MAPSET);
                    break;

                case MARKER_GROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_MARKERGROUP);
                    break;

                case EXPERIMENT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_EXPERIMENTS);
                    break;

                case REFERENCE:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_REFERENCE);
                    break;

                case ROLE:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_ROLES);
                    break;

                case PROTOCOL:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_PROTOCOL);
                    break;

                case VENDOR_PROTOCOL:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            RestRequestId.GOBII_PROTOCOL)
                            .appendSegment(RestRequestId.GOBII_VENDORS);
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
