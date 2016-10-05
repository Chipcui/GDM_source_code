// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import io.swagger.annotations.Api;
import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.ExperimentService;
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
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
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
@Api(value = "BRAPI", position = 1)
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
    private DisplayService displayService = null;

    @Autowired
    private CvService cvService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

    @Autowired
    private MapsetService mapsetService;

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

            payloadWriter.writeSingleItem(returnVal,
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

            payloadWriter.writeSingleItem(returnVal,
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

                payloadWriter.writeSingleItem(returnVal,
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

            payloadWriter.writeSingleItem(returnVal,
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

            payloadWriter.writeSingleItem(returnVal,
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

            payloadWriter.writeSingleItem(returnVal,
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

            payloadWriter.writeSingleItem(returnVal,
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

}// LoadController
