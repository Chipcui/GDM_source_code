package org.gobiiproject.gobiiclient.brapi;


import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
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
public class BrapiTestObservationVariables {

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
    public void getObservationVariables() throws Exception {


        RestUri restUriObservationVariables = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .childResourceByUriIdParam(RestResourceId.BRAPI_STUDIES,
                        RestResourceId.GOBII_OBSERVATION_VARIABLES);
        restUriObservationVariables.setParamValue("id", "1");

        BrapiEnvelopeRestResource<ObjectUtils.Null, BrapiResponseObservationVariablesMaster, BrapiResponseObservationVariablesMaster> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriObservationVariables,
                        ObjectUtils.Null.class,
                        BrapiResponseObservationVariablesMaster.class,
                        BrapiResponseObservationVariablesMaster.class,
                        httpCore);

        BrapiResponseEnvelopeMaster<BrapiResponseObservationVariablesMaster> brapiResponseEnvelopeMaster =
                brapiEnvelopeRestResource.getFromMasterResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(brapiResponseEnvelopeMaster.getBrapiMetaData());

        BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseEnvelopeMaster.getResult();


        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getStudyDbId());
        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getTrialName());
        Assert.assertTrue(brapiResponseObservationVariablesMaster.getData().size() > 0);
        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getData().get(0).getName());

    }
}
