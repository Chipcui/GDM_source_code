package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiResult;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.*;
import org.gobiiproject.gobiimodel.dto.system.PagedListByCursor;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.RestResourceLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Scope(value = "request")
@Controller
@EnableAsync
@RequestMapping(GobiiControllerType.SERVICE_PATH_BRAPI_V2)
@CrossOrigin
@Api
public class BRAPIIControllerV2 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV2.class);

    private final Integer brapiDefaultPageSize = 1000;


    @Autowired
    private DnaRunService dnaRunService = null;

    @Autowired
    private MarkerBrapiService markerBrapiService = null;

    @Autowired
    private GenotypeCallsService genotypeCallsService = null;

    @Autowired
    private SearchService searchService = null;

    @Autowired
    private ConfigSettingsService configSettingsService;

    @Autowired
    private MapsetBrapiService mapsetBrapiService;

    @Autowired
    private SamplesBrapiService samplesBrapiService;

    @Autowired
    private VariantSetsBrapiService variantSetsService;

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    private class CallSetResponse extends BrApiMasterPayload<DnaRunDTO>{}
    private class CallSetListResponse extends BrApiMasterPayload<BrApiResult<DnaRunDTO>>{}
    private class GenotypeCallsResponse extends BrApiMasterPayload<GenotypeCallsDTO>{}
    private class GenotypeCallsListResponse extends BrApiMasterPayload<BrApiResult<GenotypeCallsDTO>>{}
    private class VariantResponse extends BrApiMasterPayload<MarkerBrapiDTO>{}
    private class VariantListResponse extends BrApiMasterPayload<BrApiResult<MarkerBrapiDTO>>{}
    private class VariantSetResponse extends  BrApiMasterPayload<DataSetBrapiDTO>{}
    private class VariantSetListResponse extends BrApiMasterPayload<BrApiResult<DataSetBrapiDTO>>{}


    /**
     * List all BrApi compliant web services in GDM system
     *
     * @param request - request object
     * @return Json object with list of brapi calls in GDM
     * @throws Exception
     */
    @RequestMapping(value = "/serverinfo",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "Get ServerInfo",
            notes = "List of all calls",
            tags = {"ServerInfo"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="ServerInfo"),
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful",
                            response = BrapiResponseMapCalls.class
                    )
            }
    )
    @ResponseBody
    public ResponseEntity getCalls(
            HttpServletRequest request) throws Exception {


        return ResponseEntity.ok("");

    }



    /**
     * Lists the dnaruns by page size and page token
     *
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size
     * @return Brapi response with list of dna runs/call sets
     */
    @ApiOperation(
            value = "List all Callsets",
            notes = "List of all Callsets.",
            tags = {"Callsets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets",
                            response = CallSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required=true,
                paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallSets(
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) Integer pageToken,
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
                payload.getMetadata().getPagination().setPageSize(dnaRunList.size());
                if(dnaRunList.size() >= pageSize) {
                    payload.getMetadata().getPagination().setNextPageToken(
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
            value = "Get callset by callsetId",
            notes = "Retrieves the Callset entity having the specified ID",
            tags = {"Callsets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets : callSetDbId")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets", response = CallSetResponse.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
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
            ,
            hidden = true
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
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/callsets/{callSetDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallsByCallset(
            @ApiParam(value = "Id for dna run to be fetched")
            @PathVariable(value="callSetDbId") Integer callSetDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) throws Exception {


        try {


            PagedListByCursor<GenotypeCallsDTO> genotypeCallsList = genotypeCallsService.getGenotypeCallsByCallSetId(
                    callSetDbId,
                    pageSize, pageToken);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload = new BrApiMasterPayload<>(
                    genotypeCallsList.getListData());

            if(genotypeCallsList.getNextPageToken() != null) {
                payload.getMetadata().getPagination().setNextPageToken(genotypeCallsList.getNextPageToken());
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
     * Lists the variants by page size and page token
     *
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
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variants",
                            response = VariantListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required=true,
            paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariants(
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) Integer pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "page", required = false) Integer pageNum,
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

            List<MarkerBrapiDTO> markerList = markerBrapiService.getMarkers(pageToken, pageNum,
                    pageSize, markerBrapiDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(markerList);

            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (markerList.size() > 0) {
                payload.getMetadata().getPagination().setPageSize(markerList.size());
                if (markerList.size() >= pageSize) {
                    payload.getMetadata().getPagination().setNextPageToken(
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
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variant by Id",
                            response = VariantResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
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

    @ApiOperation(value="List samples", hidden = true)
    @RequestMapping(value="/samples", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getMaps(
            @RequestParam(value="sampleDbId", required=false) Integer sampleDbId,
            @RequestParam(value="observationUnitDbId", required=false) String observationUnitDbId,
            @RequestParam(value="germplasmDbId", required=false) Integer germplasmDbId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        try {

            if(page == null) {
                //First Page
                page = getDefaultBrapiPage();
            }


            List<SamplesBrapiDTO> samples = samplesBrapiService.getSamples(
                    page, pageSize,
                    sampleDbId, germplasmDbId,
                    observationUnitDbId);

            Map<String, Object> brapiResult = new HashMap<>();

            brapiResult.put("data", samples);

            BrApiMasterPayload<Map<String, Object>> payload = new BrApiMasterPayload(brapiResult);

            payload.getMetadata().getPagination().setCurrentPage(page);

            payload.getMetadata().getPagination().setPageSize(pageSize);


            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(payload);


        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }
    }


    @ApiOperation(
            value = "List Maps",
            notes = "List Genome maps in the database",
            tags = {"Maps"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Maps")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getMaps(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "type", required = false) String mapType) throws GobiiException {

        try {

            if(page == null) {
                //First Page
                page = getDefaultBrapiPage();
            }

            if(pageSize == null) {
                //TODO: Using same resource limit as markers. But, Can be defined seperately
                pageSize = getDefaultPageSize(RestResourceId.GOBII_MARKERS);
            }


            List<MapsetBrapiDTO> mapsetList = mapsetBrapiService.getMapSets(page, pageSize);

            Map<String, Object> brapiResult = new HashMap<>();

            brapiResult.put("data", mapsetList);

            BrApiMasterPayload<Map<String, Object>> payload = new BrApiMasterPayload(brapiResult);

            payload.getMetadata().getPagination().setCurrentPage(page);

            payload.getMetadata().getPagination().setPageSize(pageSize);


            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(payload);
            
        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }

    }

    @ApiOperation(
            value = "Get Maps by mapId",
            notes = "List Genome maps in the database",
            tags = {"Maps"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Maps : mapId")
                    })
            }
            ,
            hidden = true

    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps/{mapId}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getMapByMapId(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @PathVariable(value = "mapId") Integer mapId) throws GobiiException {

        try {

            if(page == null) {
                //First Page
                page = getDefaultBrapiPage();
            }
            if(pageSize == null) {

                //TODO: Using same resource limit as markers. But, Can be defined seperately
                pageSize = getDefaultPageSize(RestResourceId.GOBII_MARKERS);

            }

            MapsetBrapiDTO mapset = mapsetBrapiService.getMapSet(mapId, page, pageSize);

            BrApiMasterPayload<Map<String, Object>> payload = new BrApiMasterPayload(mapset);


            payload.getMetadata().getPagination().setCurrentPage(page);

            payload.getMetadata().getPagination().setPageSize(pageSize);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(payload);

        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Entity does not exist"
            );
        }

    }

    @ApiOperation(
            value = "Get Markers by mapId",
            notes = "List Genome maps in the database",
            tags = {"Maps"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Markers")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/maps/{mapId}/positions", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getMarkersByMapId(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "linkageGroupName", required = false) String linkageGroupName,
            @PathVariable(value = "mapId") Integer mapId) throws GobiiException {

        try {

            if(page == null) {
                //First Page
                page = getDefaultBrapiPage();
            }
            if(pageSize == null) {

                //TODO: Using same resource limit as markers. But, Can be defined seperately
                pageSize = getDefaultPageSize(RestResourceId.GOBII_MARKERS);

            }

            MapsetBrapiDTO mapset = mapsetBrapiService.getMapSet(mapId, page, pageSize);

            MarkerBrapiDTO markerFilter = new MarkerBrapiDTO();

            markerFilter.setMapSetId(mapset.getMapDbId());


            if(linkageGroupName != null) {
                markerFilter.setLinkageGroupName(linkageGroupName);
            }

            List<MarkerBrapiDTO> markers = markerBrapiService.getMarkers(
                    null, page, pageSize, markerFilter);

            Map<String, Object> brapiResult = new HashMap<>();

            brapiResult.put("data", markers);

            BrApiMasterPayload<Map<String, Object>> payload = new BrApiMasterPayload(brapiResult);


            payload.getMetadata().getPagination().setCurrentPage(page);
            payload.getMetadata().getPagination().setPageSize(pageSize);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(payload);

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
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
            value = "List Genotype Calls",
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
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variants/{variantDbId}/calls", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getCallsByVariant(
            @ApiParam(value = "Id for marker to be fetched")
            @PathVariable(value="variantDbId") Integer variantDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageToken,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) throws Exception {
        try {

            PagedListByCursor<GenotypeCallsDTO> genotypeCallsList = genotypeCallsService.getGenotypeCallsByVariantDbId(
                    variantDbId,
                    pageSize, pageToken);

            BrApiMasterPayload<List<GenotypeCallsDTO>> payload = new BrApiMasterPayload<>(
                    genotypeCallsList.getListData());

            if(genotypeCallsList.getNextPageToken() != null) {
                payload.getMetadata().getPagination().setNextPageToken(genotypeCallsList.getNextPageToken());
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
            value = "List variantsets",
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
                    @ApiResponse(code = 200, message = "Success",
                            response = VariantSetListResponse.class),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 400, message = "Bad Request",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 401, message = "Unauthorized",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 403, message = "Forbidden",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariantSets(
            @ApiParam(value = "Id of the VariantSet to be fetched. Also, corresponds to dataset Id")
            @RequestParam(value = "variantSetDbId", required = false) Integer variantSetDbId,
            @ApiParam(value = "Study Id for which list of Variantsets need to be fetched. studyDbID " +
                    "also corresponds to experiment")
            @RequestParam(value = "studyDbId", required = false) String studyDbId,
            @ApiParam(value = "Page Token to fetch a page. " +
                    "nextPageToken form previous page's meta data should be used." +
                    "If pageNumber is specified pageToken will be ignored. " +
                    "pageToken can be used to sequentially get pages faster. " +
                    "When an invalid pageToken is given the page will start from beginning.")
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @ApiParam(value = "Size of the page to be fetched. Default is 1000. Maximum page size is 1000")
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "Page number to be fetched")
            @RequestParam(value = "page", required = false) Integer pageNum,
            HttpServletRequest request
    ) {
        try {

            Integer maxPageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_DATASETS,
                    RestMethodType.GET
            );

            if (maxPageSize == null) {
                maxPageSize = this.brapiDefaultPageSize;
            }

            if (pageSize == null) {
                pageSize = this.brapiDefaultPageSize;
            }
            else if(pageSize > maxPageSize) {
                pageSize = maxPageSize;
            }

            variantSetsService.setCropType(CropRequestAnalyzer.getGobiiCropType(request));

            List<VariantSetDTO> variantSets = variantSetsService.listVariantSets(pageNum, pageSize, variantSetDbId);

            BrApiMasterListPayload<VariantSetDTO> payload = new BrApiMasterListPayload<>(variantSets);

            payload.getMetadata().getPagination().setPageSize(pageSize);

            payload.getMetadata().getPagination().setCurrentPage(pageNum);

            return ResponseEntity.ok().body(payload);

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
            value = "Get Variantset by variantSetDbId",
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
                    ),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 400, message = "Bad Request",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 401, message = "Unauthorized",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 403, message = "Forbidden",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 404, message = "Resource Not Found",
                            response = ErrorPayload.class),
                    @ApiResponse(code = 500, message = "Internal Server Error",
                            response = ErrorPayload.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/variantsets/{variantSetDbId:[\\d]+}", method=RequestMethod.GET)
    public @ResponseBody ResponseEntity getVariantSetById(
            @ApiParam(value = "ID of the VariantSet to be extracted", required = true)
            @PathVariable("variantSetDbId") Integer variantSetDbId,
            HttpServletRequest request) {

        try {

            variantSetsService.setCropType(CropRequestAnalyzer.getGobiiCropType(request));

            VariantSetDTO variantSetDTO = variantSetsService.getVariantSetById(variantSetDbId);

            BrApiMasterPayload<VariantSetDTO> payload = new BrApiMasterPayload<>(variantSetDTO);

            return ResponseEntity.ok().body(payload);

        }
        catch (GobiiException gE) {
            throw gE;
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
     * @param pageSize - Page size set by the user. If page size is more than maximum allowed page size,
     *                 then the response will have maximum page size
     * @return Brapi response with list of CallSets
     */
    @ApiOperation(
            value = "List Variants by VariantSetDbId",
            notes = "List of all the Variants in a specific VariantSet",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Variants")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of Variants by VariantSetId",
                            response = VariantListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
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
            @RequestParam(value = "page", required = false) Integer pageNum,
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

            List<MarkerBrapiDTO> markerList = markerBrapiService.getMarkers(pageToken, pageNum,
                    pageSize, markerBrapiDTOFilter);

            BrApiResult result = new BrApiResult();
            result.setData(markerList);
            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (markerList.size() > 0) {
                payload.getMetadata().getPagination().setPageSize(markerList.size());
                if (markerList.size() >= pageSize) {
                    payload.getMetadata().getPagination().setNextPageToken(
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
            value = "List Callsets by VariantSetDbId",
            notes = "List of all the CallSets in a specific VariantSet",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Callsets")
                    })
            }
            ,
            hidden = true
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful retrieval of CallSets by VariantSetId",
                            response = CallSetListResponse.class
                    )
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value="Authentication Token", required = true,
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
                payload.getMetadata().getPagination().setPageSize(dnaRunList.size());
                if (dnaRunList.size() >= pageSize) {
                    payload.getMetadata().getPagination().setNextPageToken(
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
            value = "Creates a searchResultDbId for Genotype Calls search",
            notes = "Creates ",
            tags = {"GenotypeCalls"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Search")
                    })
            },
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = {
            "/search/calls",
            "/search/variantsets/",
            "/search/callsets"},
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity searchGenotypeCalls(
            HttpEntity<String> searchQuery,
            HttpServletRequest request
    ) {
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);


            if (searchQuery.hasBody()) {

                SearchResultDTO searchResultDTO = searchService.createSearchQueryResource(cropType,
                        searchQuery.getBody());

                BrApiMasterPayload<SearchResultDTO> payload = new BrApiMasterPayload<>(searchResultDTO);

                return  ResponseEntity.status(HttpStatus.CREATED).body(payload);

            }
            else {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Missing Request body"
                );
            }

        }
        catch (GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error " + e.getMessage()
            );

        }
    }

    @RequestMapping(value = "/search/calls/{searchResultDbId}")
    public ResponseEntity getSearchCalls(
            @ApiParam()
            @PathVariable String searchResultDbId,
            HttpServletRequest request) {
        return ResponseEntity.ok("");
    }


    @ApiOperation(
            value = "Create an extract ",
            notes = "Creates a variant set resource for given extract query",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="VariantSets")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/variantsets/extract", method=RequestMethod.POST)
    public ResponseEntity<String> VariantSetsExtract(
            HttpEntity<String> extractQuery,
            HttpServletRequest request) throws Exception {

        return ResponseEntity.ok("");
    }

    @ApiOperation(
            value = "List Genotype Calls",
            notes = "List of all the genotype calls in a given Variantset",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
            ,
            hidden = true
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
                    name="Authorization", value="Authentication Token", required=true,
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
            @RequestParam(value = "pageSize", required = false, defaultValue = BrapiDefaults.pageSizeStr) Integer pageSize,
            HttpServletRequest request
    ){

        try {
            List<GenotypeCallsDTO> genotypeCallsList = new ArrayList<>();

            Integer variantSetDbId;

            try {

                variantSetDbId = Integer.parseInt(variantSetDbIdVar);

                genotypeCallsList = genotypeCallsService.getGenotypeCallsByDatasetId(
                        variantSetDbId, pageSize, pageToken);

            }
            catch(NumberFormatException | NullPointerException ne) {

                String cropType = CropRequestAnalyzer.getGobiiCropType(request);

                String extractQueryPath = LineUtils.terminateDirectoryPath(
                        configSettingsService.getConfigSettings().getServerConfigs().get(
                                cropType).getFileLocations().get(GobiiFileProcessDir.RAW_USER_FILES)
                ) + variantSetDbIdVar + LineUtils.PATH_TERMINATOR + "extractQuery.json";

                genotypeCallsList =
                        genotypeCallsService.getGenotypeCallsByExtractQuery(
                                extractQueryPath, pageSize, pageToken);
            }


            BrApiResult result = new BrApiResult();

            result.setData(genotypeCallsList);

            BrApiMasterPayload payload = new BrApiMasterPayload(result);

            if (genotypeCallsList.size() > 0) {
                payload.getMetadata().getPagination().setPageSize(genotypeCallsList.size());
                if (genotypeCallsList.size() >= pageSize) {
                    payload.getMetadata().getPagination().setNextPageToken(
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
            value = "Download Genotype Calls",
            notes = "Download of all the genotype calls in a given Variantset",
            tags = {"VariantSets"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="GenotypeCalls")
                    })
            }
            ,
            hidden = true
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name="Authorization", value="Authentication Token", required=true,
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

        //Giving Response emitter to finish the download within 30 mins.
        //The request thread will be terminated once 30 mins is done.
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

    /**
     * Brapi pages are 0 indexed and first page number is 0
     * @return First page number
     */
    public Integer getDefaultBrapiPage() {

        return 0;
    }

    /**
     * Gets the default page size
     * @param restResourceId Resource Id for which default page size needs to be fetched. Example: GOBII_MARKERS
     * @return
     */
    public Integer getDefaultPageSize(RestResourceId restResourceId) {

        Integer pageSize = 1000;

        try {

            //TODO: Using same resource limit as markers. define different resource limit if required
            pageSize = RestResourceLimits.getResourceLimit(
                    RestResourceId.GOBII_MARKERS,
                    RestMethodType.GET
            );

            if(pageSize == null) {
                pageSize = 1000;
            }
        }
        catch(Exception e) {
            //If resource limit is not defined
            pageSize = 1000;
        }

        return pageSize;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "error";
    }

}// BRAPIController
