package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
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
                .resourceColl(GobiiServiceRequestId.URL_CV));
        PayloadEnvelope<CvDTO> cvCreateResultEnvelope = cvCreateGobiiEnvelopeRestResource.post(CvDTO.class,
                cvCreatePayloadEnvelope);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvCreateResultEnvelope.getHeader()));
        CvDTO cvDTOResponse = cvCreateResultEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertTrue(cvDTOResponse.getCvId() > 0);

        return cvCreateResultEnvelope;

    }

    private void checkNameIdListResponse(PayloadEnvelope<NameIdDTO> responsePayloadEnvelope, List<NameIdDTO> nameIdDTOListInput, String nameIdDTONotExisting) {

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(responsePayloadEnvelope.getHeader()));

        List<NameIdDTO> nameIdDTOListResponse = responsePayloadEnvelope.getPayload().getData();
        Assert.assertNotEquals(null, nameIdDTOListResponse);
        Assert.assertEquals(nameIdDTOListInput.size(), nameIdDTOListResponse.size());

        for (NameIdDTO currentNameIdDTO : nameIdDTOListResponse) {

            if (currentNameIdDTO.getName().equals(nameIdDTONotExisting)) {

                Assert.assertTrue(currentNameIdDTO.getId() <= 0);
            } else {

                Assert.assertTrue(currentNameIdDTO.getId() > 0);

            }
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
                .resourceColl(GobiiServiceRequestId.URL_CVGROUP)
                .addUriParam("groupId")
                .setParamValue("groupId", cvGroupId.toString())
                .appendSegment(GobiiServiceRequestId.URL_CV);

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


    private PayloadEnvelope<NameIdDTO> getNamesByNameList(List<NameIdDTO> nameIdDTOList, GobiiEntityNameType gobiiEntityNameType, String cvGroupName) throws Exception {

        PayloadEnvelope<NameIdDTO> payloadEnvelope = new PayloadEnvelope<>();
        payloadEnvelope.getHeader().setGobiiProcessType(GobiiProcessType.CREATE);
        payloadEnvelope.getPayload().setData(nameIdDTOList);

        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO, NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", gobiiEntityNameType.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_NAME_LIST.toString().toUpperCase()));
        namesUri.setParamValue("filterValue", cvGroupName);

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

        String cvGroupName = CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(cvGroupName);

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId);

        CvDTO notExistingCvDto = createNotExistingCv();
        cvDTOList.add(notExistingCvDto);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, cvGroupName);

        checkNameIdListResponse(responsePayloadEnvelope, nameIdDTOList, notExistingCvDto.getTerm());
    }

    @Test
    public void testGetCvTermsForGermplasmTypeAndNameList() throws Exception {

        String cvGroupName = CvGroup.CVGROUP_GERMPLASM_TYPE.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(cvGroupName);

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId);

        CvDTO notExistingCvDto = createNotExistingCv();
        cvDTOList.add(notExistingCvDto);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, cvGroupName);

        checkNameIdListResponse(responsePayloadEnvelope, nameIdDTOList, notExistingCvDto.getTerm());

    }

    @Test
    public void testGetCvTermsForMarkerStrandAndNameList() throws Exception {

        String cvGroupName = CvGroup.CVGROUP_MARKER_STRAND.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(CvGroup.CVGROUP_MARKER_STRAND.getCvGroupName());

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId);

        CvDTO notExistingCvDto = createNotExistingCv();
        cvDTOList.add(notExistingCvDto);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, cvGroupName);

        checkNameIdListResponse(responsePayloadEnvelope, nameIdDTOList, notExistingCvDto.getTerm());
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
                .resourceColl(GobiiServiceRequestId.URL_REFERENCE));
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
                .resourceColl(GobiiServiceRequestId.URL_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTO2ResponseEnvelope = gobiiEnvelopeRestResourceReference2.post(ReferenceDTO.class,
                payloadEnvelopeReference2);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTO2ResponseEnvelope.getHeader()));

        ReferenceDTO referenceDTO2Response = referenceDTO2ResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, referenceDTO2Response);
        Assert.assertTrue(referenceDTO2Response.getReferenceId() > 0);

        // create reference 3
        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference3 = new PayloadEnvelope<>(newReferenceDto3, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO, ReferenceDTO> gobiiEnvelopeRestResourceReference3 = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_REFERENCE));
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

        NameIdDTO nameIdDTONotExisting = new NameIdDTO();
        nameIdDTONotExisting.setName(notExistingReference);

        List<NameIdDTO> nameIdDTOList = new ArrayList<>();
        nameIdDTOList.add(nameIdDTO1);
        nameIdDTOList.add(nameIdDTO2);
        nameIdDTOList.add(nameIdDTO3);
        nameIdDTOList.add(nameIdDTONotExisting);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.REFERENCE, "test");

        checkNameIdListResponse(responsePayloadEnvelope, nameIdDTOList, notExistingReference);

    }


    @Test
    public void testWithDuplicateNames() throws Exception {

        String cvGroupName = CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName();
        Integer cvGroupId = getCvGroupIdByGroupName(cvGroupName);

        List<CvDTO> cvDTOList = createCvTerms(cvGroupId, true);

        List<NameIdDTO> nameIdDTOList = createNameIdDTOList(cvDTOList);

        PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = getNamesByNameList(nameIdDTOList, GobiiEntityNameType.CV, cvGroupName);


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

}
