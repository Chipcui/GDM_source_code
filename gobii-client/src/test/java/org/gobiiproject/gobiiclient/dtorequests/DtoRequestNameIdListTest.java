// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class DtoRequestNameIdListTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestNameIdListTest.uriFactory = new UriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    private void testNameRetrieval(GobiiEntityNameType gobiiEntityNameType,
                                   GobiiFilterType gobiiFilterType,
                                   String filterValue) throws Exception {
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
        namesUri.setParamValue("entity", gobiiEntityNameType.toString().toLowerCase());

        if (GobiiFilterType.NONE != gobiiFilterType) {
            namesUri.setParamValue("filterType", StringUtils.capitalize(gobiiFilterType.toString().toUpperCase()));
            namesUri.setParamValue("filterValue", filterValue);
        }


        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        String assertionErrorStem = "Error testing name-id retrieval of entity "
                + gobiiEntityNameType.toString();

        if (GobiiFilterType.NONE != gobiiFilterType) {

            assertionErrorStem += " with filter type "
                    + gobiiFilterType.toString()
                    + " and filter value "
                    + filterValue;
        }

        assertionErrorStem += ": ";

        Assert.assertFalse(assertionErrorStem,
                TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(assertionErrorStem
                        + "null name id list",
                NameIdDTOList);

        Assert.assertTrue(assertionErrorStem
                        + "empty name id list",
                NameIdDTOList.size() > 0);

        Assert.assertNotNull(assertionErrorStem
                        + "null name",
                NameIdDTOList.get(0).getName());

        Assert.assertNotNull(assertionErrorStem
                        + "null ",
                NameIdDTOList.get(0).getId());

        Assert.assertTrue(assertionErrorStem
                        + "id <= 0",
                NameIdDTOList.get(0).getId() > 0);

    }


    @Test
    public void testGetAnalysisNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.NONE, null);

    } // testGetAnalysisNames()

    @Test
    public void testGetAnalysisNamesByTypeId() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.BYTYPEID, "33");

    }

    @Test
    public void testGetNamesWithBadEntityValue() throws Exception {

        // Assumes rice data with seed script is loaded
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);

        namesUri.setParamValue("entity", "foo");
        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for bad entity type",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("unsupported entity for list request"))
                        .count());

    }

    @Test
    public void testGetAnalysisNamesByTypeIdErrorBadFilterType() throws Exception {

        // Assumes rice data with seed script is loaded
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);

        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
        namesUri.setParamValue("filterType", "foo");
        namesUri.setParamValue("filterValue", "33");
        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for the unsupported filter type",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("unsupported filter"))
                        .count());

    }

    @Test
    public void testGetAnalysisNamesByTypeIdErrorEmptyFilterValue() throws Exception {

        // Assumes rice data with seed script is loaded
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);

        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPEID.toString().toUpperCase()));
        // normally would also specify "filterValue" here

        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for filter value not supplied",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("a value was not supplied for filter"))
                        .count());

    }


    @Test
    public void testGetContactsByIdForContactType() throws Exception {

        testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, "Curator");

    } // testGetMarkers()

    @Test
    public void testGetContactNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.NONE, null);


    } // testGetMarkers()

    @Test
    public void testGetProjectNames() throws Exception {


        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.NONE, null);

    } // testGetMarkers()


    @Test
    public void testGetProjectNamesByContactId() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.BYTYPEID, "2");
    }

    @Test
    public void testGetExperimentNamesByProjectId() throws Exception {


        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.BYTYPEID, "1");

    }

    @Test
    public void testGetExperimentNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.EXPERIMENTS, GobiiFilterType.NONE, null);

    }

    @Test
    public void testGetCvTermsByGroup() throws Exception {

        // Assumes rice data with seed script is loaded

        testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "strand");

    }

    @Test
    public void testGetPlatformNames() throws Exception {

        // Assumes rice data with seed script is loaded
        //testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.NONE, null);
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
        namesUri.setParamValue("entity", GobiiEntityNameType.PLATFORMS.toString().toLowerCase());

        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        List<NameIdDTO> nameIdDTOList = resultEnvelope.getPayload().getData();

        String assertionErrorStem = "Error retrieving Platform Names: ";

        Assert.assertFalse(assertionErrorStem,
                TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        Assert.assertNotNull(assertionErrorStem
                        + "null name id list",
                nameIdDTOList);

        Assert.assertTrue(assertionErrorStem
                        + "empty name id list",
                nameIdDTOList.size() > 0);

        Assert.assertNotNull(assertionErrorStem
                        + "null name",
                nameIdDTOList.get(0).getName());

        Assert.assertNotNull(assertionErrorStem
                        + "null ",
                nameIdDTOList.get(0).getId());

        Assert.assertTrue(assertionErrorStem
                        + "id <= 0",
                nameIdDTOList.get(0).getId() > 0);


        // verify that we can retrieve platofrmDtos from the links we got for the platform name IDs
        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        List<Integer> itemsToTest = TestUtils.makeListOfIntegersInRange(10, nameIdDTOList.size());
        for (Integer currentIdx : itemsToTest) {

            NameIdDTO currentPlatformNameDto = nameIdDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);
            RestUri restUriPlatformForGetById = DtoRequestNameIdListTest
                    .uriFactory
                    .RestUriFromUri(currentLink.getHref());
            RestResource<PlatformDTO> restResourceForGetById = new RestResource<>(restUriPlatformForGetById);
            PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(PlatformDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            PlatformDTO platformDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);

            Assert.assertTrue(currentPlatformNameDto.getName().equals(platformDTOFromLink.getPlatformName()));
            Assert.assertTrue(currentPlatformNameDto.getId().equals(platformDTOFromLink.getPlatformId()));
        }


    } // testGetMarkers()

    @Test
    public void testGetPlatformNamesByTypeId() throws Exception {
        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PLATFORMS, GobiiFilterType.BYTYPEID, "1");

    } // testGetMarkers()


    @Test
    public void testGetMarkerGroupNames() throws Exception {

        testNameRetrieval(GobiiEntityNameType.MARKERGROUPS, GobiiFilterType.NONE, null);

    } // testGetMarkerGroupNames()

    @Test
    public void testGetReferenceNames() throws Exception {


        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.REFERENCES, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetMapNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetMapsSetNamesByType() throws Exception {
        testNameRetrieval(GobiiEntityNameType.MAPSETS, GobiiFilterType.BYTYPEID, "19");

    } // testGetMarkers()


    @Test
    public void testGetCvTypes() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.CVGROUPS, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetCvNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.NONE, null);

    } // testGetMarkers()

    @Test
    public void testGetRoles() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.ROLES,GobiiFilterType.NONE,null);
    } // testGetMarkers()


    @Test
    public void testGetManifestNames() throws Exception {

        testNameRetrieval(GobiiEntityNameType.MANIFESTS, GobiiFilterType.NONE, null);

    }

    @Test
    public void testGetOrganizationNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.ORGANIZATIONS, GobiiFilterType.NONE, null);

    }

    @Test
    public void testGetDataSetNamesByExperimentId() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.BYTYPEID, "2");

    }

    @Test
    public void testGetDataSetNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.NONE, null);
    }
}
