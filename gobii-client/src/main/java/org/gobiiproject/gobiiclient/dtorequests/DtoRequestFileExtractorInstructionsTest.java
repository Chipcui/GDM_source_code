// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Angel Raquel
// Create Date:   2016-06-08
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Collectors;

public class DtoRequestFileExtractorInstructionsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testSendInstructionFile() throws Exception {

        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();


        ClientContext.getInstance().setCurrentClientCrop(GobiiCropType.RICE);

        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String digesterInputDirectory = ClientContext
                .getInstance()
                .getCurrentClientCropConfig()
                .getRawUserFilesDirectory();

        String digesterOutputDirectory = ClientContext
                .getInstance()
                .getCurrentClientCropConfig()
                .getIntermediateFilesDirectory();

        extractorInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);


        GobiiExtractorInstruction gobiiExtractorInstructionOne = new GobiiExtractorInstruction();

        // GobiiDataSetExtract one
        String DataSetExtractOneName = "my_foo_Dataset";
        GobiiFileType DataSetExtractOneFileType = GobiiFileType.GENERIC;
        GobiiDataSetExtract gobiiDataSetExtractOne = new GobiiDataSetExtract();
            gobiiDataSetExtractOne.setDataSetName(DataSetExtractOneName);
            gobiiDataSetExtractOne.setAccolate(true);
            gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
            gobiiDataSetExtractOne.setDataSetId(1);

        // GobiiDataSetExtract two
        String DataSetExtractNameTwo = "my_foo_Dataset2";
        GobiiFileType DataSetExtractFileTypeTwo = GobiiFileType.GENERIC;
        GobiiDataSetExtract gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setDataSetName(DataSetExtractNameTwo);
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractFileTypeTwo);
        gobiiDataSetExtractOne.setDataSetId(1);


        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractOne);
        gobiiExtractorInstructionOne.getDataSetExtracts().add(gobiiDataSetExtractTwo);

        extractorInstructionFilesDTOToSend
                .getGobiiExtractorInstructions()
                .add(gobiiExtractorInstructionOne);


        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        GobiiExtractorInstruction gobiiExtractorInstructionTwo = new GobiiExtractorInstruction();

        gobiiExtractorInstructionTwo.setTable("bar_table");

        // column one
        gobiiDataSetExtractOne = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setDataSetName( "my_foo_2Dataset");
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractOneFileType);
        gobiiDataSetExtractOne.setDataSetId(1);

        // column two
        gobiiDataSetExtractTwo = new GobiiDataSetExtract();
        gobiiDataSetExtractOne.setDataSetName( "my_foo_2Dataset2");
        gobiiDataSetExtractOne.setAccolate(true);
        gobiiDataSetExtractOne.setGobiiFileType(DataSetExtractFileTypeTwo);
        gobiiDataSetExtractOne.setDataSetId(1);

        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractTwo);
        gobiiExtractorInstructionTwo.getDataSetExtracts().add(gobiiDataSetExtractTwo);
        
        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(gobiiExtractorInstructionTwo);


        DtoRequestFileExtractorInstructions dtoRequestFileExtractorInstructions = new DtoRequestFileExtractorInstructions();
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOResponse = dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, extractorInstructionFilesDTOResponse);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFilesDTOResponse));


        // ************** NOW RETRIFVE THE FILE WE JUST CREATED AND MAKE SURE IT'S REALLY THERE
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOretrieve = new ExtractorInstructionFilesDTO();
        extractorInstructionFilesDTOretrieve.setProcessType(DtoMetaData.ProcessType.READ);
        extractorInstructionFilesDTOretrieve
                .setInstructionFileName(extractorInstructionFilesDTOResponse.getInstructionFileName());
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOretrieveResponse
                = dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOretrieve);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(extractorInstructionFilesDTOretrieveResponse));

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
                        .getName().equals(instructionOneColumnOneName)
        );

        // ************** VERIFY THAT WE CAN MEANINGFULLY TEST FOR NON EXISTENT DIRECTORIES
        String newInstructionFileName = "testapp_" + DateUtils.makeDateIdString();
        extractorInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileName);

        extractorInstructionFilesDTOToSend
                .getGobiiExtractorInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setRequireDirectoriesToExist(true); // <== should result in validation error

        ExtractorInstructionFilesDTO requiredDirectoriesResponse =
                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);

        Assert.assertTrue(
                2 ==
                        requiredDirectoriesResponse
                                .getDtoHeaderResponse()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getValidationStatusType()
                                                .equals(DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .collect(Collectors.toList())
                                .size()
        );


        // ************** VERIFY THAT THE DIRECTORIES WE SHOULD HAVE CREATED DO EXIST
        String newInstructionFileNameNoError = "testapp_" + DateUtils.makeDateIdString();
        extractorInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        extractorInstructionFilesDTOToSend
                .getGobiiExtractorInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(testSourceDirName)
                .setDestination(testDestinationDirName)
                .setRequireDirectoriesToExist(true); // <== should result in validation error

        ExtractorInstructionFilesDTO requiredDirectoriesResponseNoError =
                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(requiredDirectoriesResponseNoError));


        // ************** VERIFY THAT WE GET AN ERROR WHEN A FILE ALREADY EXISTS
        extractorInstructionFilesDTOToSend.setInstructionFileName(newInstructionFileNameNoError);
        ExtractorInstructionFilesDTO requiredDirectoriesResponseDuplicateNameError =
                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);
        Assert.assertTrue(1 == requiredDirectoriesResponseDuplicateNameError
                .getDtoHeaderResponse()
                .getStatusMessages().size());

        Assert.assertTrue(requiredDirectoriesResponseDuplicateNameError
                .getDtoHeaderResponse()
                .getStatusMessages()
                .get(0)
                .getMessage().toLowerCase().contains("already exists"));


        // ************** VERIFY THAT WE ERROR ON USER INPUT FILE THAT SHOULD EXISTS BUT DOESN'T EXIST

        extractorInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());

        extractorInstructionFilesDTOToSend
                .getGobiiExtractorInstructions()
                .get(0)
                .getGobiiFile()
                .setSource("foo")
                .setDestination("bar")
                .setCreateSource(false); // <== should result in validation error


        ExtractorInstructionFilesDTO testForuserInputFileExistsCausesError =
                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);

        Assert.assertTrue(
                2 ==
                        testForuserInputFileExistsCausesError
                                .getDtoHeaderResponse()
                                .getStatusMessages()
                                .stream()
                                .filter(r ->
                                        r.getValidationStatusType()
                                                .equals(DtoHeaderResponse.ValidationStatusType.ENTITY_DOES_NOT_EXIST))
                                .collect(Collectors.toList())
                                .size()
        );

        // ************** VERIFY THAT WE HANDLE USER INPUT FILE ALREADY EXISTS
        // we're going to test for the existence of the previous instruction file we created;
        // that would not be the real use case; however, it is a file we created on the server
        // so it's handy way to test this functionality
        String instructionFileDirectory = ClientContext
                .getInstance()
                .getCurrentClientCropConfig()
                .getInstructionFilesDirectory();
        String bogusUserInputFile = instructionFileDirectory + newInstructionFileNameNoError + ".json";

        extractorInstructionFilesDTOToSend.setInstructionFileName("testapp_" + DateUtils.makeDateIdString());
        extractorInstructionFilesDTOToSend
                .getGobiiExtractorInstructions()
                .get(0)
                .getGobiiFile()
                .setSource(bogusUserInputFile)
                .setDestination(testDestinationDirName)
                .setCreateSource(false); // <== should result in validation error

        ExtractorInstructionFilesDTO testForuserInputFileExistsNoError =
                dtoRequestFileExtractorInstructions.process(extractorInstructionFilesDTOToSend);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(testForuserInputFileExistsNoError));

    } // testGetMarkers()
}
