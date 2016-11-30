// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DtoRequestLoaderFilePreviewTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testCreateDirectory() throws Exception {
        LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO();

        RestUri previewTestUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .createLoaderDirectory();

        previewTestUri.setParamValue("directoryName", TestDtoFactory.getFolderNameWithTimestamp("Loader File Preview Test"));
        RestResource<LoaderFilePreviewDTO> restResource = new RestResource<>(previewTestUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));



        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No directory name DTO received", resultEnvelope.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO  resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getDirectoryName());

    }

    @Test
    public void testGetFilePreview() throws Exception {
        //Create newFolder
        LoaderFilePreviewDTO loaderFileCreateDTO = new LoaderFilePreviewDTO();
        RestUri previewTestUriCreate = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .createLoaderDirectory();
        previewTestUriCreate.setParamValue("directoryName", TestDtoFactory.getFolderNameWithTimestamp("Loader File Preview Test"));
        RestResource<LoaderFilePreviewDTO> restResourceCreate = new RestResource<>(previewTestUriCreate);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeCreate = restResourceCreate.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFileCreateDTO, GobiiProcessType.CREATE));


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeCreate.getHeader()));
        Assert.assertTrue("No directory name DTO received", resultEnvelopeCreate.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO  resultLoaderFilePreviewDTOCreated = resultEnvelopeCreate.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTOCreated.getDirectoryName());

        //get intended path for the created directory
        TestConfiguration testConfiguration = new TestConfiguration();
        String testCrop = testConfiguration.getConfigSettings().getTestExecConfig().getTestCrop();
        String destinationDirectory = testConfiguration.getConfigSettings().getProcessingPath(testCrop, GobiiFileProcessDir.RAW_USER_FILES);
        String createdFileDirectory = destinationDirectory + resultLoaderFilePreviewDTOCreated.getDirectoryName();

        //copyContentsFromCreatedFolder
        File resourcesDirectory = new File("src/test/resources/datasets");
        File dst = new File(resultLoaderFilePreviewDTOCreated.getDirectoryName());

        FileUtils.copyDirectory(resourcesDirectory, dst);

        //retrieve contents from created name
        RestUri previewTestUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .fileLoaderPreviewQuery();
        previewTestUri.setParamValue("directoryName", dst.getName());
        previewTestUri.setParamValue("fileFormat", "txt");

        RestResource<LoaderFilePreviewDTO> restResource = new RestResource<>(previewTestUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.get(LoaderFilePreviewDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No file preview DTO received", resultEnvelopeCreate.getPayload().getData().size() > 0);
        LoaderFilePreviewDTO resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getDirectoryName());
        Assert.assertTrue(resultLoaderFilePreviewDTO.getDirectoryName().equals(createdFileDirectory));
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getFileList().get(0));

        //compare results read to file
        Assert.assertTrue(checkPreviewFileMatch(resultLoaderFilePreviewDTO.getFilePreview(), resourcesDirectory,resultLoaderFilePreviewDTO.getFileList().get(0)));

    }

    public static File getFileOfType(String filePath, File resourcesDirectory) {

        File newFile = new File(filePath);
        for(File f: resourcesDirectory.listFiles()){
            if(f.getName().equals(newFile.getName())){
                return f;
            }
        }
        return null;
    }

    public static boolean checkPreviewFileMatch(List<List<String>> previewFileItems, File resourcesDirectory, String filePath) throws FileNotFoundException {

        Scanner input = new Scanner(System.in);

            int lineCtr = 0; //count lines read
            input = new Scanner(getFileOfType(filePath, resourcesDirectory));
            while (lineCtr<50) { //read first 50 lines only
                int ctr=0; //count words stored
                String line = input.nextLine();
                for(String s: line.split("\t")){
                    if(ctr==50) break;
                    else{
                        if(!previewFileItems.get(lineCtr).get(ctr).equals(s)) return false;
                        ctr++;
                    }
                }
                lineCtr++;
            }
            input.close();


        return true;
    }
}
