// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobidomain.services.ManifestService;
import org.gobiiproject.gobidomain.services.MapsetService;
import org.gobiiproject.gobidomain.services.MarkerGroupService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobidomain.services.OrganizationService;
import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobidomain.services.ReferenceService;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.EntityNameConverter;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.automation.ControllerUtils;
import org.gobiiproject.gobiiweb.automation.PayloadReader;
import org.gobiiproject.gobiiweb.automation.PayloadWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * Created by MrPhil on 7/6/2015.
 */
@Scope(value = "request")
@Controller
@RequestMapping("/brapi/v1")
public class BRAPIController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIController.class);

    @Autowired
    private PingService pingService = null;

    @Autowired
    private ProjectService projectService = null;

    @Autowired
    private ContactService contactService = null;

    @Autowired
    private ReferenceService referenceService = null;

    @Autowired
    private AnalysisService analysisService = null;

    @Autowired
    private ManifestService manifestService = null;

    @Autowired
    private MarkerGroupService markerGroupService = null;

    @Autowired
    private OrganizationService organizationService = null;

    @Autowired
    private ExperimentService experimentService = null;

    @Autowired
    private NameIdListService nameIdListService = null;

    @Autowired
    private LoaderInstructionFilesService loaderInstructionFilesService = null;

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService = null;

    @Autowired
    private DisplayService displayService = null;

    @Autowired
    private CvService cvService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

    @Autowired
    private MapsetService mapsetService = null;

    @Autowired
    private ConfigSettingsService configSettingsService;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public PingDTO getPingResponse(@RequestBody PingDTO pingDTORequest) {

        PingDTO returnVal = new PingDTO();

        try {
            returnVal = pingService.getPings(pingDTORequest);
            String newResponseString = LineUtils.wrapLine("Loader controller responded");
            returnVal.getPingResponses().add(newResponseString);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }//getPingResponse()

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public String authenticate(@RequestBody String noContentExpected) {

        String returnVal = null;
        try {
            returnVal = "Authenticated: " + (new Date()).toString();
        } catch (Exception e) {
            String msg = e.getMessage();
            String tmp = msg;
            try {
                throw (e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return (returnVal);

    }

    @RequestMapping(value = "/configsettings", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ConfigSettingsDTO> getContactsById(
            HttpServletRequest request,
            HttpServletResponse response) {

        PayloadEnvelope<ConfigSettingsDTO> returnVal = new PayloadEnvelope<>();
        try {

            ConfigSettingsDTO configSettingsDTO = configSettingsService.getConfigSettings();
            if (null != configSettingsDTO) {

                PayloadWriter<ConfigSettingsDTO> payloadWriter = new PayloadWriter<>(request,
                        ConfigSettingsDTO.class);

                payloadWriter.writeSingleItemForDefaultId(returnVal,
                        ServiceRequestId.URL_CONFIGSETTINGS,
                        configSettingsDTO);

                returnVal.getPayload().getData().add(configSettingsDTO);

            } else {
                returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unable to retrieve config settings");
            }

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** CONTACT METHODS
    // *********************************************
    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> createContact(@RequestBody PayloadEnvelope<ContactDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ContactDTO> payloadReader = new PayloadReader<>(ContactDTO.class);
            ContactDTO contactDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ContactDTO contactDTONew = contactService.createContact(contactDTOToCreate);

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request,
                    ContactDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_CONTACTS,
                    contactDTONew);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/contacts/{contactId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> replaceContact(@RequestBody PayloadEnvelope<ContactDTO> payloadEnvelope,
                                                      @PathVariable Integer contactId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ContactDTO> payloadReader = new PayloadReader<>(ContactDTO.class);
            ContactDTO contactDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ContactDTO contactDTOReplaced = contactService.replaceContact(contactId, contactDTOToReplace);

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request,
                    ContactDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_CONTACTS,
                    contactDTOReplaced);


        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/contacts/{contactId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsById(@PathVariable Integer contactId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            ContactDTO contactDTO = contactService.getContactById(contactId);
            if (null != contactDTO) {

                PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request,
                        ContactDTO.class);

                payloadWriter.writeSingleItemForDefaultId(returnVal,
                        ServiceRequestId.URL_CONTACTS,
                        contactDTO);

            } else {
                returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unable to retrieve a contact with contactId " + contactId);
            }

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContacts(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<ContactDTO> payloadReader = new PayloadReader<>(ContactDTO.class);
            List<ContactDTO> platformDTOs = contactService.getContacts();

            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request,
                    ContactDTO.class);

            payloadWriter.writeList(returnVal,
                    ServiceRequestId.URL_CONTACTS,
                    platformDTOs);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    //Technically, this regex specifies an email address format, and it actually works.
    //However, when you execute this, you get back an error "The resource identified by this request is only
    // capable of generating responses with characteristics not acceptable according to the request "accept" headers."
    // In other words, the email address is telling the server that you're asking for some other format
    // So for email based searches, you'll have to use the request parameter version
    @RequestMapping(value = "/contacts/{email:[a-zA-Z-]+@[a-zA-Z-]+.[a-zA-Z-]+}",
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsByEmail(@PathVariable String email,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            returnVal.getHeader().getStatus().addStatusMessage(GobiiStatusLevel.ERROR, "Method not implemented");

//            ContactDTO contactRequestDTO = new ContactDTO();
//            contactRequestDTO.setContactId(1);
            //contactRequestDTO.setEmail(email);
            //returnVal = contactService.processDml(new PayloadEnvelope<>(contactRequestDTO, GobiiProcessType.READ));

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // Example: http://localhost:8282/gobii-dev/brapi/v1/contact-search?email=foo&lastName=bar&firstName=snot
    // all parameters must be present, but they don't all neeed a value
    @RequestMapping(value = "/contact-search",
            params = {"email", "lastName", "firstName"},
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsBySearch(@RequestParam("email") String email,
                                                           @RequestParam("lastName") String lastName,
                                                           @RequestParam("firstName") String firstName,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {
            ContactDTO contactDTO = contactService.getContactByEmail(email);
            PayloadWriter<ContactDTO> payloadWriter = new PayloadWriter<>(request,
                    ContactDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_CONTACTS,
                    contactDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** DATASET METHODS
    // *********************************************
    @RequestMapping(value = "/datasets", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> createDataSet(@RequestBody PayloadEnvelope<DataSetDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<DataSetDTO> payloadReader = new PayloadReader<>(DataSetDTO.class);
            DataSetDTO dataSetDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            DataSetDTO dataSetDTONew = dataSetService.createDataSet(dataSetDTOToCreate);


            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request,
                    DataSetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_DATASETS,
                    dataSetDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/datasets/{dataSetId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> replaceDataSet(@RequestBody PayloadEnvelope<DataSetDTO> payloadEnvelope,
                                                      @PathVariable Integer dataSetId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<DataSetDTO> payloadReader = new PayloadReader<>(DataSetDTO.class);
            DataSetDTO dataSetDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            DataSetDTO dataSetDTOReplaced = dataSetService.replaceDataSet(dataSetId, dataSetDTOToReplace);


            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request,
                    DataSetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_DATASETS,
                    dataSetDTOReplaced);
//

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/datasets", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> getDataSets(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<DataSetDTO> payloadReader = new PayloadReader<>(DataSetDTO.class);
            List<DataSetDTO> dataSetDTOs = dataSetService.getDataSets();

            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request,
                    DataSetDTO.class);

            payloadWriter.writeList(returnVal,
                    ServiceRequestId.URL_DATASETS,
                    dataSetDTOs);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/datasets/{dataSetId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<DataSetDTO> getDataSetsById(@PathVariable Integer dataSetId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<DataSetDTO> returnVal = new PayloadEnvelope<>();
        try {

            DataSetDTO dataSetDTO = dataSetService.getDataSetById(dataSetId);

            PayloadWriter<DataSetDTO> payloadWriter = new PayloadWriter<>(request,
                    DataSetDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_DATASETS,
                    dataSetDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** LOADER INSTRUCTION METHODS
    // *********************************************

    @RequestMapping(value = "/instructions/loader", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<LoaderInstructionFilesDTO> createLoaderInstruction(@RequestBody PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) {

        PayloadEnvelope<LoaderInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<LoaderInstructionFilesDTO> payloadReader = new PayloadReader<>(LoaderInstructionFilesDTO.class);
            LoaderInstructionFilesDTO loaderInstructionFilesDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            LoaderInstructionFilesDTO loaderInstructionFilesDTONew = loaderInstructionFilesService.createInstruction(cropType, loaderInstructionFilesDTOToCreate);

            PayloadWriter<LoaderInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request,
                    LoaderInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS,
                    loaderInstructionFilesDTONew,
                    loaderInstructionFilesDTONew.getInstructionFileName());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/instructions/loader/{instructionFileName}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<LoaderInstructionFilesDTO> getLoaderInstruction(@PathVariable("instructionFileName") String instructionFileName,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response) {

        PayloadEnvelope<LoaderInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            LoaderInstructionFilesDTO loaderInstructionFilesDTO = loaderInstructionFilesService.getInstruction(cropType, instructionFileName);

            PayloadWriter<LoaderInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request,
                    LoaderInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS,
                    loaderInstructionFilesDTO,
                    loaderInstructionFilesDTO.getInstructionFileName());

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** EXTRACTOR INSTRUCTION METHODS
    // *********************************************

    @RequestMapping(value = "/instructions/extractor", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ExtractorInstructionFilesDTO> createExtractorInstruction(@RequestBody PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope,
                                                                                    HttpServletRequest request,
                                                                                    HttpServletResponse response) {

        PayloadEnvelope<ExtractorInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ExtractorInstructionFilesDTO> payloadReader = new PayloadReader<>(ExtractorInstructionFilesDTO.class);
            ExtractorInstructionFilesDTO extractorInstructionFilesDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);
            String cropType = CropRequestAnalyzer.getGobiiCropType(request);

            ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService.createInstruction(cropType, extractorInstructionFilesDTOToCreate);


            PayloadWriter<ExtractorInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request,
                    ExtractorInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    ServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS,
                    extractorInstructionFilesDTONew,
                    extractorInstructionFilesDTONew.getInstructionFileName());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/instructions/extractor/{instructionFileName}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ExtractorInstructionFilesDTO> getExtractorInstruction(@PathVariable("instructionFileName") String instructionFileName,
                                                                                 HttpServletRequest request,
                                                                                 HttpServletResponse response) {

        PayloadEnvelope<ExtractorInstructionFilesDTO> returnVal = new PayloadEnvelope<>();
        try {

//            PayloadReader<ExtractorInstructionFilesDTO> payloadReader = new PayloadReader<>(ExtractorInstructionFilesDTO.class);
//            ExtractorInstructionFilesDTO extractorInstructionFilesDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);
            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            ExtractorInstructionFilesDTO extractorInstructionFilesDTO = extractorInstructionFilesService.getInstruction(cropType, instructionFileName);


            PayloadWriter<ExtractorInstructionFilesDTO> payloadWriter = new PayloadWriter<>(request,
                    ExtractorInstructionFilesDTO.class);

            payloadWriter.writeSingleItemForId(returnVal,
                    ServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS,
                    extractorInstructionFilesDTO,
                    extractorInstructionFilesDTO.getInstructionFileName());

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** NameIDList
    // *********************************************
    @RequestMapping(value = "/names/{entity}",
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<NameIdDTO> getNames(@PathVariable("entity") String entity,
                                               @RequestParam(value = "filterType", required = false) String filterType,
                                               @RequestParam(value = "filterValue", required = false) String filterValue,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        PayloadEnvelope<NameIdDTO> returnVal = new PayloadEnvelope<>();
        try {

            // We are getting raw string parameters from the uri and query parameters;
            // here is the place to validate the types before sending the parameters on the service layer,
            // which should only be dealing with GOBII native natives.
            //
            // **************** Get entity type
            GobiiEntityNameType gobiiEntityNameType;
            try {
                gobiiEntityNameType = GobiiEntityNameType.valueOf(entity.toUpperCase());
            } catch (IllegalArgumentException e) {

                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unsupported entity for list request: " + entity);
            }


            // **************** If a filter was specified, convert it as well
            GobiiFilterType gobiiFilterType = GobiiFilterType.NONE;
            Object typedFilterValue = filterValue;
            if (!LineUtils.isNullOrEmpty(filterType)) {
                try {
                    gobiiFilterType = GobiiFilterType.valueOf(filterType.toUpperCase());
                } catch (IllegalArgumentException e) {

                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONE,
                            "Unsupported filter for list request: " + filterType);
                }

                if (!LineUtils.isNullOrEmpty(filterValue)) {

                    if (GobiiFilterType.BYTYPEID == gobiiFilterType) {
                        if (NumberUtils.isNumber(filterValue)) {
                            typedFilterValue = Integer.valueOf(filterValue);
                        } else {
                            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                    GobiiValidationStatusType.NONE,
                                    "Value for "
                                            + filterType
                                            + " value is not a number: "
                                            + filterValue);
                        }

                    } else if (GobiiFilterType.BYTYPENAME == gobiiFilterType) {
                        // there is nothing to test here -- the string could be anything
                        // add additional validation tests for other filter types

                    } else {
                        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.NONE,
                                "Unable to do type checking on filter value for filter type "
                                        + filterType
                                        + " with value "
                                        + filterValue);
                    }

                } else {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONE,
                            "A value was not supplied for filter: " + filterType);
                }
            }


            DtoMapNameIdParams dtoMapNameIdParams = new DtoMapNameIdParams(gobiiEntityNameType, gobiiFilterType, typedFilterValue);

            List<NameIdDTO> nameIdList = nameIdListService.getNameIdList(dtoMapNameIdParams);

            PayloadWriter<NameIdDTO> payloadWriter = new PayloadWriter<>(request, NameIdDTO.class);
            payloadWriter.writeList(returnVal,
                    EntityNameConverter.toServiceRequestId(gobiiEntityNameType),
                    nameIdList);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** ORGANIZATION METHODS
    // *********************************************

    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> createOrganization(@RequestBody PayloadEnvelope<OrganizationDTO> payloadEnvelope,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            OrganizationDTO OrganizationDTONew = organizationService.createOrganization(organizationDTOToCreate);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_ORGANIZATION,
                    OrganizationDTONew);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/organizations/{organizationId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> replaceOrganization(@RequestBody PayloadEnvelope<OrganizationDTO> payloadEnvelope,
                                                                @PathVariable Integer organizationId,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            OrganizationDTO organizationDTOReplaced = organizationService.replaceOrganization(organizationId, organizationDTOToReplace);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_ORGANIZATION,
                    organizationDTOReplaced);


        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> getOrganizations(HttpServletRequest request,
                                                             HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            List<OrganizationDTO> organizationDTOs = organizationService.getOrganizations();

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request,
                    OrganizationDTO.class);

            payloadWriter.writeList(returnVal,
                    ServiceRequestId.URL_ORGANIZATION,
                    organizationDTOs);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/organizations/{organizationId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<OrganizationDTO> getOrganizationsById(@PathVariable Integer organizationId,
                                                                 HttpServletRequest request,
                                                                 HttpServletResponse response) {

        PayloadEnvelope<OrganizationDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<OrganizationDTO> payloadReader = new PayloadReader<>(OrganizationDTO.class);
            OrganizationDTO organizationDTO = organizationService.getOrganizationById(organizationId);

            PayloadWriter<OrganizationDTO> payloadWriter = new PayloadWriter<>(request,
                    OrganizationDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_ORGANIZATION,
                    organizationDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** MAPSET METHODS
    // *********************************************
    /*
    * NOTE: this implementation is incorrect: it is using getAllmapsetNames;
    * There needs to be a getAllMapset() method added. For now, the funcitonality
    * Provided by the LoadControlle remains in place and the client side tets have
    * not been modified. This funcitonality will have to be built out later.
    * Also note that the resource name /maps is correct but does not match
    * what is being used in ResourceBuilder on the client side*/
    @RequestMapping(value = "/maps", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<MapsetDTO> getMaps(HttpServletRequest request,
                                              HttpServletResponse response) {

        PayloadEnvelope<MapsetDTO> returnVal = new PayloadEnvelope<>();
        try {

            List<MapsetDTO> mapsetDTOs = mapsetService.getAllMapsetNames();
            for (MapsetDTO currentMapsetDTO : mapsetDTOs) {
                returnVal.getPayload().getData().add(currentMapsetDTO);
            }

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    // *********************************************
    // *************************** PLATFORM METHODS
    // *********************************************

    @RequestMapping(value = "/platforms", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> createPlatform(@RequestBody PayloadEnvelope<PlatformDTO> payloadEnvelope,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            PlatformDTO platformDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            PlatformDTO platformDTONew = platformService.createPlatform(platformDTOToCreate);

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request,
                    PlatformDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_PLATFORM,
                    platformDTONew);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/platforms/{platformId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> replacePlatform(@RequestBody PayloadEnvelope<PlatformDTO> payloadEnvelope,
                                                        @PathVariable Integer platformId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            PlatformDTO platformDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            PlatformDTO platformDTOReplaced = platformService.replacePlatform(platformId, platformDTOToReplace);

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request,
                    PlatformDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_PLATFORM,
                    platformDTOReplaced);


        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/platforms", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> getPlatforms(HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            List<PlatformDTO> platformDTOs = platformService.getPlatforms();

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request,
                    PlatformDTO.class);

            payloadWriter.writeList(returnVal,
                    ServiceRequestId.URL_PLATFORM,
                    platformDTOs);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/platforms/{platformId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<PlatformDTO> getPlatformsById(@PathVariable Integer platformId,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {

        PayloadEnvelope<PlatformDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<PlatformDTO> payloadReader = new PayloadReader<>(PlatformDTO.class);
            PlatformDTO platformDTO = platformService.getPlatformById(platformId);

            PayloadWriter<PlatformDTO> payloadWriter = new PayloadWriter<>(request,
                    PlatformDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_PLATFORM,
                    platformDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** PROJECT METHODS
    // *********************************************
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> createProject(@RequestBody PayloadEnvelope<ProjectDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ProjectDTO> payloadReader = new PayloadReader<>(ProjectDTO.class);
            ProjectDTO projectDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ProjectDTO projectDTONew = projectService.createProject(projectDTOToCreate);


            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request,
                    ProjectDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_PROJECTS,
                    projectDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/projects/{projectId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> replaceProject(@RequestBody PayloadEnvelope<ProjectDTO> payloadEnvelope,
                                                      @PathVariable Integer projectId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ProjectDTO> payloadReader = new PayloadReader<>(ProjectDTO.class);
            ProjectDTO projectDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ProjectDTO projectDTOReplaced = projectService.replaceProject(projectId, projectDTOToReplace);


            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request,
                    ProjectDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_PROJECTS,
                    projectDTOReplaced);
//

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> getProjects(HttpServletRequest request,
                                                   HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();
        try {

            //PayloadReader<ProjectDTO> payloadReader = new PayloadReader<>(ProjectDTO.class);
            List<ProjectDTO> projectDTOs = projectService.getProjects();

            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request,
                    ProjectDTO.class);

            payloadWriter.writeList(returnVal,
                    ServiceRequestId.URL_PROJECTS,
                    projectDTOs);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/projects/{projectId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ProjectDTO> getProjectsById(@PathVariable Integer projectId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ProjectDTO> returnVal = new PayloadEnvelope<>();
        try {

            ProjectDTO projectDTO = projectService.getProjectById(projectId);

            PayloadWriter<ProjectDTO> payloadWriter = new PayloadWriter<>(request,
                    ProjectDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_PROJECTS,
                    projectDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    // *********************************************
    // *************************** EXPERIMENT METHODS
    // *********************************************
    @RequestMapping(value = "/experiments", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> createExperiment(@RequestBody PayloadEnvelope<ExperimentDTO> payloadEnvelope,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();
        try {

            PayloadReader<ExperimentDTO> payloadReader = new PayloadReader<>(ExperimentDTO.class);
            ExperimentDTO exprimentDTOToCreate = payloadReader.extractSingleItem(payloadEnvelope);

            ExperimentDTO exprimentDTONew = experimentService.createExperiment(exprimentDTOToCreate);

            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request,
                    ExperimentDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_EXPERIMENTS,
                    exprimentDTONew);

        } catch (GobiiException e) {
            returnVal.getHeader().getStatus().addException(e);
        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> replaceExperiment(@RequestBody PayloadEnvelope<ExperimentDTO> payloadEnvelope,
                                                            @PathVariable Integer experimentId,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();

        try {

            PayloadReader<ExperimentDTO> payloadReader = new PayloadReader<>(ExperimentDTO.class);
            ExperimentDTO exprimentDTOToReplace = payloadReader.extractSingleItem(payloadEnvelope);

            ExperimentDTO exprimentDTOReplaced = experimentService.replaceExperiment(experimentId, exprimentDTOToReplace);


            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request,
                    ExperimentDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_EXPERIMENTS,
                    exprimentDTOReplaced);


        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


    @RequestMapping(value = "/experiments", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> getExperiments(HttpServletRequest request,
                                                         HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();
        try {

            List<ExperimentDTO> experimentDTOs = experimentService.getExperiments();


            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request,
                    ExperimentDTO.class);

            payloadWriter.writeList(returnVal,
                    ServiceRequestId.URL_EXPERIMENTS,
                    experimentDTOs);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }

    @RequestMapping(value = "/experiments/{experimentId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ExperimentDTO> getExperimentsById(@PathVariable Integer experimentId,
                                                             HttpServletRequest request,
                                                             HttpServletResponse response) {

        PayloadEnvelope<ExperimentDTO> returnVal = new PayloadEnvelope<>();
        try {

            ExperimentDTO experimentDTO = experimentService.getExperimentById(experimentId);

            returnVal.getPayload().getData().add(experimentDTO);

            PayloadWriter<ExperimentDTO> payloadWriter = new PayloadWriter<>(request,
                    ExperimentDTO.class);

            payloadWriter.writeSingleItemForDefaultId(returnVal,
                    ServiceRequestId.URL_EXPERIMENTS,
                    experimentDTO);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return (returnVal);

    }


}// BRAPIController
