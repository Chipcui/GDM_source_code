// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.async.SearchExtract;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiResult;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCalls;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseMapGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.login.BrapiRequestLogin;
import org.gobiiproject.gobiibrapi.calls.login.BrapiResponseLogin;
import org.gobiiproject.gobiibrapi.calls.login.BrapiResponseMapLogin;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseMapAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch.BrapiResponseMapAlleleMatrixSearch;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles.BrapiResponseMapMarkerProfiles;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles.BrapiResponseMarkerProfilesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseMapObservationVariables;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseMapStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;

import org.gobiiproject.gobiibrapi.core.common.BrapiPagination;
import org.gobiiproject.gobiibrapi.core.common.BrapiRequestReader;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.types.RestMethodType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.RestResourceLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This controller is only for BRAPI v1. compliant calls. It consumes the gobii-brapi module.
 * <p>
 * BRAPI responses all contain a "metadata" and a "result" key. The "result" key's value can take
 * three forms:
 * 1) A "data" property that is an array of items of the same type (e.g., /studies-search)
 * 2) A set of arbitrary properties (i.e., a specific type in Java) (e.g., /germplasm/{id})
 * 3) A set of arbitrary properties and a "data" property (e.g., /studies/{studyDbId}/observationVariables)
 * <p>
 * Type 3) is for a master-detail scenario: you have a master record that is related to one or more detail
 * records. Type 2) is just the master. Type 1) is just the detail records.
 * <p>
 * The classes that support this API are as follows:
 * <p>
 * BrapiResponseEnvelope: This is the base class for all response envelopes: it is responsible for the metadata
 * key of the response
 * <p>
 * BrapiResponseEnvelopeMasterDetail and BrapiResponseEnvelopeMaster derive from BrapiResponseEnvelope.
 * They are responsible for the result key of the response.
 * <p>
 * BrapiResponseEnvelopeMasterDetail: This class is used for types 1) and 3). It is
 * type-parmaeterized for the pojo that will be the value of the result key; the pojo must extend  BrapiResponseDataList;
 * this way, the pojo will always have a setData() method for specifying the list content of the data key.
 * <p>
 * In the case of (1):
 * The result pojo will be a class with no properties that derives from BrapiResponseDataList. The maping
 * class will use the pojo's setData() method to specify the list that will constitutes the data key
 * of the result key.
 * <p>
 * In the case of (3):
 * Thee result pojo will have its own properties; the pojo's own properties will constitute the values
 * of the result key; the pojos setData() method will be used to specify the list that constitutes the
 * data key;
 * <p>
 * BrapiResponseEnvelopeMaster: This class is used for type 2). It is type-parameterized
 * for an arbitrary pojo. Because type 2 responses do not have a data key, the pojo does
 * _not_ extend BrapiResponseDataList. Its properties will be the values of the response's result key.
 * <p>
 * The calls namespace of gobii-brapi is organized as the brapi API is organized. In the descriptions
 * below, <CallName> refers to the BRAPI call name.
 * Each call contains several sorts of classes:
 * ---- POJOs named BrapiResponse<CallName>: these are the arbitrary pojos that
 * type-parameterize BrapiResponseElvelopeMasterDetail and BrapiResponseElvelopeMaster
 * ---- POJOs named BrapiRequest<CallName>: these are POST/PUT bodies for which the
 * relevant methods in here the controller have @RequestBody parameters (e.g., BrapiRequestStudiesSearch)
 * ---- POJOs named BrapiResponseMap<CallName>: Right now these clases create dummy responses; the real
 * implementations of these classes will consume classes from the gobii-domain project (i.e., the Service
 * classes): they will get data from gobii in terms of gobii DTOs and convert the DTOs in to the
 * BRAPI POJOs
 * <p>
 * The BrapiController does the following:
 * 0.   If there is a post body, deserializes it to the appropriate post pojo;
 * i.   Instantiates a BrapiResponseEnvelopeMaster or BrapiResponseEnvelopeMasterDetail;
 * ii.  Uses the map classes for each call to get the pojo that the call will use for its payload;
 * iii. Assigns the pojo to the respective respone envelope;
 * iv.  Serialies the content of the response envelope;
 * v.   Sets the reponse of the method to the serialized content.
 * <p>
 * Note that this controller receives and sends plain String json data. This approach is different
 * from the gobii api. The BrapiResponseEnvelopeList and BrapiResponseEnvelopeMaster are serialzied to
 * json and sent over the wire in this way rather than letting the Jackson embedded through Spring do
 * the job automatically. This approach is more traditionally the web service way of doing things.
 */
