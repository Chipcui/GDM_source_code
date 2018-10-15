package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by VCalaminos on 1/15/2018.
 */
public class DtoCrudRequestNameIdListTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    private PayloadEnvelope<CvDTO> createCv(CvDTO newCvDTO) throws Exception {

        PayloadEnvelope<CvDTO> cvCreatePayloadEnvelope = new PayloadEnvelope<>(newCvDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<CvDTO,CvDTO> cvCreateGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_CV));
        PayloadEnvelope<CvDTO> cvCreateResultEnvelope = cvCreateGobiiEnvelopeRestResource.post(CvDTO.class,
                cvCreatePayloadEnvelope);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvCreateResultEnvelope.getHeader()));
        CvDTO cvDTOResponse = cvCreateResultEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertTrue(cvDTOResponse.getCvId() > 0);

        return cvCreateResultEnvelope;

    }

    private void checkNameIdListResponseAll(PayloadEnvelope<NameIdDTO> responsePayloadEnvelope, List<NameIdDTO> nameIdDTOListInput, String[] nameIdDTONotExisting) {

        // Convert String Array to List
        List<String> nameIdDTONotExistingList = Arrays.asList(nameIdDTONotExisting);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(responsePayloadEnvelope.getHeader()));

        List<NameIdDTO> nameIdDTOListResponse = responsePayloadEnvelope.getPayload().getData();
        Assert.assertNotEquals(null, nameIdDTOListResponse);
        Assert.assertEquals(nameIdDTOListInput.size(), nameIdDTOListResponse.size());

        for (NameIdDTO currentNameIdDTO : nameIdDTOListResponse) {

            if (nameIdDTONotExistingList.contains(currentNameIdDTO.getName())){

                Assert.assertTrue(currentNameIdDTO.getId() <= 0);
            } else {

                Assert.assertTrue(currentNameIdDTO.getId() > 0);

            }
        }
    }

    private void checkNameIdListResponseAbsent(PayloadEnvelope<NameIdDTO> responsePayloadEnvelope, String[] nameIdDTOListInput) {

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(responsePayloadEnvelope.getHeader()));

        List<NameIdDTO> nameIdDTOListResponse = responsePayloadEnvelope.getPayload().getData();
        Assert.assertNotEquals(null, nameIdDTOListResponse);
        Assert.assertEquals(nameIdDTOListInput.length, nameIdDTOListResponse.size());

        for (NameIdDTO nameIdDTO : nameIdDTOListResponse) {

            Assert.assertTrue(nameIdDTO.getId() == 0);

        }

    }

    private void checkNameIdListResponseExists(PayloadEnvelope<NameIdDTO> responsePayloadEnvelope, String[] nameIdDTOListInput) {

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(responsePayloadEnvelope.getHeader()));

        List<NameIdDTO> nameIdDTOListResponse = responsePayloadEnvelope.getPayload().getData();
        Assert.assertNotEquals(null, nameIdDTOListResponse);
        Assert.assertEquals(nameIdDTOListInput.length, nameIdDTOListResponse.size());

        for (NameIdDTO nameIdDTO : nameIdDTOListResponse) {

            Assert.assertTrue(nameIdDTO.getId() > 0);
        }

    }

    private Integer getCvGroupIdByGroupName(String cvGroupName) throws Exception {

        // get cvGroupId
        RestUri restUriCvGroupDetails = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .cvGroupByQueryParams()
                .setParamValue("groupName", cvGroupName)
                .setParamValue("cvGroupTypeId", GobiiCvGroupType.GROUP_TYPE_USER.getGroupTypeId().toString());

        GobiiEnvelopeRestResource<CvGroupDTO,CvGroupDTO> cvGroupDTOGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriCvGroupDetails);
        PayloadEnvelope<CvGroupDTO> cvGroupDTOResultEnvelope = cvGroupDTOGobiiEnvelopeRestResource.get(CvGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvGroupDTOResultEnvelope.getHeader()));
        CvGroupDTO cvGroupDTO = cvGroupDTOResultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue(cvGroupDTO.getCvGroupId() > 0);
        Assert.assertEquals(cvGroupDTO.getName(), cvGroupName);

        Integer cvGroupId = cvGroupDTO.getCvGroupId();

        return cvGroupId;
    }

    private MarkerDTO createMarkerForTest(MarkerDTO markerDTO) throws Exception {

        RestUri createMarkerUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_MARKERS);
        GobiiEnvelopeRestResource<MarkerDTO,MarkerDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(createMarkerUri);
        PayloadEnvelope<MarkerDTO> resultEnvelope = gobiiEnvelopeRestResource
                .post(MarkerDTO.class, new PayloadEnvelope<>(markerDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, markerDTOResponse);
        Assert.assertNotNull(markerDTOResponse.getMarkerName());
        Assert.assertNotNull(markerDTOResponse.getPlatformId());
        Assert.assertTrue(markerDTOResponse.getPlatformId() > 0);
        Assert.assertTrue(markerDTOResponse.getMarkerId() > 0);

        return markerDTOResponse;
    }

    private PlatformDTO createPlatformForTest() throws Exception {

        PlatformDTO platformDTO = new PlatformDTO();
        platformDTO.setPlatformName(UUID.randomUUID().toString());
        platformDTO.setPlatformCode(UUID.randomUUID().toString());
        platformDTO.setTypeId(1);
        platformDTO.setStatusId(1);
        platformDTO.setCreatedBy(1);

        RestUri createPlatformUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_PLATFORM);
        PayloadEnvelope<PlatformDTO> payloadEnvelope = new PayloadEnvelope<>(platformDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<PlatformDTO,PlatformDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(createPlatformUri);
        PayloadEnvelope<PlatformDTO> responseEnvelope = gobiiEnvelopeRestResource.post(PlatformDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(responseEnvelope.getHeader()));
        PlatformDTO platformDTOResponse = responseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, platformDTOResponse);
        Assert.assertTrue(platformDTOResponse.getPlatformId() > 0);
        Assert.assertNotNull(platformDTOResponse.getPlatformName());

        return platformDTOResponse;
    }

    private List<CvDTO> createCvTerms(Integer cvGroupId) throws Exception {
       return createCvTerms(cvGroupId, false);
    }

    private List<CvDTO> createCvTerms(Integer cvGroupId, Boolean withDuplicates) throws Exception {

        // create list of cv terms

        CvDTO newCvDTO1 = TestDtoFactory.makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        newCvDTO1.setGroupId(cvGroupId);

        CvDTO newCvDTO2 = TestDtoFactory.makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        newCvDTO2.setGroupId(cvGroupId);

        CvDTO newCvDTO3 = TestDtoFactory.makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        newCvDTO3.setGroupId(cvGroupId);

        RestUri restUriCvGroup = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_CVGROUP)
                .addUriParam("groupId")
                .setParamValue("groupId", cvGroupId.toString())
                .appendSegment(RestResourceId.GOBII_CV);

        GobiiEnvelopeRestResource<CvDTO,CvDTO> cvDTOGobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriCvGroup);
        PayloadEnvelope<CvDTO> cvDTOResultEnvelope = cvDTOGobiiEnvelopeRestResource.get(CvDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResultEnvelope.getHeader()));
        List<CvDTO> existingCvDTOList = cvDTOResultEnvelope.getPayload().getData();

        List<String> existingCvTerms = new ArrayList<>();

        if (existingCvDTOList.size() > 0) {

            for (CvDTO currentCvDTO : existingCvDTOList) {
                if (!currentCvDTO.getTerm().equals(null)) {
                    existingCvTerms.add(currentCvDTO.getTerm());
                }
            }
        }

        // check if cv terms already exists; if FALSE, create new CvDTO

        if (!existingCvTerms.contains(newCvDTO1.getTerm())) {

            createCv(newCvDTO1);
        }

        if (!existingCvTerms.contains(newCvDTO2.getTerm())) {

            createCv(newCvDTO2);
        }

        if (!existingCvTerms.contains(newCvDTO3.getTerm())) {
            createCv(newCvDTO3);
        }

        List<CvDTO> cvDTOList = new ArrayList<>();
        cvDTOList.add(newCvDTO1);

        if (withDuplicates) {
            cvDTOList.add(newCvDTO1);
        }

        cvDTOList.add(newCvDTO2);
        cvDTOList.add(newCvDTO3);

        return cvDTOList;
    }

    private List<NameIdDTO> createNameIdDTOList(List<CvDTO> cvDTOList) {

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();

        for (CvDTO cvDTO : cvDTOList) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(cvDTO.getTerm());

            nameIdDTOList.add(nameIdDTO);
        }

        return nameIdDTOList;

    }


    private PayloadEnvelope<NameIdDTO> getNamesByNameList(List<NameIdDTO> nameIdDTOList, GobiiEntityNameType gobiiEntityNameType, GobiiFilterType gobiiFilterType, String filterValue) throws Exception {

        PayloadEnvelope<NameIdDTO> payloadEnvelope = new PayloadEnvelope<>();
        payloadEnvelope.getHeader().setGobiiProcessType(GobiiProcessType.CREATE);
        payloadEnvelope.getPayload().setData(nameIdDTOList);

        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO,NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", gobiiEntityNameType.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(gobiiFilterType.toString().toUpperCase()));
        namesUri.setParamValue("filterValue", filterValue);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = gobiiEnvelopeRestResource.post(NameIdDTO.class, payloadEnvelope);

        return responsePayloadEnvelope;
    }


    private CvDTO createNotExistingCv() {

        String notExistingCvTerm = "notexisting_cvterm-" + UUID.randomUUID().toString();
        CvDTO notExistingCvDTO = new CvDTO();
        notExistingCvDTO.setTerm(notExistingCvTerm);

        return notExistingCvDTO;
    }

    @Test
    public void testGetCvTermsForGermplasmSpeciesAndNameList() throws Exception {

        String cvGroupName = CvGroup.GERMPLASM_SPECIES.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(cvGroupName);

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId);

        CvDTO notExistingCvDto = createNotExistingCv();

        List<CvDTO> cvDTOListInput = new ArrayList<>();
        cvDTOListInput.add(notExistingCvDto);
        cvDTOListInput.addAll(cvDTOList);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOListInput);

        String[] nameAbsent = new String[]{notExistingCvDto.getTerm()};

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, GobiiFilterType.NAMES_BY_NAME_LIST, cvGroupName);

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, nameAbsent);
    }

    @Test
    public void testGetCvTermsForGermplasmTypeAndNameList() throws Exception {

        String cvGroupName = CvGroup.GERMPLASM_TYPE.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(cvGroupName);

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId);

        CvDTO notExistingCvDto = createNotExistingCv();
        cvDTOList.add(notExistingCvDto);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        String[] nameAbsent = new String[]{notExistingCvDto.getTerm()};

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, GobiiFilterType.NAMES_BY_NAME_LIST, cvGroupName);

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, nameAbsent);

    }

    @Test
    public void testGetCvTermsForMarkerStrandAndNameList() throws Exception {

        String cvGroupName = CvGroup.MARKER_STRAND.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(CvGroup.MARKER_STRAND.getCvGroupName());

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId);

        CvDTO notExistingCvDto = createNotExistingCv();
        cvDTOList.add(notExistingCvDto);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        String[] nameAbsent = new String[]{notExistingCvDto.getTerm()};

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, GobiiFilterType.NAMES_BY_NAME_LIST, cvGroupName);

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, nameAbsent);
    }


    @Test
    public void testGetReferencesByNameList() throws Exception {

        ReferenceDTO newReferenceDto1 = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);
        newReferenceDto1.setName(UUID.randomUUID().toString());

        ReferenceDTO newReferenceDto2 = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);
        newReferenceDto2.setName(UUID.randomUUID().toString());

        ReferenceDTO newReferenceDto3 = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);
        newReferenceDto3.setName(UUID.randomUUID().toString());

        ReferenceDTO notExistingReferenceDto = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);

        // create reference 1
        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference1 = new PayloadEnvelope<>(newReferenceDto1, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO,ReferenceDTO> gobiiEnvelopeRestResourceReference1 = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTO1ResponseEnvelope = gobiiEnvelopeRestResourceReference1.post(ReferenceDTO.class,
                payloadEnvelopeReference1);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTO1ResponseEnvelope.getHeader()));

        ReferenceDTO referenceDTO1Response = referenceDTO1ResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, referenceDTO1Response);
        Assert.assertTrue(referenceDTO1Response.getReferenceId() > 0);

        // create reference 2
        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference2 = new PayloadEnvelope<>(newReferenceDto2, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO,ReferenceDTO> gobiiEnvelopeRestResourceReference2 = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTO2ResponseEnvelope = gobiiEnvelopeRestResourceReference2.post(ReferenceDTO.class,
                payloadEnvelopeReference2);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTO2ResponseEnvelope.getHeader()));

        ReferenceDTO referenceDTO2Response = referenceDTO2ResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, referenceDTO2Response);
        Assert.assertTrue(referenceDTO2Response.getReferenceId() > 0);

        // create reference 3
        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference3 = new PayloadEnvelope<>(newReferenceDto3, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO,ReferenceDTO> gobiiEnvelopeRestResourceReference3 = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestResourceId.GOBII_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTO3ResponseEnvelope = gobiiEnvelopeRestResourceReference3.post(ReferenceDTO.class,
                payloadEnvelopeReference3);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTO3ResponseEnvelope.getHeader()));

        ReferenceDTO referenceDTO3Response = referenceDTO3ResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, referenceDTO3Response);
        Assert.assertTrue(referenceDTO3Response.getReferenceId() > 0);

        // create NameIdDTOs; 2 existing in the database, and 1 not existing

        NameIdDTO nameIdDTO1 = new NameIdDTO();
        nameIdDTO1.setName(newReferenceDto1.getName());

        NameIdDTO nameIdDTO2 = new NameIdDTO();
        nameIdDTO2.setName(newReferenceDto2.getName());

        NameIdDTO nameIdDTO3 = new NameIdDTO();
        nameIdDTO3.setName(newReferenceDto3.getName());

        String notExistingReference = "notexisting_reference-" + UUID.randomUUID().toString();

        String[] nameAbsent = new String[]{notExistingReference};

        NameIdDTO nameIdDTONotExisting = new NameIdDTO();
        nameIdDTONotExisting.setName(notExistingReference);

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();
        nameIdDTOList.add(nameIdDTONotExisting);
        nameIdDTOList.add(nameIdDTO1);
        nameIdDTOList.add(nameIdDTO2);
        nameIdDTOList.add(nameIdDTO3);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.REFERENCE, GobiiFilterType.NAMES_BY_NAME_LIST, "test");

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, nameAbsent);

    }


    @Test
    public void testWithDuplicateNames() throws Exception {

        String cvGroupName = CvGroup.GERMPLASM_SPECIES.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(cvGroupName);

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId, true);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, GobiiFilterType.NAMES_BY_NAME_LIST, cvGroupName);


        Assert.assertTrue("The error message should contain 'There were duplicate values in the list'",
                responsePayloadEnvelope.getHeader()
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getMessage().contains("There were duplicate values in the list"))
                .count()
                > 0);

        Assert.assertTrue(responsePayloadEnvelope.getPayload().getData().size() == 0);

    }

    @Ignore
    public void testGetDnaSampleNamesByList() throws Exception {

        Integer projectId = 1;
        GobiiEntityNameType gobiiEntityNameType = GobiiEntityNameType.DNA_SAMPLE;

        String[] dnasampleNamesExisting = new String[]{"dnasample_rza_codom_10","dnasample_rza_codom_11","dnasample_rza_codom_12","dnasample_rza_codom_13"};

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();

        String[] dnasampleNamesAbsent = new String[]{"notdnasample1","notdnasample2","notdnasample3"};

        for (String dnaSampleName : dnasampleNamesAbsent) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(dnaSampleName);

            nameIdDTOList.add(nameIdDTO);
        }

        for (String dnaSampleName : dnasampleNamesExisting) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(dnaSampleName);

            nameIdDTOList.add(nameIdDTO);
        }


        /**
         * Test get DNA Sample Names by NAMES_BY_NAME_LIST
         * */
        GobiiFilterType gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST;

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, projectId.toString());

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, dnasampleNamesAbsent);

        /**
         * Test get DNA Sample Names by NAMES_BY_NAME_LIST_RETURN_ABSENT
         * Will return items on the list that are not found in the database
         */
        gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT;

        responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, projectId.toString());

        checkNameIdListResponseAbsent(responsePayloadEnvelope, dnasampleNamesAbsent);


        /**
         * Test get DNA Sample Names by NAMES_BY_NAMES_LIST_RETURN_EXISTS
         * Will return items on the list that are existing in the database
         */
        gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS;

        responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, projectId.toString());

        checkNameIdListResponseExists(responsePayloadEnvelope, dnasampleNamesExisting);

    }

    @Ignore
    public void testGetMarkerNamesByList() throws Exception {

        // create markers for test

        MarkerDTO markerDTO1 = TestDtoFactory
                .makeMarkerDTO(UUID.randomUUID().toString());

        Integer platformId = markerDTO1.getPlatformId();

        MarkerDTO markerDTO2 = TestDtoFactory
                .makeMarkerDTO(UUID.randomUUID().toString());
        markerDTO2.setPlatformId(platformId);

        MarkerDTO markerDTO3 = TestDtoFactory
                .makeMarkerDTO(UUID.randomUUID().toString());
        markerDTO3.setPlatformId(platformId);


        markerDTO1 = createMarkerForTest(markerDTO1);
        markerDTO2 = createMarkerForTest(markerDTO2);
        markerDTO3 = createMarkerForTest(markerDTO3);

        GobiiEntityNameType gobiiEntityNameType = GobiiEntityNameType.MARKER;

        String[] markerNameExisting = new String[]{markerDTO1.getMarkerName(), markerDTO2.getMarkerName(), markerDTO3.getMarkerName()};

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();


        String[] markerNameAbsent = new String[]{"absent-marker1", "absent-marker2", "absent-marker3"};

        for (String markerName : markerNameAbsent) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(markerName);

            nameIdDTOList.add(nameIdDTO);
        }

        for (String markerName : markerNameExisting) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(markerName);

            nameIdDTOList.add(nameIdDTO);
        }


        /**
         * Test get Marker names by NAMES_BY_NAME_LIST
         */
        GobiiFilterType gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST;

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, platformId.toString());

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, markerNameAbsent);

        /**
         * Test get Marker names by NAMES_BY_NAME_LIST_RETURN_ABSENT
         * Will return items on the list that are not found in the database
         */
        gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT;

        responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, platformId.toString());

        checkNameIdListResponseAbsent(responsePayloadEnvelope, markerNameAbsent);

        /**
         * Test get Marker names by NAMES_BY_NAME_LIST_RETURN_EXISTS
         */
        gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS;

        responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, platformId.toString());

        checkNameIdListResponseExists(responsePayloadEnvelope, markerNameExisting);

    }

    @Test
    public void testGetPlatformNamesByList() throws Exception {

        // create platforms for test
        PlatformDTO platformDTO1 = createPlatformForTest();
        PlatformDTO platformDTO2 = createPlatformForTest();
        PlatformDTO platformDTO3 = createPlatformForTest();

        GobiiEntityNameType gobiiEntityNameType = GobiiEntityNameType.PLATFORM;

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();

        String[] platformNameAbsent = new String[]{"absent-platform1", "absent-platform2", "absent-platform3"};

        for (String platformName : platformNameAbsent) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(platformName);

            nameIdDTOList.add(nameIdDTO);

        }

        String[] platformNameExisting = new String[]{platformDTO1.getPlatformName(), platformDTO2.getPlatformName(), platformDTO3.getPlatformName()};

        for (String platformName : platformNameExisting) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(platformName);

            nameIdDTOList.add(nameIdDTO);
        }

        GobiiFilterType gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST;

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, "test");

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, platformNameAbsent);

    }

    @Ignore
    public void testGetLinkageGroupNamesByList() throws Exception {

        Integer mapsetId = 1;

        GobiiEntityNameType gobiiEntityNameType = GobiiEntityNameType.LINKAGE_GROUP;

        String[] linkageGroupNameExisting = new String[]{"lg1", "lg2", "lg3", "lg4"};

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();

        String[] linkageGroupNameAbsent = new String[]{"notlg1", "notlg2", "notlg3"};

        for (String lgName : linkageGroupNameAbsent) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(lgName);

            nameIdDTOList.add(nameIdDTO);
        }

        for (String lgName : linkageGroupNameExisting) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(lgName);

            nameIdDTOList.add(nameIdDTO);
        }


        GobiiFilterType gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST;

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, mapsetId.toString());

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, linkageGroupNameAbsent);

    }

    @Ignore
    public void testGetDnaRunNamesByList() throws Exception {

        Integer experimentId = 1;
        Integer dnasample_id = 1;

        GobiiEntityNameType gobiiEntityNameType = GobiiEntityNameType.DNA_RUN;

        String[] dnaRunNameExisting = new String[]{"dnarun1", "dnarun2", "dnarun3", "dnarun4"};

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();

        String[] dnaRunNameAbsent = new String[]{"notdnarun1", "notdnarun2", "notdnarun3"};

        for (String dnaRunName : dnaRunNameAbsent) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(dnaRunName);

            nameIdDTOList.add(nameIdDTO);
        }

        for (String dnaRunName : dnaRunNameExisting) {

            NameIdDTO nameIdDTO = new NameIdDTO();
            nameIdDTO.setName(dnaRunName);

            nameIdDTOList.add(nameIdDTO);
        }

        GobiiFilterType gobiiFilterType = GobiiFilterType.NAMES_BY_NAME_LIST;

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, gobiiEntityNameType, gobiiFilterType, experimentId.toString());

        checkNameIdListResponseAll(responsePayloadEnvelope, nameIdDTOList, dnaRunNameAbsent);
    }

}
