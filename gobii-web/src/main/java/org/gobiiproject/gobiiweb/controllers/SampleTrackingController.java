package org.gobiiproject.gobiiweb.controllers;

import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterPayload;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ListPayload;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.*;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.*;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(value="request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_SAMPLE_TRACKING)
@CrossOrigin
public class SampleTrackingController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SampleTrackingController.class);

    @Autowired
    private ProjectService<ProjectDTO> sampleTrackingProjectService = null;

    @Autowired
    private ExperimentService sampleTrackingExperimentService = null;

    @Autowired
    private DnaSampleService sampleTrackingDnasampleService = null;

    @Autowired
    private FilesService fileService = null;

    /**
     * Authenticates the User.
     * @param noContentExpected
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(
            value = "Authentication",
            notes = "Authentication",
            tags = {"Authentication"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Authentication")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Username", value="User Id", required=true,
                    paramType = "header", dataType = "string"),
            @ApiImplicitParam(name="X-Password", value="User password", required=true,
                    paramType = "header", dataType = "string"),
    })
    @ApiResponses(value={
            @ApiResponse(code=200, message = "OK", responseHeaders=@ResponseHeader(
                    name="X-Auth-Token ", description = "API key to authenticate GDM api calls",
                    response = String.class
            ))
    })
    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity authenticate(@RequestBody(required = false) String noContentExpected,
                               HttpServletRequest request,
                               HttpServletResponse response) throws GobiiException {

        HeaderAuth dtoHeaderAuth = new HeaderAuth();

        try {


            dtoHeaderAuth.setToken(response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN));
            dtoHeaderAuth.setGobiiCropType(response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_GOBII_CROP));
            dtoHeaderAuth.setUserName(response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME));


        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Authentication Failure");
        }

        return ResponseEntity.ok(dtoHeaderAuth);
    }

    /**
     * Lists the projects by page size and page token.
     *
     * @param pageSize - Page Size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size.
     * @param pageNum - Page number for the list defined by pageSize.
     * @return Brapi response with list of projects.
     */
    @ApiOperation(
            value = "List all projects",
            notes = "List of all Projects.",
            tags = {"Project"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string"),
    })
    @ApiResponses(value={@ApiResponse(code=200, message="successful operation",
            response=ProjectDTO.class)})
    @RequestMapping(value="/projects", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity listProjects(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        try {

            if(pageSize == null) {
                //Default Brapi Page Size.
                //TODO: Add it to the Gobii Web XML resource limit
                pageSize = 1000;
            }

            if(pageNum == null) {
                pageNum = 0;
            }

            List<ProjectDTO> projectsList = sampleTrackingProjectService.getProjects(pageNum, pageSize);

            Map<String, List<ProjectDTO>> brapiDataList = new HashMap<>();
            brapiDataList.put("data", projectsList);

            BrApiMasterPayload<Object> payload = new BrApiMasterPayload<>(brapiDataList);

            payload.getMetadata().getPagination().setPageSize(pageSize);
            payload.getMetadata().getPagination().setCurrentPage(pageNum);

            return ResponseEntity.ok(payload);

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error");
        }
    }


    /**
     * Endpoint for getting a specific project with the given project ID
     *
     * @param projectId ID of the requested project
     * @return ResponseEntity with http status code specifying if retrieval of the project is successful.
     * Response body contains the requested Project information
     */
    @ApiOperation(
            value = "Get a project by projectId",
            notes = "Retrieves the Project entity having the specified ID.",
            tags = {"Project"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects : projectId")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
            paramType = "header", dataType = "string"),
    })
    @RequestMapping(value="/projects/{projectId:[\\d]+}", method=RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity getProjectById(
            @ApiParam(value = "ID of the Project to be extracted", required = true)
            @PathVariable Integer projectId
    ) throws GobiiException {
            ProjectDTO project = sampleTrackingProjectService.getProjectById(projectId);
            BrApiMasterPayload<ProjectDTO> payload = new BrApiMasterPayload<>(project);
            return ResponseEntity.ok(payload);
    }


    /**
     * Endpoint to create new project.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newProject - New project to be created.
     *                   Json request body automatically deserialized to ProjectDTO.
     * @return ResponseEntity with http status code respective of successful creation or failure.
     * Response body contains created resource if project creation is successful.
     */
    @ApiOperation(
            value = "Create new project",
            notes = "Creates a new project in the system.",
            tags = {"Project"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Projects")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/projects", method=RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity createProject(
            @ApiParam(required = true)
            @RequestBody ProjectDTO newProject) throws GobiiException {

        ProjectDTO createdProject = sampleTrackingProjectService.createProject(newProject);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }


    /**
     * Lists the experiments by page size and page token.
     *
     * @param pageTokenParam - String page token.
     * @param pageSize - Page Size set by the user. If page size is more than maximum allowed
     *                 page size, then the response will have maximum page size.
     * @param pageNum - Page number for the list defined by pageSize.
     * @return Brapi response with list of experiments.
     */
    @ApiOperation(
            value = "List all experiments",
            notes = "List of all Experiments.",
            tags = {"Experiment"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Experiments")
                    })}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required=true,
                    paramType = "header", dataType = "string"),
    })
    @ApiResponses(value={@ApiResponse(code=200, message="successful operation",
            response=ExperimentDTO.class)})
    @RequestMapping(value="/experiments", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody ResponseEntity listExperiment(
            @RequestParam(value = "pageToken", required = false) String pageTokenParam,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum
    ) {
        try {

            List<ExperimentDTO> experimentsList = sampleTrackingExperimentService.getExperiments();

            ListPayload<ExperimentDTO> payload = new ListPayload<ExperimentDTO>();

            payload.setData(experimentsList);

            return ResponseEntity.ok(payload);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    /**
     * Endpoint to create new experiment.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newExperiment - New experiment to be created.
     *                   Json request body automatically deserialized to ExperimentDTO.
     * @param dataFile - File associated with Experiment to be created.
     * @return ResponseEntity with http status code respective of successful creation or failure.
     * Response body contains created resource if experiment creation is successful.
     */
    @ApiOperation(
            value = "Create new experiment",
            notes = "Creates new experiment in the system.",
            tags = {"Experiment"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Experiments"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="experimentMetaData", value="The file to be uploaded",
                    required = true, dataType = "string",
                    paramType = "formData"),
            @ApiImplicitParam(name="dataFile", value="The file to be uploaded",
                    dataType = "string", paramType = "formData"),
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value="/experiments", method=RequestMethod.POST, consumes="multipart/form-data")
    public @ResponseBody ResponseEntity createExperiment(
            @ApiParam(hidden = true)
            @RequestPart(name = "dataFile", required = false) MultipartFile dataFile,
            @ApiParam(hidden = true)
            @RequestPart(name="experimentMetaData") ExperimentDTO newExperiment,
            HttpServletRequest request)  throws GobiiException {

        newExperiment = (ExperimentDTO) sampleTrackingExperimentService.createExperiment(newExperiment);

        if(dataFile == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newExperiment);
        }

        //Save the data file.
        try {

            byte[] dataFileBytes = dataFile.getBytes();

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            String dataFileName = dataFile.getOriginalFilename();

            if(dataFileName.isEmpty()) {
                dataFileName = "experimentDataFile";
            }

            String dataFilePath = fileService.writeExperimentDataFile(cropType, dataFileName, dataFileBytes);

            boolean updateSuccess = sampleTrackingExperimentService.updateExperimentDataFile(
                    newExperiment.getExperimentId(), dataFilePath);

            return ResponseEntity.status(HttpStatus.CREATED).body(newExperiment);

        }
        catch (Exception e) {

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal server error.");
        }
    }

    /**
     * Endpoint for getting a specific experiment with the given experiment ID
     *
     * @param experimentId ID of the requested experiment
     * @return ResponseEntity with http status code specifying if retrieval of the Experiment is successful.
     * Response body contains the requested Experiment information
     */
    @ApiOperation(
            value = "Get an experiment by experimentId",
            notes = "Retrieves the Experiment entity having the specified ID.",
            tags = {"Experiment"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Experiment : experimentId")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string"),
    })
    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody ResponseEntity getExperimentById(
            @PathVariable Integer experimentId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            ExperimentDTO experimentDTO = (ExperimentDTO) sampleTrackingExperimentService.getExperimentById(
                    experimentId);
            BrApiMasterPayload<ExperimentDTO> payload = new BrApiMasterPayload<>(experimentDTO);
            return ResponseEntity.ok(payload);
        }
        catch (GobiiException gobiiE) {
            throw gobiiE;
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    /**
     * Endpoint to create new samples for given projectId.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newProjectSamples - List samples to be created to project.
     *                          Json request body automatically deserialized to ProjectSamplesDTO.
     *                          projectId and Samples[] will be the request body.
     * @return ResponseEntity with http status code 200 for OK accepted.
     * Response body contains jobId and related details to check the success/failure of asynchronous operation.
     */
    @ApiOperation(
            value = "Create new samples",
            notes = "Creates a new samples in the system.",
            tags = {"Sample"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Samples")
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/samples", method=RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity createSamples(
        @RequestBody ProjectSamplesDTO newProjectSamples) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(newProjectSamples);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    /**
     * Endpoint to upload samples for given project Id.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param sampleFile - Tab delimited sample file, with respective columns as per design document.
     * @param sampleMetaData - Object with project Id and Map object to map input file headers to
     *                       gdm properties.
     * @return ResponseEntity with http status code 200 for OK accepted.
     * Response body contains jobId and related details to check the success of asynchronous operation.
     *
     */
    @ApiOperation(
            value = "Upload new samples",
            notes = "Creates a new samples in the system.",
            tags = {"Sample"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Upload Samples"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="sampleMetaData", value="The file to be uploaded",
                    dataType = "string", paramType = "formData"),
            @ApiImplicitParam(name="sampleFile", value="The file to be uploaded",
                    dataType = "string", paramType = "formData", required = true),
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/samples/upload", method=RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity uploadSamples(
            @ApiParam(hidden = true)
            @RequestPart("sampleFile") MultipartFile sampleFile,
            @ApiParam(hidden = true)
            @RequestPart("sampleMetaData") SampleMetadataDTO sampleMetaData,
            HttpServletRequest request) {
        try {

            byte[] sampleInputBytes = sampleFile.getBytes();

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            JobStatusDTO uploadSampleJob = (JobStatusDTO) (
                    sampleTrackingDnasampleService.uploadSamples(sampleInputBytes, sampleMetaData, cropType));

            return ResponseEntity.status(HttpStatus.CREATED).body(uploadSampleJob);

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Unknown Error. Please contact system administrator.");
        }


    }

    /**
     * Endpoint to create dataset under given experiment Id.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param newDataset - Dataset metadata.
     * @return ResponseEntity with http status code 201 if successful.
     */
    @ApiOperation(
            value = "Create new dataset",
            notes = "Creates a new dataset in the system.",
            tags = {"Dataset"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Dataset"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/dataset", method=RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity createDataset(@RequestBody DataSetDTO newDataset) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    /**
     * Endpoint to upload data to given Dataset Id.
     * Exceptions are handled in GlobalControllerExceptionHandler.
     * @param genotypeFile - Genotype files as application/zip
     * @param datasetMetaData - Meta data related to dataset and Vendor file urls to
     *                        download data from external sources.
     * @return ResponseEntity with http status code 200 for OK accepted.
     * Response body contains jobId and related details to check the success of asynchronous operation.
     */
    @ApiOperation(
            value = "Upload data to dataset",
            notes = "Upload data to the dataset.",
            tags = {"Dataset"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Upload Data"),
                    })
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name="datasetMetaData", value="Metadata associated with data",
                    dataType = "string", paramType = "formData", required = true),
            @ApiImplicitParam(name="genotypeFile", value="The file to be uploaded",
                    dataType = "string", paramType = "formData", required = true),
            @ApiImplicitParam(name="X-Auth-Token", value="Authentication Token", required = true,
                    paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/dataset/{datasetId}/data", method=RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity uploadDatasetData(
            @ApiParam(hidden = true)
            @RequestPart("genotypeFile") MultipartFile genotypeFile,
            @ApiParam(hidden = true)
            @RequestPart("genotypeMetaData") DataSetDataDTO datasetMetaData,
            @PathVariable Integer datasetId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }


    @ApiOperation(value="dummy", hidden = true)
    @RequestMapping(value = "/germplasm", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity createGermplasm(
            @RequestBody GermplasmListDTO germplasmListDTO,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            // temporary only
            int idCount = 0;
            for (GermplasmDTO germplasmDTO: germplasmListDTO.getGermplasms()) {
                germplasmDTO.setId(idCount++);

            }

            return ResponseEntity.ok(germplasmListDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    @ApiOperation(value="dummy", hidden = true)
    @RequestMapping(value = "/germplasm", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity listGermplasms(
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            List<GermplasmDTO> germplasmDTOList = new ArrayList<>();

            GermplasmDTO germplasmDTO = new GermplasmDTO();
            germplasmDTO.setGermplasmName("foo germplasm");
            germplasmDTO.setExternalCode("external bar code");
            germplasmDTO.setSpeciesName("foo species");
            germplasmDTO.setTypeName("foo type");

            germplasmDTOList.add(germplasmDTO);

            return ResponseEntity.ok(germplasmDTOList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }

    @ApiOperation(value="dummy", hidden = true)
    @RequestMapping(value = "/germplasm/{germplasmId:[\\d]+}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity listGermplasms(
            @PathVariable Integer germplasmId,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {

            GermplasmDTO germplasmDTO = new GermplasmDTO();

            return ResponseEntity.ok(germplasmDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }

    }



}
