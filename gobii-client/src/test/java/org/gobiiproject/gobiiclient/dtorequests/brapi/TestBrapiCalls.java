package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.ResourceBuilder;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestAnalysis;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.brapi.CallsDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.ContactDTO;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.types.brapi.BrapiDataTypes;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Collectors;

/**
 * Created by Phil on 12/14/2016.
 */
public class TestBrapiCalls {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testCallsGet() throws Exception {

        RestUri restUriCalls = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CALLS);
        RestResource<CallsDTO> restResourceCalls = new RestResource<>(restUriCalls);
        PayloadEnvelope<CallsDTO> resultEnvelope = restResourceCalls
                .get(CallsDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        Assert.assertTrue(resultEnvelope.getPayload().getData().size() > 0 );

        String callsRelativePath = ResourceBuilder.getRelativePath(ServiceRequestId.URL_CALLS);
        CallsDTO callsDtoCalls = resultEnvelope
                .getPayload()
                .getData()
                .stream()
                .filter( callsDTO -> callsDTO.getCall().equals(callsRelativePath))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(callsDtoCalls.getDataTypes().get(0).equals(BrapiDataTypes.JSON));

        Assert.assertTrue(callsDtoCalls.getMethods().get(0).equals(RestMethodTypes.GET));

    }
}
