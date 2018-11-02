package org.gobiiproject.gobiibrapi.calls.calls;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiibrapi.types.BrapiDataTypes;
import org.gobiiproject.gobiimodel.types.RestMethodType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapCalls {

    String contextRoot = null;

    public BrapiResponseMapCalls(HttpServletRequest request) {

        this.contextRoot = request.getContextPath();
    }

    private List<BrapiResponseCallsItem> getBrapiResponseCallsItems() throws Exception {

        List<BrapiResponseCallsItem> returnVal = new ArrayList<>();

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                RestResourceId.GOBII_CALLS.getResourcePath()),
                Arrays.asList(RestMethodType.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                RestResourceId.GOBII_STUDIES_SEARCH.getResourcePath()),
                Arrays.asList(RestMethodType.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                RestResourceId.GOBII_GERMPLASM.getResourcePath()).addUriParam("id"),
                Arrays.asList(RestMethodType.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                RestResourceId.GOBII_STUDIES.getResourcePath()).addUriParam("id").appendSegment(RestResourceId.GOBII_OBSERVATION_VARIABLES),
                Arrays.asList(RestMethodType.POST),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                RestResourceId.BRAPI_ALLELE_MATRICES.getResourcePath()).addQueryParam("studyDbId"),
                Arrays.asList(RestMethodType.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        returnVal.add(new BrapiResponseCallsItem(new RestUri(this.contextRoot,
                GobiiControllerType.BRAPI.getControllerPath(),
                RestResourceId.BRAPI_ALLELE_MATRIX_SEARCH.getResourcePath()),
                Arrays.asList(RestMethodType.GET, RestMethodType.POST),
                Arrays.asList(BrapiDataTypes.FLAPJACK)));


        return returnVal;
    }

    public BrapiResponseCalls getBrapiResponseCalls() throws Exception {

        BrapiResponseCalls returnVal = new BrapiResponseCalls();
        List<BrapiResponseCallsItem> brapiResponseCallsItems = this.getBrapiResponseCallsItems();
        returnVal.setData(brapiResponseCallsItems);

        return returnVal;

    }
}
