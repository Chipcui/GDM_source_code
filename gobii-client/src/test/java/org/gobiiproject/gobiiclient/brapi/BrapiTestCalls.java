package org.gobiiproject.gobiiclient.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCalls;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiiclient.core.brapi.BrapiClientContextAuth;
import org.gobiiproject.gobiiclient.core.common.HttpCore;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestCalls {

    private static HttpCore httpCore = null;

    @BeforeClass
    public static void setUpClass() throws Exception {

        // you have to set up the GobiiClient because some native GOBII calls
        // are made to set up data
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

        // but for the BRAPI calls we use the raw httpCore
        httpCore = BrapiClientContextAuth.authenticate();
        Assert.assertNotNull("Could not create http core component",
                httpCore);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void getCalls() throws Exception {


        RestUri restUriCalls = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .resourceColl(RestResourceId.GOBII_CALLS);

        BrapiEnvelopeRestResource<ObjectUtils.Null,ObjectUtils.Null,BrapiResponseCalls> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriCalls,
                        ObjectUtils.Null.class,
                        ObjectUtils.Null.class,
                        BrapiResponseCalls.class,
                        httpCore);

        BrapiResponseEnvelopeMasterDetail<BrapiResponseCalls> callsResult = brapiEnvelopeRestResource.getFromListResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(callsResult.getBrapiMetaData());

        Assert.assertTrue(callsResult.getResult().getData().size() > 0 );

    }
}
