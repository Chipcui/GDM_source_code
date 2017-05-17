// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.infrastructure;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiAuthenticator;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;

public class DtoRequestConfigSettingsPropsTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
    }

    private PayloadEnvelope<ConfigSettingsDTO> getConfigSettingsFromServer() throws Exception {

        GobiiClientContext.resetConfiguration();
        Assert.assertTrue(GobiiAuthenticator.authenticate());

        PayloadEnvelope<ConfigSettingsDTO> returnVal = null;

        RestUri confgSettingsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONFIGSETTINGS);
        GobiiEnvelopeRestResource<ConfigSettingsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(confgSettingsUri);
        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ConfigSettingsDTO.class);

        TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader());

        returnVal = resultEnvelope;

        Assert.assertTrue(GobiiAuthenticator.deAuthenticate());


        return returnVal;

    }

    @Test
    public void testGetConfigSettings() throws Exception {

        GobiiClientContext.resetConfiguration();
        Assert.assertTrue(GobiiAuthenticator.authenticate());

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = getConfigSettingsFromServer();
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull(configSettingsDTOResponse);
        Assert.assertTrue(configSettingsDTOResponse.getServerConfigs().size() > 0);


        String randomCrop = new ArrayList<String>( configSettingsDTOResponse
                .getServerConfigs()
                .keySet())
                .get(0);


        ServerConfig serverConfigDefaultCrop = configSettingsDTOResponse
                .getServerConfigs()
                .get(randomCrop);

        Assert.assertNotNull("The remote server configuration does not define crop type",
                serverConfigDefaultCrop.getGobiiCropType());

        Assert.assertTrue("The default crop's crop ID does not match the settings default crop",
                randomCrop.equals(serverConfigDefaultCrop.getGobiiCropType()));

        Assert.assertNotNull("The remote server configuration does not define a domain for crop type " + serverConfigDefaultCrop.getGobiiCropType(),
                serverConfigDefaultCrop.getDomain());

        Assert.assertNotNull("The remote server configuration does not define a context root for crop type " + serverConfigDefaultCrop.getGobiiCropType(),
                serverConfigDefaultCrop.getContextRoot());

        Assert.assertNotNull("The remote server configuration does not define a port for crop type " + serverConfigDefaultCrop.getGobiiCropType(),
                serverConfigDefaultCrop.getPort());


        URL url = new URL("http",
                serverConfigDefaultCrop.getDomain(),
                serverConfigDefaultCrop.getPort(),
                serverConfigDefaultCrop.getContextRoot());

        String serviceUrl = url.toString();


        GobiiClientContext.resetConfiguration();

        GobiiClientContext.getInstance(serviceUrl, true);

        String testUser = GobiiAuthenticator.getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = GobiiAuthenticator.getTestExecConfig().getLdapPasswordForUnitTest();

        String testCrop = GobiiAuthenticator.getTestExecConfig().getTestCrop();

        Assert.assertTrue("Unable to authenticate to remote server with default drop " + randomCrop,
                GobiiClientContext.getInstance(null, false).login(testCrop, testUser, testPassword));

        Assert.assertTrue(GobiiAuthenticator.deAuthenticate());

    }

    @Test
    public void testWithCaseMisMatchedCropname() throws Exception {

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = getConfigSettingsFromServer();
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(configSettingsDTOResponse.getServerConfigs().size() > 0);

        String randomCrop = new ArrayList<String>(
                configSettingsDTOResponse.getServerConfigs().keySet()
        ).get(0);

        ServerConfig serverConfigDefaultCrop = configSettingsDTOResponse
                .getServerConfigs()
                .get(randomCrop);


        URL url = new URL("http",
                serverConfigDefaultCrop.getDomain(),
                serverConfigDefaultCrop.getPort(),
                serverConfigDefaultCrop.getContextRoot());

        String serviceUrl = url.toString();


        GobiiClientContext.resetConfiguration();

        GobiiClientContext.getInstance(serviceUrl, true);


        String defaultCropMismatched = randomCrop.toUpperCase();


        try {
            //authentication would fail if we didn't get the mismatched crop error
            GobiiClientContext.getInstance(null, false).login(defaultCropMismatched, "foo", "foo");
        } catch (Exception e) {
            Assert.assertTrue("Setting context to a case-mismatched crop type does not throw an exception",
                    e.getMessage().contains("The requested crop does not exist in the configuration"));

        }

        Assert.assertTrue(GobiiAuthenticator.deAuthenticate());

    }

    @Test
    public void testInitContextFromConfigSettings() throws Exception {

        GobiiClientContext.resetConfiguration();
        ConfigSettings configSettings = new GobiiTestConfiguration().getConfigSettings();

        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();


        GobiiClientContext.getInstance(configSettings,
                gobiiTestConfiguration
                        .getConfigSettings()
                        .getTestExecConfig()
                        .getTestCrop(),
                GobiiAutoLoginType.USER_TEST);

        Assert.assertNotNull("Unable to log in with locally instantiated config settings",
                GobiiClientContext.getInstance(null, false).getUserToken());


        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        //DtoRequestPing dtoRequestPing = new DtoRequestPing();
        GobiiEnvelopeRestResource<PingDTO> gobiiEnvelopeRestResourcePingDTO = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PING));

        PayloadEnvelope<PingDTO> resultEnvelopePing = gobiiEnvelopeRestResourcePingDTO.post(PingDTO.class,
                new PayloadEnvelope<>(pingDTORequest, GobiiProcessType.CREATE));
        //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));

        Assert.assertNotNull(resultEnvelopePing);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePing.getHeader()));
        Assert.assertTrue(resultEnvelopePing.getPayload().getData().size() > 0);
        PingDTO pingDTOResponse = resultEnvelopePing.getPayload().getData().get(0);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getDbMetaData());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getDbMetaData().size());

        Assert.assertTrue(GobiiAuthenticator.deAuthenticate());

    } // testInitContextFromConfigSettings()

}