@Scope(value = "request")
@Controller
@Api()
@EnableAsync
@RequestMapping(GobiiControllerType.SERVICE_PATH_BRAPI)
public class BRAPIIControllerV1 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV1.class);

    @Autowired
    private PingService pingService = null;


    @Autowired
    private BrapiResponseMapStudiesSearch brapiResponseMapStudiesSearch = null;


    @Autowired
    private BrapiResponseMapAlleleMatrixSearch brapiResponseMapAlleleMatrixSearch = null;

    @Autowired
    private BrapiResponseMapMarkerProfiles brapiResponseMapMarkerProfiles = null;


    @Autowired
    private BrapiResponseMapAlleleMatrices brapiResponseMapAlleleMatrices = null;

    @Autowired
    private DnaRunService dnaRunService = null;

    @Autowired
    private MarkerBrapiService markerBrapiService = null;

    @Autowired
    private GenotypeCallsService genotypeCallsService = null;

    @Autowired
    private DatasetBrapiService dataSetBrapiService = null;

    @Autowired
    private SearchExtract searchExtract = null;

    @Autowired
    private ConfigSettingsService configSettingsService;

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    private class CallSetResponse extends BrApiMasterPayload<DnaRunDTO>{}
    private class CallSetListResponse extends BrApiMasterPayload<BrApiResult<DnaRunDTO>>{}
    private class GenotypeCallsResponse extends BrApiMasterPayload<GenotypeCallsDTO>{}
    private class GenotypeCallsListResponse extends BrApiMasterPayload<BrApiResult<GenotypeCallsDTO>>{}
    private class VariantResponse extends BrApiMasterPayload<MarkerBrapiDTO>{}
    private class VariantListResponse extends BrApiMasterPayload<BrApiResult<MarkerBrapiDTO>>{}
    private class VariantSetResponse extends  BrApiMasterPayload<DataSetBrapiDTO>{}
    private class VariantSetListResponse extends BrApiMasterPayload<BrApiResult<DataSetBrapiDTO>>{}



    // *********************************************
    // *************************** CALLS
    // *********************************************
    @RequestMapping(value = "/calls",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "List all calls.",
            notes = "List all calls",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Calls"),
                            @ExtensionProperty(
                                    name="tag-description",
                                    value= "BrAPI is standards api calls. Below section list all" +
                                            " the BrAPI calls supported by GDM."
                            )
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getCalls(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseCalls> brapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();
        try {

            BrapiResponseMapCalls brapiResponseMapCalls = new BrapiResponseMapCalls(request);

            BrapiResponseCalls brapiResponseCalls = brapiResponseMapCalls.getBrapiResponseCalls();
            brapiResponseEnvelopeMasterDetail.setResult(brapiResponseCalls);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelopeMasterDetail);

        return returnVal;
    }


    // *********************************************
    // *************************** LOGIN (MASTER ONLY)
    // *********************************************
    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            produces = "application/json")
    @ApiOperation(
            value = "List all tokens.",
            hidden = true,
            notes = "List all tokens",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Tokens"),
                    })
            }
    )
    @ResponseBody
    public String postLogin(@RequestBody String loginRequestBody,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        String returnVal;


        BrapiResponseLogin brapiResponseLogin = null;
        try {

            BrapiRequestReader<BrapiRequestLogin> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestLogin.class);
            BrapiRequestLogin brapiRequestLogin = brapiRequestReader.makeRequestObj(loginRequestBody);

            BrapiResponseMapLogin brapiResponseMapLogin = new BrapiResponseMapLogin();
            brapiResponseLogin = brapiResponseMapLogin.getLoginInfo(brapiRequestLogin, response);


            brapiResponseLogin.getBrapiMetaData().setPagination(new BrapiPagination(
                    1,
                    1,
                    1,
                    0
            ));

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseLogin.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseLogin.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseLogin);

        return returnVal;
    }


    // *********************************************
    // *************************** STUDIES_SEARCH (DETAILS ONLY)
    // *************************** LIST ITEMS ONLY
    // *********************************************
    @RequestMapping(value = "/studies-search",
            method = RequestMethod.POST,
            produces = "application/json")
    @ApiOperation(
            value = "Search the studies.",
            notes = "Search studies.",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="StudiesSearch"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getStudies(@RequestBody String studiesRequestBody,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseStudiesSearch> BrapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();

        try {

            BrapiRequestReader<BrapiRequestStudiesSearch> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestStudiesSearch.class);
            BrapiRequestStudiesSearch brapiRequestStudiesSearch = brapiRequestReader.makeRequestObj(studiesRequestBody);

            Integer requestedPageSize = brapiRequestStudiesSearch.getPageSize();

            BrapiResponseStudiesSearch brapiResponseStudySearch = brapiResponseMapStudiesSearch.getBrapiResponseStudySearch(brapiRequestStudiesSearch);

            BrapiResponseEnvelopeMasterDetail.setResult(brapiResponseStudySearch);

            Integer numberOfHits = BrapiResponseEnvelopeMasterDetail.getResult().getData().size();

            Integer reportedPageSize;
            Integer totalPages;
            if (requestedPageSize > numberOfHits) {
                reportedPageSize = numberOfHits;
                totalPages = 1;
            } else {
                reportedPageSize = requestedPageSize;
                totalPages = numberOfHits / reportedPageSize; // get the whole part of the result
                if (numberOfHits % reportedPageSize > 0) {   // if there's a remainder, there's an additional page
                    totalPages += 1;
                }
            }


            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().setPagination(new BrapiPagination(
                    numberOfHits,
                    reportedPageSize,
                    totalPages,
                    0
            ));

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(BrapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    // *********************************************
    // *************************** Germplasm details [GET]
    // **************************** MASTER ONLY
    // *********************************************
    @RequestMapping(value = "/germplasm/{studyDbId}",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ApiOperation(
            value = "Get germplasm by study db id.",
            notes = "Get germplasm by study db id.",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Germplasm : studyDbId"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getGermplasmByDbId(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @ApiParam(value = "Study DB Id", required = true)
                                     @PathVariable Integer studyDbId
//            ,
//                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
//                               @RequestParam(value = "page", required = false) Integer page
    ) throws Exception {


        BrapiResponseEnvelopeMaster<BrapiResponseGermplasmByDbId> responseEnvelope
                = new BrapiResponseEnvelopeMaster<>();

        String returnVal;

        try {

            BrapiResponseMapGermplasmByDbId brapiResponseMapGermplasmByDbId = new BrapiResponseMapGermplasmByDbId();

            // extends BrapiMetaData, no list items
            BrapiResponseGermplasmByDbId brapiResponseGermplasmByDbId = brapiResponseMapGermplasmByDbId.getGermplasmByDbid(studyDbId);

            responseEnvelope.setResult(brapiResponseGermplasmByDbId);


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(responseEnvelope);

        return returnVal;

    }

    // *********************************************
    // *************************** Study obsefvation variables (GET)
    // **************************** MASTER AND DETAIL
    // *********************************************
    @RequestMapping(value = "/studies/{studyDbId}/observationVariables",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ApiOperation(
            value = "List all observation variables for given study db id",
            notes = "List all observation variables for given study db id",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Studies.observationVariables"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getObservationVariables(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam(value = "Study DB Id", required = true)
                                          @PathVariable Integer studyDbId) throws Exception {

        BrapiResponseEnvelopeMasterDetail<BrapiResponseObservationVariablesMaster> responseEnvelope
                = new BrapiResponseEnvelopeMasterDetail<>();

        String returnVal;

        try {

            BrapiResponseMapObservationVariables brapiResponseMapObservationVariables = new BrapiResponseMapObservationVariables();

            BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseMapObservationVariables.gerObservationVariablesByStudyId(studyDbId);

            responseEnvelope.setResult(brapiResponseObservationVariablesMaster);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(responseEnvelope);

        return returnVal;
    }


    // *********************************************
    // *************************** ALLELE MATRICES
    // *********************************************
    @RequestMapping(value = "/allelematrices",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "Get Allele Matrices",
            notes = "Get allele matrices by given study db id",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrices"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getAlleleMatrices(@ApiParam(value = "Study DB Id", required = false)
                                    @RequestParam("studyDbId") Optional<String> studyDbIdd,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseAlleleMatrices> BrapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();

        try {

            BrapiResponseAlleleMatrices brapiResponseAlleleMatrices;
            if (studyDbIdd.isPresent()) {
                Integer studyDbIdAsInteger = Integer.parseInt(studyDbIdd.get());
                brapiResponseAlleleMatrices = brapiResponseMapAlleleMatrices.getBrapiResponseAlleleMatricesItemsByStudyDbId(studyDbIdAsInteger);
            } else {
                brapiResponseAlleleMatrices = brapiResponseMapAlleleMatrices.getBrapiResponseAlleleMatrices();
            }

            BrapiResponseEnvelopeMasterDetail.setResult(brapiResponseAlleleMatrices);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(BrapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    @RequestMapping(value = "/allelematrix-search",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ApiOperation(
            value = "Search Allele Matrix",
            notes = "Search allele matrix using marker profiles",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrixSearch"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getAlleleMatrix(@ApiParam(value = "Matrix DB Id", required = false)
                                  @RequestParam("matrixDbId") Optional<String> matrixDbId,
                                  @ApiParam(value = "Marker Profile Id", required = false)
                                  @RequestParam("markerprofileDbId") Optional<String> markerprofileDbId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        return this.alleleMatrix(matrixDbId, markerprofileDbId, request, response);

    }

    @RequestMapping(value = "/allelematrix-search",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ApiOperation(
            value = "Search Allele Matrix",
            notes = "Search allele matrix using marker profiles",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrixSearch"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String postAlleleMatrix(@ApiParam(value = "Matrix DB Id", required = false)
                                  @RequestParam("matrixDbId") Optional<String> matrixDbId,
                                  @ApiParam(value = "Marker Profile Id", required = false)
                                  @RequestParam("markerprofileDbId") Optional<String> markerprofileDbId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        return this.alleleMatrix(matrixDbId, markerprofileDbId, request, response);

    }

    public String alleleMatrix(Optional<String> matrixDbId,
                               Optional<String> markerprofileDbId,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        String returnVal = null;

        BrapiResponseEnvelope brapiResponseEnvelope = new BrapiResponseEnvelope();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            if (matrixDbId.isPresent() == markerprofileDbId.isPresent()) {
                String message = "Incorrect request format. At least one of matrixDbId or markerprofileDbId should be specified.";
                brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
            } else if (matrixDbId.isPresent()) {
                List<String> matrixDbIdList = Arrays.asList(matrixDbId.get().split(","));
                if (matrixDbIdList.size() > 1) {
                    String message = "Incorrect request format. Only one matrixDbId is supported at the moment.";
                    brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
                    return objectMapper.writeValueAsString(brapiResponseEnvelope);
                }
                brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.searchByMatrixDbId(cropType, matrixDbIdList.get(0)));
            } else {
                List<String> externalCodes = Arrays.asList(markerprofileDbId.get().split(","));
                brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.searchByExternalCode(cropType, externalCodes));
            }
        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }


    @RequestMapping(value = "/allelematrix-search/status/{jobId}",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "Get Allele Matrix Job status",
            notes = "Get allele matrix Job status",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrix.status : jobId"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getAlleleMatrixStatus(@ApiParam(value = "Job Id", required = true)
                                        @PathVariable("jobId") String jobId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        String returnVal = null;

        BrapiResponseEnvelope brapiResponseEnvelope = new BrapiResponseEnvelope();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.getStatus(cropType,
                    jobId,
                    request));


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }


    /***
     * Returns a stream for the at at the path specified by the query parameter. This method is not
     * defined by the BRAPI spec, nor should it be: the spec only stipulates that entries in the files
     * section of the metadata object be accessible to the client.
     *
     * @param fqpn: fully qualified path of file to download
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/files",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(
            value = "List all files",
            notes = "List all the files in a given path.",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Files"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public void getFile(@ApiParam(value = "Fully qualified path name", required = true)
                        @RequestParam("fqpn") String fqpn,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        try {

            response.setContentType("application/text");
            InputStream inputStream = new FileInputStream(fqpn);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file " + fqpn + "to output stream: " + ex.getMessage());
        }
    }


    @RequestMapping(value = "/markerprofiles",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ApiOperation(
            value = "List all Marker Profiles",
            notes = "List all Marker Profiles in given germplasm db..",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="MarkerProfiles"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String getMarkerProfile(@ApiParam(value = "Germplasm DB Id", required = true)
                                   @RequestParam("germplasmDbId") String germplasmDbId,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        return this.markerProfile(germplasmDbId, request, response);
    }

    @RequestMapping(value = "/markerprofiles",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ApiOperation(
            value = "List all Marker Profiles",
            notes = "List all Marker Profiles in given germplasm db..",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="MarkerProfiles"),
                    })
            },
            hidden = true
    )
    @ResponseBody
    public String postMarkerProfile(@ApiParam(value = "Germplasm DB Id", required = true)
                                    @RequestParam("germplasmDbId") String germplasmDbId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        return this.markerProfile(germplasmDbId, request, response);
    }

    public String markerProfile(String germplasmDbId,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        BrapiResponseEnvelopeMasterDetail<BrapiResponseMarkerProfilesMaster> brapiResponseEnvelope
                = new BrapiResponseEnvelopeMasterDetail<>();

        String returnVal;
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            if (!LineUtils.isNullOrEmpty(germplasmDbId)) {

                brapiResponseEnvelope.setResult(brapiResponseMapMarkerProfiles.getBrapiResponseMarkerProfilesByGermplasmId(germplasmDbId));

            } else {
                String message = "Incorrect request format: germplasmDbId must be specified";
                brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
            }
        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }

    /**
     * Lists the dnaruns by page size and page token
     *
     * @param pageTokenParam String page token
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @return Brapi response with list of dna runs/call sets
     */
    @ApiOperation(
            value = "List all callsets",
            notes = "List of all Callsets.",
            tags = {"Callsets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets",
                            response = CallSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallSets(
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "ID of the CallSet to be retrieved.")
            @RequestParam(value = "callSetDbId", required = false) Integer callSetDbId,
            @ApiParam(value = "The human readable name of the CallSet to be retrieved.")
            @RequestParam(value = "callSetName", required = false) String callSetName,
            @ApiParam(value = "The ID of the VariantSet to be retrieved.")
            @RequestParam(value = "variantSetDbId", required = false) String variantSetDbId,
            @ApiParam(value = "The ID of the Sample to be retrieved.")
            @RequestParam(value = "sampleDbId", required = false) String sampleDbId,
            @ApiParam(value = "The ID of the Germplasm to be retrieved.")
            @RequestParam(value = "germplasmDbId", required = false) String germplasmDbId,
            @ApiParam(value = "The ID of the study to be retrieved.")
            @RequestParam(value = "studyDbId", required = false) String studyDbId,
            @ApiParam(value = "The name of the sample to be retrieved.")
            @RequestParam(value = "sampleName", required = false) String sampleName
    ) {
        try {

            Integer pageToken = null;

            if (pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                }
                catch(Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token");
                }
            }

            DnaRunDTO dnaRunDTOFilter = new DnaRunDTO();

            if (callSetDbId != null) {
                dnaRunDTOFilter.setCallSetDbId(callSetDbId);
            }

            if (callSetName != null) {
                dnaRunDTOFilter.setCallSetName(callSetName);
            }

            if (variantSetDbId != null) {
                List<Integer> variantDbArr = new ArrayList<>();
                variantDbArr.add(Integer.parseInt(variantSetDbId));
                dnaRunDTOFilter.setVariantSetIds(variantDbArr);
            }

            if (sampleDbId != null) {
                dnaRunDTOFilter.setSampleDbId(Integer.parseInt(sampleDbId));
            }

            if (germplasmDbId != null) {
                dnaRunDTOFilter.setGermplasmDbId(Integer.parseInt(germplasmDbId));
            }

            if (studyDbId != null) {
                dnaRunDTOFilter.setStudyDbId(Integer.parseInt(studyDbId));
            }

            if (sampleName != null) {
                dnaRunDTOFilter.setSampleName(sampleName);
            }

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_DNARUN,
                    RestMethodType.GET
            );

            if(maxPageSize == null) {
                //As per brapi initial standards
                maxPageSize = 1000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<DnaRunDTO> dnaRunList = dnaRunService.getDnaRuns(pageToken, pageSize, dnaRunDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(dnaRunList);
            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (dnaRunList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(dnaRunList.size());
                if(dnaRunList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            dnaRunList.get(dnaRunList.size() - 1).getCallSetDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage());
        }
    }

    /**
     * Endpoint for getting a specific callset with a given callSetDbId
     *
     * @param callSetDbId ID of the requested callset
     * @return ResponseEntity with http status code specifying if retrieval of the callset is successful.
     * Response body contains the requested callset information
     */
    @ApiOperation(
            value = "Get a callset by callsetId",
            notes = "Retrieves the Callset entity having the specified ID",
            tags = {"Callsets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets : callSetDbId")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets", response = CallSetResponse.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/callsets/{callSetDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallSetsByCallSetDbId(
            @ApiParam(value = "ID of the Callset to be extracted", required = true)
            @PathVariable("callSetDbId") Integer callSetDbId) {

        Integer callSetDbIdInt;

        try {
            callSetDbIdInt = callSetDbId;
            DnaRunDTO dnaRunDTO = dnaRunService.getDnaRunById(callSetDbIdInt);
            BrApiMasterPayload<DnaRunDTO> payload = new BrApiMasterPayload<>(dnaRunDTO);

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);
        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist");
        }

    }


    /**
     * Returns the list of genotypes calls in a given DNA run id.
     * It fetches calls in all the datasets where the dnarun_id is present.
     * The calls is paged.
     *
     * @param callSetDbId - DNA run Id.
     * @param pageSize - Size of the page to fetched.
     * @param pageToken - Page token to fetch the page. User will get the pageToken
     *                       from the nextPageToken parameter in the previous response.
     *
     * @return BrApi Response entity with list of genotypes calls for given dnarun id.
     * TODO: Add page number parameter to comply BrApi standards.
     */
    @ApiOperation(
            value = "List genotype calls",
            notes = "List of all the genotype calls in a given Dna run identified by Dna run Id",
            tags = {"Callsets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Genotype calls",
                            response = GenotypeCallsResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets/{callSetDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallsByCallset(
            @ApiParam(value = "Id for dna run to be fetched")
            @PathVariable(value="callSetDbId") String callSetDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) throws Exception {

        Integer callSetDbIdInt;

        try {

            try {
                callSetDbIdInt = Integer.parseInt(callSetDbId);
            } catch (Exception e) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Invalid dna run Id");
            }

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_DNARUN,
                    RestMethodType.GET);

            if(maxPageSize == null) {
                //As per brapi initial standards
                maxPageSize = 10000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<GenotypeCallsDTO> genotypeCallsList = genotypeCallsService.getGenotypeCallsByDnarunId(
                    callSetDbIdInt, pageToken,
                    pageSize
            );

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload = new BrApiMasterPayload<>(genotypeCallsList);

            if (genotypeCallsList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(genotypeCallsList.size());
                if (genotypeCallsList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            genotypeCallsList.get(genotypeCallsList.size() - 1).getVariantDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage());
        }
    }


    @RequestMapping(value="/search/callsets", method=RequestMethod.POST)
    public ResponseEntity searchCallSets(
            HttpEntity<String> searchQuery,
            HttpServletRequest request) throws Exception {

        String uuid = null;

        if (searchQuery.hasBody()) {

            uuid = dnaRunService.createSearchQuery(searchQuery.getBody());

        }

        HashMap<String, String> result = new HashMap<>();
        result.put("searchResultsDbId", uuid);
        BrApiMasterPayload brApiMasterPayload = new BrApiMasterPayload(result);

        return ResponseEntity.ok(brApiMasterPayload);

    }

    @RequestMapping(value="/search/callsets/{searchResultsDbId}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallSetsBySearch(
            @PathVariable("searchResultsDbId") String searchResultsDbId,
            @RequestParam(value="pageToken", required = false) String pageTokenParam,
            @RequestParam(value="pageSize", required = false) Integer pageSize
    ){

        try {

            Integer pageToken = null;

            if (pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                }
                catch (Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token"
                    );
                }
            }

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_DNARUN,
                    RestMethodType.GET
            );

            if (maxPageSize == null) {
                maxPageSize = 1000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<DnaRunDTO> dnaRunDTOList = dnaRunService.getDnaRunsFromSearch(searchResultsDbId, pageToken, pageSize);

            BrApiResult result = new BrApiResult();
            result.setData(dnaRunDTOList);
            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (dnaRunDTOList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(dnaRunDTOList.size());
                if(dnaRunDTOList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            dnaRunDTOList.get(dnaRunDTOList.size() - 1).getCallSetDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);


        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }

    }

    /**
     * Lists the variants by page size and page token
     *
     * @param pageTokenParam String page token
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page sie
     * @return Brapi response with list of variants
     */
    @ApiOperation(
            value = "List all variants",
            notes = "List of all Variants",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variants",
                            response = VariantListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
            paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariants(
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "ID of the variant to be extracted")
            @RequestParam(value = "variantDbId", required = false) Integer variantDbId,
            @ApiParam(value = "ID of the variantSet to be extracted")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
            @ApiParam(value = "ID of the mapset to be retrieved")
            @RequestParam(value = "mapSetId", required = false) Integer mapSetId,
            @ApiParam(value = "Name of the mapset to be retrieved")
            @RequestParam(value = "mapSetName", required = false) String mapSetName
    ) {
        try {

            Integer pageToken = null;

            if (pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                } catch (Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token"
                    );
                }
            }

            MarkerBrapiDTO markerBrapiDTOFilter = new MarkerBrapiDTO();

            if (variantDbId != null) {
                markerBrapiDTOFilter.setVariantDbId(variantDbId);
            }

            if (variantSetDbId != null) {
                List<Integer> variantDbArr = new ArrayList<>();
                variantDbArr.add(variantSetDbId);
                markerBrapiDTOFilter.setVariantSetDbId(variantDbArr);
            }

            if (mapSetId != null) {
                markerBrapiDTOFilter.setMapSetId(mapSetId);
            }

            if (mapSetName != null) {
                markerBrapiDTOFilter.setMapSetName(mapSetName);
            }

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_MARKERS,
                    RestMethodType.GET
            );

            if (maxPageSize == null) {
                maxPageSize = 1000;
            }

            if (pageSize == null || pageSize >  maxPageSize) {
                pageSize = maxPageSize;
            }

            List<MarkerBrapiDTO> markerList = markerBrapiService.getMarkers(pageToken, pageSize, markerBrapiDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(markerList);

            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (markerList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(markerList.size());
                if (markerList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            markerList.get(markerList.size() -1).getVariantDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }

    }

    /**
     * Endpoint for getting a specific marker with a given markerDbId
     *
     * @param variantDbId ID of the requested marker
     * @return ResponseEntity with http status code specifying if retrieval of the marker is successful.
     * Response body contains the requested marker information
     */
    @ApiOperation(
            value = "Get a variant by variantDbId",
            notes = "Retrieves the Variant entity having the specified ID",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants: variantDbId")
                    })
            }

    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variant by Id",
                            response = VariantResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/variants/{variantDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariantsByVariantDbId(
            @ApiParam(value = "ID of the Variant to be extracted", required = true)
            @PathVariable("variantDbId") Integer variantDbId) {

        Integer variantDbIdInt;

        try {
            variantDbIdInt = variantDbId;

            MarkerBrapiDTO markerBrapiDTO = markerBrapiService.getMarkerById(variantDbIdInt);
            BrApiMasterPayload<MarkerBrapiDTO> payload = new BrApiMasterPayload<>(markerBrapiDTO);

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);

        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }
    }

    /**
     * Returns the list of genotypes calls in a given Marker id.
     * It fetches calls in all the datasets where the marker_id is present.
     * The calls is paged.
     *
     * @param variantDbId - Marker run Id.
     * @param pageSize - Size of the page to fetched.
     * @param pageToken - Page token to fetch the page. User will get the pageToken
     *                       from the nextPageToken parameter in the previous response.
     *
     * @return BrApi Response entity with list of genotypes calls for given dnarun id.
     * TODO: Add page number parameter to comply BrApi standards.
     */
    @ApiOperation(
            value = "List genotype calls",
            notes = "List of all the genotype calls in a given Marker identified by Marker Id",
            tags = {"Variants"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Genotype Calls",
                            response = GenotypeCallsResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants/{variantDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallsByVariant(
            @ApiParam(value = "Id for marker to be fetched")
            @PathVariable(value="variantDbId") String variantDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) throws Exception {

        Integer variantDbIdInt;

        try {

            try {
                variantDbIdInt = Integer.parseInt(variantDbId);
            } catch (Exception e) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Invalid marker Id");
            }


            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_DNARUN,
                    RestMethodType.GET);

            if(maxPageSize == null) {
                //As per brapi initial standards
                maxPageSize = 10000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<GenotypeCallsDTO> genotypeCallsList = genotypeCallsService.getGenotypeCallsByMarkerId(
                    variantDbIdInt, pageToken, pageSize);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload = new BrApiMasterPayload<>(genotypeCallsList);

            if (genotypeCallsList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(genotypeCallsList.size());
                if (genotypeCallsList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            genotypeCallsList.get(genotypeCallsList.size() - 1).getVariantSetDbId().toString() +
                                    "-" +
                            genotypeCallsList.get(genotypeCallsList.size() - 1).getCallSetDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage());
        }
    }

    /**
     * Lists the variantsets by page size and page token
     *
     * @param pageTokenParam - String page token
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @return Brapi response with list of variantsets
     */
    @ApiOperation(
            value = "List all variantsets",
            notes = "List of all Variantsets",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="VariantSets")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of VariantSets",
                            response = VariantSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariantSets(
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
            @RequestParam(value = "variantSetName", required = false) String variantSetName,
            @RequestParam(value = "studyDbId", required = false) String studyDbId
    ) {
        try {

            Integer pageToken = null;

            if (pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                }
                catch (Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token"
                    );
                }
            }

            DataSetBrapiDTO dataSetBrapiDTOFilter = new DataSetBrapiDTO();

            if (variantSetDbId != null) {
                dataSetBrapiDTOFilter.setVariantSetDbId(variantSetDbId);
            }

            if (variantSetName != null) {
                dataSetBrapiDTOFilter.setVariantSetName(variantSetName);
            }

            if (studyDbId != null) {
                dataSetBrapiDTOFilter.setStudyDbId(Integer.parseInt(studyDbId));
            }

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_DATASETS,
                    RestMethodType.GET
            );

            if (maxPageSize == null) {
                maxPageSize = 1000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<DataSetBrapiDTO> dataSetBrapiDTOList = dataSetBrapiService.getDatasets(pageToken, pageSize,
                    dataSetBrapiDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(dataSetBrapiDTOList);

            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (dataSetBrapiDTOList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(dataSetBrapiDTOList.size());
                if (dataSetBrapiDTOList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            dataSetBrapiDTOList.get(dataSetBrapiDTOList.size() - 1).getVariantSetDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }

    /**
     * Endpoint for getting a specific variantset with a given variantSetDbId
     *
     * @param variantSetDbId ID of the requested variantset
     * @return ResponseEntity with http status code specifying if retrieval of the variantset is successful.
     * Response body contains the requested variantset information
     */
    @ApiOperation(
            value = "Get a variantset by variantSetDbId",
            notes = "Retrieves the VariantSet entity having the specified ID",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="VariantSets : variantSetDbId")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of VariantSet By ID",
                            response = VariantSetResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariantSetsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId) {

        try {

            DataSetBrapiDTO dataSetBrapiDTO = dataSetBrapiService.getDatasetById(variantSetDbId);
            BrApiMasterPayload<DataSetBrapiDTO> payload = new BrApiMasterPayload<>(dataSetBrapiDTO);

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);

        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }
    }

    /**
     * Lists the variants for a given VariantSetDbId by page size and page token
     *
     * @param variantSetDbId - Integer ID of the VariantSet to be fetched
     * @param pageTokenParam - String page token
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed page size, then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
            value = "List all Variants for a given VariantSetDbId",
            notes = "List of all the Variants in a specific VariantSet",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variants by VariantSetId",
                            response = VariantListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}/variants", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariantsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet of the Variants to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "ID of the mapset to be retrieved")
            @RequestParam(value = "mapSetId", required = false) Integer mapSetId,
            @ApiParam(value = "Name of the mapset to be retrieved")
            @RequestParam(value = "mapSetName", required = false) String mapSetName
    ){

        try {

            Integer pageToken = null;

            if (pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                } catch (Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token"
                    );
                }
            }

            MarkerBrapiDTO markerBrapiDTOFilter = new MarkerBrapiDTO();

            List<Integer> variantSetDbIdArr = new ArrayList<>();
            variantSetDbIdArr.add(variantSetDbId);
            markerBrapiDTOFilter.setVariantSetDbId(variantSetDbIdArr);

            if (mapSetId != null) {
                markerBrapiDTOFilter.setMapSetId(mapSetId);
            }

            if (mapSetName != null) {
                markerBrapiDTOFilter.setMapSetName(mapSetName);
            }

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_MARKERS,
                    RestMethodType.GET
            );

            if (maxPageSize == null){
                maxPageSize = 1000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<MarkerBrapiDTO> markerList = markerBrapiService.getMarkers(pageToken, pageSize, markerBrapiDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(markerList);
            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (markerList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(markerList.size());
                if (markerList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            markerList.get(markerList.size() -1).getVariantDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }

    /**
     * Lists the callsets for a given VariantSetDbId by page size and page token
     *
     * @param variantSetDbId - Integer ID of the VariantSet to be fetched
     * @param pageTokenParam - String page token
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed page size, then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
            value = "List all Callsets for a given VariantSetDbId",
            notes = "List of all the CallSets in a specific VariantSet",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets by VariantSetId",
                            response = CallSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}/callsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallSetsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet of the CallSets to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ){

        try {

            Integer pageToken = null;

            if (pageTokenParam != null) {
                try {
                    pageToken = Integer.parseInt(pageTokenParam);
                } catch (Exception e) {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Token"
                    );
                }
            }

            DnaRunDTO dnaRunDTOFilter = new DnaRunDTO();

            List<Integer> variantSetDbIdArr = new ArrayList<>();
            variantSetDbIdArr.add(variantSetDbId);
            dnaRunDTOFilter.setVariantSetIds(variantSetDbIdArr);

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_MARKERS,
                    RestMethodType.GET
            );

            if (maxPageSize == null){
                maxPageSize = 1000;
            }

            if (pageSize == null || pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            List<DnaRunDTO> dnaRunList = dnaRunService.getDnaRuns(pageToken, pageSize, dnaRunDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(dnaRunList);
            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (dnaRunList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(dnaRunList.size());
                if (dnaRunList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            dnaRunList.get(dnaRunList.size() -1).getCallSetDbId().toString()
                    );
                }
            }

            return ResponseEntity.ok().contentType(
                    MediaType.APPLICATION_JSON).body(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }


    @ApiOperation(
            value = "Creates a extract",
            notes = "Creates a variant set resource for given extract query",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="VariantSets")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/extract", method=RequestMethod.POST)
    public ResponseEntity<String> VariantSetsExtract(
            HttpEntity<String> extractQuery,
            HttpServletRequest request) throws Exception {

        String cropType = CropRequestAnalyzer.getGobiiCropType(request);

        String processingId = UUID.randomUUID().toString();

        if(extractQuery.hasBody()) {

            String extractQueryPath = LineUtils.terminateDirectoryPath(
                    configSettingsService.getConfigSettings().getServerConfigs().get(
                            cropType).getFileLocations().get(GobiiFileProcessDir.RAW_USER_FILES)
            ) + processingId + LineUtils.PATH_TERMINATOR + "extractQuery.json";

            File extractQueryFile = new File(extractQueryPath);

            extractQueryFile.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(extractQueryFile));

            bw.write(extractQuery.getBody());

            bw.close();
        }

        searchExtract.asyncMethod();

        return ResponseEntity.ok(processingId);
    }

    @ApiOperation(
            value = "List genotype calls",
            notes = "List of all the genotype calls in a given Variantset",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Genotype Calls by VariantSetId",
                            response = GenotypeCallsListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(
            value="/variantsets/{variantSetDbId}/calls",
            method=RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody ResponseEntity getCallsByVariantSetDbId(
            @ApiParam(value = "ID of the VariantSet of the CallSets to be extracted", required = true)
            @PathVariable("variantSetDbId") String variantSetDbIdVar,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request
    ){

        try {
            List<GenotypeCallsDTO> genotypeCallsList = new ArrayList<>();

            Integer variantSetDbId;

            try {

                variantSetDbId = Integer.parseInt(variantSetDbIdVar);

                genotypeCallsList =
                        genotypeCallsService.getGenotypeCallsByDatasetId(
                                variantSetDbId, pageToken, pageSize);
            }
            catch(NumberFormatException | NullPointerException ne) {

                String cropType = CropRequestAnalyzer.getGobiiCropType(request);

                String extractQueryPath = LineUtils.terminateDirectoryPath(
                        configSettingsService.getConfigSettings().getServerConfigs().get(
                                cropType).getFileLocations().get(GobiiFileProcessDir.RAW_USER_FILES)
                ) + variantSetDbIdVar + LineUtils.PATH_TERMINATOR + "extractQuery.json";

                genotypeCallsList =
                        genotypeCallsService.getGenotypeCallsByExtractQuery(
                                extractQueryPath, pageToken, pageSize);
            }


            BrApiResult result = new BrApiResult();
            result.setData(genotypeCallsList);
            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (genotypeCallsList.size() > 0) {
                payload.getMetaData().getPagination().setPageSize(genotypeCallsList.size());
                if (genotypeCallsList.size() >= pageSize) {
                    payload.getMetaData().getPagination().setNextPageToken(
                            genotypeCallsService.getNextPageToken());
                }
            }

            return ResponseEntity.ok(payload);

        }
        catch (GobiiException gE) {
            throw gE;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error" + e.getMessage()
            );
        }
    }

    @ApiOperation(
            value = "Download genotype calls",
            notes = "Download of all the genotype calls in a given Variantset",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(
            value="/variantsets/{variantSetDbId:[\\d]+}/calls/download",
            method=RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<ResponseBodyEmitter> handleRbe(
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            HttpServletRequest request

    ) {

        ResponseBodyEmitter emitter = new ResponseBodyEmitter((long)1800000);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

           try {

               Map<String, String> genotypesResult = genotypeCallsService.getGenotypeCallsAsString(
                       variantSetDbId, null);

               String genotypes = genotypesResult.get("genotypes");

               if(genotypesResult.containsKey("genotypes") &&
                       genotypesResult.get("genotypes") != null &&
                       genotypesResult.get("genotypes").length() != 0) {

                   emitter.send(genotypes, MediaType.TEXT_PLAIN);

                   while(genotypesResult.get("nextPageOffset") != null) {

                       genotypesResult = genotypeCallsService.getGenotypeCallsAsString(
                               variantSetDbId, genotypesResult.get("nextPageOffset"));

                       genotypes = genotypesResult.get("genotypes");

                       emitter.send(genotypes, MediaType.TEXT_PLAIN);
                   }

                   emitter.complete();
               }
               else {
                   emitter.complete();
               }
               emitter.complete();

           } catch (Exception e) {
               e.printStackTrace();
               emitter.completeWithError(e);
               return;
           }
            emitter.complete();
        });

        return ResponseEntity.ok().header(
                "Content-Disposition", "attachment; filename=" + variantSetDbId.toString() + ".csv"
        ).contentType(MediaType.parseMediaType("text/csv")
        ).body(emitter);
    }

}// BRAPIController
