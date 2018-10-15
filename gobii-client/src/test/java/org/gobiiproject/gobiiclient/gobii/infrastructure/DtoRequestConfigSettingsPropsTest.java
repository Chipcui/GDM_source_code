// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.infrastructure;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.dto.system.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.dto.system.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.ServerCapabilityType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class DtoRequestConfigSettingsPropsTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
    }

    private PayloadEnvelope<ConfigSettingsDTO> getConfigSettingsFromServer() throws Exception {

        GobiiClientContext.resetConfiguration();
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

        PayloadEnvelope<ConfigSettingsDTO> returnVal = null;

        RestUri confgSettingsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_CONFIGSETTINGS);
        GobiiEnvelopeRestResource<ConfigSettingsDTO,ConfigSettingsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(confgSettingsUri);
        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ConfigSettingsDTO.class);

        TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader());

        returnVal = resultEnvelope;



        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());


        return returnVal;

    }

    @Test
    public void testGetConfigSettings() throws Exception {

        GobiiClientContext.resetConfiguration();
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = getConfigSettingsFromServer();
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull(configSettingsDTOResponse);

        Assert.assertNotNull("The max upload size is null",
                configSettingsDTOResponse.getMaxUploadSizeMbytes());

        Assert.assertTrue("The max upload size bytes is not greater than zero",
                configSettingsDTOResponse.getMaxUploadSizeMbytes() > 0 );

        Assert.assertTrue(configSettingsDTOResponse.getServerConfigs().size() > 0);


        String randomCrop = new ArrayList<String>(configSettingsDTOResponse
                .getServerConfigs()
                .keySet())
                .get(0);


        ServerConfigItem serverConfigItemDefaultCrop = configSettingsDTOResponse
                .getServerConfigs()
                .get(randomCrop);

        Assert.assertNotNull("The remote server configuration does not define crop type",
                serverConfigItemDefaultCrop.getGobiiCropType());

        Assert.assertTrue("The default crop's crop ID does not match the settings default crop",
                randomCrop.equals(serverConfigItemDefaultCrop.getGobiiCropType()));

        Assert.assertNotNull("The remote server configuration does not define a domain for crop type " + serverConfigItemDefaultCrop.getGobiiCropType(),
                serverConfigItemDefaultCrop.getDomain());

        Assert.assertNotNull("The remote server configuration does not define a context root for crop type " + serverConfigItemDefaultCrop.getGobiiCropType(),
                serverConfigItemDefaultCrop.getContextRoot());

        Assert.assertNotNull("The remote server configuration does not define a port for crop type " + serverConfigItemDefaultCrop.getGobiiCropType(),
                serverConfigItemDefaultCrop.getPort());


        URL url = new URL("http",
                serverConfigItemDefaultCrop.getDomain(),
                serverConfigItemDefaultCrop.getPort(),
                serverConfigItemDefaultCrop.getContextRoot());

        String serviceUrl = url.toString();


        GobiiClientContext.resetConfiguration();

        GobiiClientContext.getInstance(serviceUrl, true);

        String testUser = GobiiClientContextAuth.getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = GobiiClientContextAuth.getTestExecConfig().getLdapPasswordForUnitTest();

        String testCrop = GobiiClientContextAuth.getTestExecConfig().getTestCrop();

        Assert.assertTrue("Unable to authenticate to remote server with default drop " + randomCrop,
                GobiiClientContext.getInstance(null, false).login(testCrop, testUser, testPassword));

        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());

    }

    @Test
    public void testWithCaseMisMatchedCropname() throws Exception {

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = getConfigSettingsFromServer();
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(configSettingsDTOResponse.getServerConfigs().size() > 0);

        String randomCrop = new ArrayList<String>(
                configSettingsDTOResponse.getServerConfigs().keySet()
        ).get(0);

        ServerConfigItem serverConfigItemDefaultCrop = configSettingsDTOResponse
                .getServerConfigs()
                .get(randomCrop);


        URL url = new URL("http",
                serverConfigItemDefaultCrop.getDomain(),
                serverConfigItemDefaultCrop.getPort(),
                serverConfigItemDefaultCrop.getContextRoot());

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

        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
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
        GobiiEnvelopeRestResource<PingDTO,PingDTO> gobiiEnvelopeRestResourcePingDTO = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_PING));

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


        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());

    } // testInitContextFromConfigSettings()


    @Test
    public void testGetServerCapabilities() throws Exception {

        // it is not possible for this test to verify the actual values against a
        // configuration file because the test may be running on a different sytem from the
        // one that the server is running on. So we are just going to verify that the
        // keys exist

        GobiiClientContext.resetConfiguration();
        ConfigSettings configSettings = new GobiiTestConfiguration().getConfigSettings();
        String initConfigUrl = configSettings.getTestExecConfig().getInitialConfigUrl();

        Map<ServerCapabilityType, Boolean> serverCapabilities = GobiiClientContext.getInstance(initConfigUrl, true)
                .getServerCapabilities();

        Assert.assertTrue("There is no capability setting for KDC",
                serverCapabilities.containsKey(ServerCapabilityType.KDC)
        );

        Assert.assertTrue("There is no capability setting for GOBII Backend",
                serverCapabilities.containsKey(ServerCapabilityType.GOBII_BACKEND)
        );

        Assert.assertTrue("There is no capability setting for BRAPI",
                serverCapabilities.containsKey(ServerCapabilityType.BRAPI)
        );

    }

}
