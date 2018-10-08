// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.flex;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.RestRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestAnalysisTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestDataSetTest;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestProtocolTest;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.VertexNameType;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.Vertices;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DtoRequestFlexQueryTest {

    private static String FQ_DUMMY_SCRIPT_NAME = "gobii_gql_placeholder.py";

    @BeforeClass
    public static void setUpClass() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());

//        String fileNamePath = "flex_query/" + FQ_DUMMY_SCRIPT_NAME;
//        URL resource = Thread.currentThread().getContextClassLoader().getResource(fileNamePath);
//
//        Assert.assertNotNull("Unable to get resource for file " + fileNamePath,
//                resource);
//
//        File dummyScript = new File(resource.getFile());
//
//        RestUri restUriUpload = GobiiClientContext.getInstance(null, false)
//                .getUriFactory()
//                .file(GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE, FQ_DUMMY_SCRIPT_NAME);
//
//        HttpMethodResult httpMethodResultUpload = GobiiClientContext.getInstance(null, false)
//                .getHttp()
//                .upload(restUriUpload, dummyScript);
//        Assert.assertTrue("Expected "
//                        + HttpStatus.SC_OK
//                        + " got: "
//                        + httpMethodResultUpload.getResponseCode()
//                        + ": "
//                        + httpMethodResultUpload.getReasonPhrase() + ": " + httpMethodResultUpload.getPlainPayload(),
//                httpMethodResultUpload.getResponseCode() == HttpStatus.SC_OK);

    }

    //
    @AfterClass
    public static void tearDownUpClass() throws Exception {

//        RestUri restUriDelete = GobiiClientContext.getInstance(null, false)
//                .getUriFactory()
//                .file(GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE, FQ_DUMMY_SCRIPT_NAME);
//
//        HttpMethodResult httpMethodResultDelete = GobiiClientContext.getInstance(null, false)
//                .getHttp()
//                .delete(restUriDelete);
//        Assert.assertTrue("Expected "
//                        + HttpStatus.SC_OK
//                        + " got: "
//                        + httpMethodResultDelete.getResponseCode()
//                        + ": "
//                        + httpMethodResultDelete.getReasonPhrase() + ": " + httpMethodResultDelete.getPlainPayload(),
//                httpMethodResultDelete.getResponseCode() == HttpStatus.SC_OK);
//

        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());

    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @Test
    public void testGetVertices() throws Exception {

        URL firstTest = getClass().getClassLoader().getResource("confidentiality.txt");
        URL test = DtoRequestFlexQueryTest.class.getResource("confidentiality.txt");


        RestUri restUriVertices = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_VERTICES);

        GobiiEnvelopeRestResource<VertexDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriVertices);
        PayloadEnvelope<VertexDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(VertexDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("No vertices were retrieved",
                resultEnvelope.getPayload().getData().size() > 0);

        resultEnvelope.getPayload().getData().forEach(
                vertexDTO -> {

//                    Assert.assertFalse("The vertex does not have an entity typename",
//                            vertexDTO.getEntityType().isEmpty());

                    Assert.assertTrue("The vertex does not have a vertexId",
                            vertexDTO.getVertexId() > 0);

                    Assert.assertFalse("The vertex name is empty",
                            vertexDTO.getVertexName().isEmpty());
                }

        );


    } // testGetVertices()

    @Test
    public void testGetVerticesValues() throws Exception {

        String jobId = DateUtils.makeDateIdString() + "_test";
        RestUri restUriVerticesValues = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_VERTICES)
                .addUriParam("jobId", jobId)
                .appendSegment(RestRequestId.URL_VALUES);


        // Add destination vertex
        GobiiEntityNameType gobiiEntityNameTypeToTest = GobiiEntityNameType.PROJECT;
        VertexFilterDTO vertexFilterDTO = new VertexFilterDTO();
        vertexFilterDTO.setDestinationVertexDTO(
                Vertices.getByVertexName(VertexNameType.VERTEX_TYPE_PROJECT)
        );

        //Add filter 1 vertex: 
        VertexDTO analysisVertex = Vertices.getByVertexName(VertexNameType.VERTEX_TYPE_ANALYSIS);
        analysisVertex.setFilterVals(
                (new GlobalPkColl<DtoCrudRequestAnalysisTest>())
                        .getPkVals(DtoCrudRequestAnalysisTest.class, GobiiEntityNameType.ANALYSIS, 3)
                        .stream()
                        .map(pk -> new NameIdDTO(analysisVertex.getEntityType(), pk, analysisVertex.getVertexNameType() + "-" + pk.toString()))
                        .collect(Collectors.toList())
        );
        vertexFilterDTO.getFilterVertices().add(analysisVertex);

        //Add filter 2 vertex:
        VertexDTO datasetVertex = Vertices.getByVertexName(VertexNameType.VERTEX_TYPE_DATASET);
        datasetVertex.setFilterVals(
                (new GlobalPkColl<DtoCrudRequestDataSetTest>()).getPkVals(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASET, 5)
                        .stream()
                        .map(pk -> new NameIdDTO(datasetVertex.getEntityType(), pk, datasetVertex.getVertexNameType() + "-" + pk.toString()))
                        .collect(Collectors.toList())
        );
        vertexFilterDTO.getFilterVertices().add(datasetVertex);

        //Add filter 3 vertex:
        VertexDTO protocolVertex = Vertices.getByVertexName(VertexNameType.VERTEX_TYPE_PROTOCOL);
        protocolVertex.setFilterVals(
                (new GlobalPkColl<DtoCrudRequestProtocolTest>()).getPkVals(DtoCrudRequestProtocolTest.class, GobiiEntityNameType.PROTOCOL, 4)
                        .stream()
                        .map(pk -> new NameIdDTO(protocolVertex.getEntityType(), pk, protocolVertex.getVertexNameType() + "-" + pk.toString()))
                        .collect(Collectors.toList())
        );
        vertexFilterDTO.getFilterVertices().add(protocolVertex);

        GobiiEnvelopeRestResource<VertexFilterDTO> gobiiEnvelopeRestResourceContacts = new GobiiEnvelopeRestResource<>(restUriVerticesValues);
        PayloadEnvelope<VertexFilterDTO> resultEnvelopeVertexFilter = gobiiEnvelopeRestResourceContacts.post(VertexFilterDTO.class,
                new PayloadEnvelope<>(vertexFilterDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeVertexFilter.getHeader()));

        Assert.assertTrue("No vertex filter value was received",
                resultEnvelopeVertexFilter.getPayload().getData().size() > 0);

        VertexFilterDTO vertexFilterDTOReceived = resultEnvelopeVertexFilter.getPayload().getData().get(0);

        Assert.assertTrue("No vertex values were received",
                vertexFilterDTOReceived.getVertexValues().size() > 0);

        Assert.assertTrue("Not all values have the correct entity types",
                vertexFilterDTOReceived.getVertexValues().size() ==
                        vertexFilterDTOReceived
                                .getVertexValues()
                                .stream()
                                .filter(vv -> vv.getGobiiEntityNameType().equals(gobiiEntityNameTypeToTest))
                                .count());

        Assert.assertTrue("Not all values have a non-null name",
                vertexFilterDTOReceived.getVertexValues().size() ==
                        vertexFilterDTOReceived
                                .getVertexValues()
                                .stream()
                                .filter(vv -> !vv.getName().isEmpty())
                                .count());

    } // testGetVerticesValues()


    @Test
    public void testGetMarkerSampleCount() throws Exception {

        String jobId = DateUtils.makeDateIdString() + "_test";
        RestUri restUriVerticesValuesCount = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(RestRequestId.URL_VERTICES)
                .addUriParam("jobId", jobId)
                .appendSegment(RestRequestId.URL_COUNT);


        VertexFilterDTO vertexFilterDTO = new VertexFilterDTO();
        // *********************** destination vertex
        GobiiEntityNameType gobiiDestinationEntityNameTypeToTest = GobiiEntityNameType.PROJECT;
        VertexDTO destinationVertexDTO =
                new VertexDTO(
                        0,
                        VertexNameType.VERTEX_TYPE_PROJECT,
                        GobiiVertexType.ENTITY,
                        null,
                        gobiiDestinationEntityNameTypeToTest,
                        GobiiEntitySubType.UNKNOWN,
                        CvGroup.UNKNOWN,
                        null
                );

        vertexFilterDTO.setDestinationVertexDTO(destinationVertexDTO);


        // *********************** filter vertices -- these are totally bogus values to start with
        GobiiEntityNameType gobiiFilterEntityTypeF1 = GobiiEntityNameType.CONTACT;
        VertexDTO filterF1VertexDTO =
                new VertexDTO(
                        0,
                        VertexNameType.VERTEX_TYPE_PRINCIPLE_INVESTIGATOR,
                        GobiiVertexType.ENTITY,
                        null,
                        gobiiFilterEntityTypeF1,
                        GobiiEntitySubType.UNKNOWN,
                        CvGroup.UNKNOWN,
                        null
                );
        filterF1VertexDTO.setFilterVals(new ArrayList<>(Arrays.asList(
                new NameIdDTO(filterF1VertexDTO.getEntityType(),1,filterF1VertexDTO.getVertexNameType() + "-1"),
                new NameIdDTO(filterF1VertexDTO.getEntityType(),2,filterF1VertexDTO.getVertexNameType() + "-2"),
                new NameIdDTO(filterF1VertexDTO.getEntityType(),3,filterF1VertexDTO.getVertexNameType() + "-3")
        )));

        GobiiEntityNameType gobiiFilterEntityTypeF2 = GobiiEntityNameType.DATASET;
        VertexDTO filterF2VertexDTO =
                new VertexDTO(
                        0,
                        VertexNameType.VERTEX_TYPE_DATASET,
                        GobiiVertexType.ENTITY,
                        null,
                        gobiiFilterEntityTypeF2,
                        GobiiEntitySubType.UNKNOWN,
                        CvGroup.UNKNOWN,
                        null
                );
        filterF2VertexDTO.setFilterVals(new ArrayList<>(Arrays.asList(
                new NameIdDTO(filterF2VertexDTO.getEntityType(),1,filterF2VertexDTO.getVertexNameType() + "-1"),
                new NameIdDTO(filterF2VertexDTO.getEntityType(),2,filterF2VertexDTO.getVertexNameType() + "-2"),
                new NameIdDTO(filterF2VertexDTO.getEntityType(),3,filterF2VertexDTO.getVertexNameType() + "-3")
        )));

        vertexFilterDTO.getFilterVertices().add(filterF1VertexDTO);
        vertexFilterDTO.getFilterVertices().add(filterF2VertexDTO);

        GobiiEnvelopeRestResource<VertexFilterDTO> gobiiEnvelopeRestResourceContacts = new GobiiEnvelopeRestResource<>(restUriVerticesValuesCount);
        PayloadEnvelope<VertexFilterDTO> resultEnvelopeVertexFilter = gobiiEnvelopeRestResourceContacts.post(VertexFilterDTO.class,
                new PayloadEnvelope<>(vertexFilterDTO, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeVertexFilter.getHeader()));

        Assert.assertTrue("No vertex filter value was received",
                resultEnvelopeVertexFilter.getPayload().getData().size() > 0);

        VertexFilterDTO vertexFilterDTOReceived = resultEnvelopeVertexFilter.getPayload().getData().get(0);

        Assert.assertTrue("There is no marker count value",
                vertexFilterDTOReceived.getMarkerCount() > 0);

        Assert.assertTrue("There is no sample count value",
                vertexFilterDTOReceived.getSampleCount() > 0);

    } // testGetVerticesValues()

}
