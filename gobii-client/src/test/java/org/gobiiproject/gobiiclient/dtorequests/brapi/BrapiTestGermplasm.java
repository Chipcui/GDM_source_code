package org.gobiiproject.gobiiclient.dtorequests.brapi;


import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmByDbId;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestGermplasm {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void getGermplasmByDbid() throws Exception {


        RestUri restUriGermplasm = ClientContext.getInstance(null, false)
                .getUriFactory(ControllerType.BRAPI)
                .resourceByUriIdParam(ServiceRequestId.URL_GERMPLASM);
        restUriGermplasm.setParamValue("id", "1");

        BrapiEnvelopeRestResource<ObjectUtils.Null, BrapiResponseGermplasmByDbId, ObjectUtils.Null> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriGermplasm,
                        ObjectUtils.Null.class,
                        BrapiResponseGermplasmByDbId.class,
                        ObjectUtils.Null.class);

        BrapiResponseEnvelopeMaster<BrapiResponseGermplasmByDbId> brapiResponseEnvelopeMaster = brapiEnvelopeRestResource.getFromMasterResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(brapiResponseEnvelopeMaster.getBrapiMetaData());

        BrapiResponseGermplasmByDbId brapiRequestStudiesSearch = brapiResponseEnvelopeMaster.getResult();

        Assert.assertNotNull(brapiRequestStudiesSearch.getGermplasmDbId());
        Assert.assertNotNull(brapiRequestStudiesSearch.getGermplasmName());
        Assert.assertTrue(brapiRequestStudiesSearch.getDonors().size() > 0);

    }
}
