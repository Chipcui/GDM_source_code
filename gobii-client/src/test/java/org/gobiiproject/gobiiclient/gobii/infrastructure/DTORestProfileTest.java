package org.gobiiproject.gobiiclient.gobii.infrastructure;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.readonly.DtoRequestNameIdListTest;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.rest.RestProfileDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class DTORestProfileTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void changePostMax() throws Exception {

        // We know that the /names/analysis profile has been defined
        PayloadEnvelope<NameIdDTO> analysisNameIdResponseEnvelopePreUpdate = DtoRequestNameIdListTest
                .testNameRetrieval(GobiiEntityNameType.ANALYSIS,
                        GobiiFilterType.NONE,
                        null);


        Integer initialMaxGet = analysisNameIdResponseEnvelopePreUpdate.getHeader().getMaxGet();
        Integer initialMaxPost = analysisNameIdResponseEnvelopePreUpdate.getHeader().getMaxPost();
        Integer initialMaxPut = analysisNameIdResponseEnvelopePreUpdate.getHeader().getMaxPut();


        Assert.assertNotNull("The max GET limit for names/"
                        + GobiiEntityNameType.ANALYSIS.toString()
                        + " should not be null",
                initialMaxGet);

        Assert.assertNotNull("The max POST limit for names/"
                        + GobiiEntityNameType.ANALYSIS.toString()
                        + " should not be null",
                initialMaxPost);

        Assert.assertNotNull("The max PUT limit for names/"
                        + GobiiEntityNameType.ANALYSIS.toString()
                        + " should not be null",
                initialMaxPut);


        Assert.assertTrue("The max GET limit for names/"
                        + GobiiEntityNameType.ANALYSIS.toString()
                        + " should have a value",
                initialMaxGet > 0);

        Assert.assertTrue("The max POST limit for names/"
                        + GobiiEntityNameType.ANALYSIS.toString()
                        + " should have a value",
                initialMaxPost > 0);

        Assert.assertTrue("The max PUT limit for names/"
                        + GobiiEntityNameType.ANALYSIS.toString()
                        + " should have a value",
                initialMaxPut > 0);


        Integer newMaxGet = initialMaxGet + 5;
        Integer newMaxPost = initialMaxPost + 6;
        Integer newMaxPut = initialMaxPut + 7;


        this.updateRestProfile(new RestProfileDTO(RestResourceId.GOBII_NAMES,
                RestMethodType.GET,
                GobiiEntityNameType.ANALYSIS.toString(),
                newMaxGet));

        this.updateRestProfile(new RestProfileDTO(RestResourceId.GOBII_NAMES,
                RestMethodType.POST,
                GobiiEntityNameType.ANALYSIS.toString(),
                newMaxPost));

        this.updateRestProfile(new RestProfileDTO(RestResourceId.GOBII_NAMES,
                RestMethodType.PUT,
                GobiiEntityNameType.ANALYSIS.toString(),
                newMaxPut));

        PayloadEnvelope<NameIdDTO> analysisNameIdResponseEnvelopePostUpdate = DtoRequestNameIdListTest
                .testNameRetrieval(GobiiEntityNameType.ANALYSIS,
                        GobiiFilterType.NONE,
                        null);


        Integer newRetrievedMaxGet = analysisNameIdResponseEnvelopePostUpdate.getHeader().getMaxGet();
        Integer newRetrievedMaxPost = analysisNameIdResponseEnvelopePostUpdate.getHeader().getMaxPost();
        Integer newRetrievedMaxPut = analysisNameIdResponseEnvelopePostUpdate.getHeader().getMaxPut();

        Assert.assertEquals("The post-put retrieved GET max ("
                        + newRetrievedMaxGet
                        + "  does not match the value that was put ("
                        + newMaxGet
                        + ")",
                newMaxGet,
                newRetrievedMaxGet);

        Assert.assertEquals("The post-put retrieved POST max("
                        + newRetrievedMaxPost
                        + ") does not match the value that was put ("
                        + newMaxPost
                        + ")",
                newMaxPost,
                newRetrievedMaxPost);

        Assert.assertEquals("The post-put retrieved PUT max ("
                        + newRetrievedMaxPut
                        + " ) does not match the value that was put ("
                        + newMaxPost,
                newMaxPut,
                newRetrievedMaxPut);

    }

    private PayloadEnvelope<RestProfileDTO> updateRestProfile(RestProfileDTO restProfileDTO) throws Exception {

        RestUri restProfilesUri =
                GobiiClientContext
                        .getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(RestResourceId.GOBII_REST_PROFILES);
        GobiiEnvelopeRestResource<RestProfileDTO,RestProfileDTO> restEnvelopeRestProfile = new GobiiEnvelopeRestResource<>(restProfilesUri);
        PayloadEnvelope<RestProfileDTO> resultEnvelope = restEnvelopeRestProfile
                .put(RestProfileDTO.class, new PayloadEnvelope<>(restProfileDTO, GobiiProcessType.UPDATE));

        Assert.assertFalse("Error updating rest request profile",
                TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        return resultEnvelope;
    }
}
