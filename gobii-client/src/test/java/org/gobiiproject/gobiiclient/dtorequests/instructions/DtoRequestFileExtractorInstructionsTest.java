package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Collectors;

/**
 * Created by Phil on 6/15/2016.
 */
public class DtoRequestFileExtractorInstructionsTest {

    private static UriFactory uriFactory;


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        uriFactory = new UriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testSendExtractorInstructionFile() throws Exception {


        String cropTypeFromContext = ClientContext.getInstance(null,false).getCurrentClientCropType();

        // ************** DEFINE DTO
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();


        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();
        extractorInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);


        // ************** INSTRUCTION ONE
        GobiiExtractorInstruction gobiiExtractorInstructionOne = new GobiiExtractorInstruction();
        gobiiExtractorInstructionOne.setContactId(1);
        gobiiExtractorInstructionOne.setGobiiCropType(cropTypeFromContext);

        // ************** DATA SET EXTRACT ONE
        GobiiDataSetExtract gobiiDataSetExtractOne = new GobiiDataSetExtract();
        GobiiFileType DataSetExtractOneFileType = GobiiFileType.GENERIC;
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        String DataSetExtractOneName = "my_foo_Dataset";
        gobiiDataSetExtractOne.setDataSetName(DataSetExtractOneName);
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setDataSetId(1);



        // ************** DATA SET EXTRACT two
        GobiiDataSetExtract gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        GobiiFileType DataSetExtractFileTypeTwo = GobiiFileType.GENERIC;
        gobiiDataSetExtractTwo.setGobiiFileType(DataSetExtractFileTypeTwo);
        String DataSetExtractNameTwo = "my_foo_Dataset2";
        gobiiDataSetExtractTwo.setDataSetName(DataSetExtractNameTwo);
        gobiiDataSetExtractTwo.setAccolate(true);
        gobiiDataSetExtractTwo.setDataSetId(1);


        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionOne);


        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        GobiiExtractorInstruction gobiiExtractorInstructionTwo = new GobiiExtractorInstruction();
        gobiiExtractorInstructionTwo.setContactId(1);
        // gobiiExtractorInstructionTwo.setGobiiCropType(cropTypeFromContext); // DON'T SET IT # 2

        // column one
        gobiiDataSetExtractOne = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setDataSetName("my_foo_2Dataset");
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        gobiiDataSetExtractOne.setDataSetId(1);

        // column two
        gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        gobiiDataSetExtractTwo.setDataSetName("my_foo_2Dataset2");
        gobiiDataSetExtractTwo.setAccolate(true);
        gobiiDataSetExtractTwo.setGobiiFileType(DataSetExtractFileTypeTwo);
        gobiiDataSetExtractTwo.setDataSetId(1);

        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionTwo);

        PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        RestResource<ExtractorInstructionFilesDTO> restResourceForPost = new RestResource<>(uriFactory
                .resourceColl(ServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS));
        PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = restResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);

        //DtoRequestFileExtractorInstructions dtoRequestFileExtractorInstructions = new DtoRequestFileExtractorInstructions();
        //ExtractorInstructionFilesDTO extractorInstructionFilesDTOResponse = dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, extractorInstructionFileDTOResponseEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFileDTOResponseEnvelope.getHeader()));
        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE, IMPLICITLY TESTING LINK

        LinkCollection linkCollection = extractorInstructionFileDTOResponseEnvelope.getPayload().getLinkCollection();
        Assert.assertNotNull(linkCollection);
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == 1);


        RestUri restUriFromLink = uriFactory.RestUriFromUri(linkCollection.getLinksPerDataItem().get(0).getHref());
        RestResource<ExtractorInstructionFilesDTO> restResourceForGet = new RestResource<>(restUriFromLink);
        PayloadEnvelope<ExtractorInstructionFilesDTO> resultEnvelope = restResourceForGet
                .get(ExtractorInstructionFilesDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ExtractorInstructionFilesDTO extractorInstructionFilesDTOretrieveResponse = resultEnvelope.getPayload().getData().get(0);

        for( GobiiExtractorInstruction currentExtractorInstruction : extractorInstructionFilesDTOretrieveResponse.getGobiiExtractorInstructions()) {
            Assert.assertTrue("The sent crop type of the retrieved crop types does not match",
                    currentExtractorInstruction.getGobiiCropType().equals(cropTypeFromContext));
        }

        Assert.assertTrue(
                2 == extractorInstructionFilesDTOretrieveResponse
                        .getGobiiExtractorInstructions()
                        .size()
        );

        Assert.assertTrue(
                extractorInstructionFilesDTOretrieveResponse
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0)
                        .getDataSetName()
                        .equals(DataSetExtractOneName)
        );


        // Test link we got from GET
        linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertNotNull(linkCollection);
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == 1);
        RestUri newRestUriFromLink = uriFactory.RestUriFromUri(linkCollection.getLinksPerDataItem().get(0).getHref());
        restResourceForGet = new RestResource<>(newRestUriFromLink);
        resultEnvelope = restResourceForGet
                .get(ExtractorInstructionFilesDTO.class);

        ExtractorInstructionFilesDTO extractorInstructionFilesDTOFromSecondRetrieval = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(
                2 == extractorInstructionFilesDTOFromSecondRetrieval
                        .getGobiiExtractorInstructions()
                        .size()
        );

        Assert.assertTrue(
                extractorInstructionFilesDTOFromSecondRetrieval
                        .getGobiiExtractorInstructions()
                        .get(0)
                        .getDataSetExtracts()
                        .get(0)
                        .getDataSetName()
                        .equals(DataSetExtractOneName)
        );

        // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality

//        ExtractorInstructionFilesDTO testForuserInputFileExistsError =
//                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);

        payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        extractorInstructionFileDTOResponseEnvelope = restResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);


        Assert.assertTrue(1 ==
                extractorInstructionFileDTOResponseEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.VALIDATION_NOT_UNIQUE))
                        .collect(Collectors.toList())
                        .size());


        //testGetExtractorInstructionStatus(

        RestUri restUriExtractorInstructionsForGetByFilename = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_FILE_EXTRACTOR_STATUS);
        restUriExtractorInstructionsForGetByFilename.setParamValue("id", extractorInstructionFilesDTOFromSecondRetrieval.getInstructionFileName());
        RestResource<ExtractorInstructionFilesDTO> restResourceForGetById = new RestResource<>(restUriExtractorInstructionsForGetByFilename);
        PayloadEnvelope<ExtractorInstructionFilesDTO> resultEnvelopeForGetStatusByFileName = restResourceForGetById
                .get(ExtractorInstructionFilesDTO.class);

            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetStatusByFileName.getHeader()));
            ExtractorInstructionFilesDTO resultExtractorInstructionFilesDTO = resultEnvelopeForGetStatusByFileName.getPayload().getData().get(0);
            Assert.assertNotNull(resultExtractorInstructionFilesDTO.getGobiiJobStatus());
        }


    }

