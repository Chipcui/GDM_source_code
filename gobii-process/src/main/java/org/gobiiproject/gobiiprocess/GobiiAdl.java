package org.gobiiproject.gobiiprocess;

import com.google.gson.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.Payload;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ContactDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ManifestDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProtocolDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.InstructionFileAccess;
import org.gobiiproject.gobiimodel.utils.InstructionFileValidator;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by VCalaminos on 2/21/2017.
 */
public class GobiiAdl {

    private static ServerConfig serverConfig;
    private static String crop = null;
    private static long timeoutInMillis;
    private static Long timeout = null;
    private static File xmlFile = null;
    private static boolean hasXmlFile = false;
    private static File parentDirectoryPath;
    private static boolean batchMode = true;

    private static String INPUT_HOST = "h";
    private static String INPUT_USER = "u";
    private static String INPUT_PASSWORD = "p";
    private static String INPUT_TIMEOUT = "t";
    private static String INPUT_SCENARIO = "s";
    private static String INPUT_DIRECTORY = "d";
    private static String NAME_COMMAND = "GobiiAdl";

    private static List<GobiiDataSetExtract> dataSetExtractReturnList = null;


    private static List<AnalysisDTO> getAnalyses() throws Exception {

        RestUri restUriAnalysis = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS);

        GobiiEnvelopeRestResource<AnalysisDTO,AnalysisDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriAnalysis);
        PayloadEnvelope<AnalysisDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(AnalysisDTO.class);

        checkStatus(resultEnvelope);

        return resultEnvelope.getPayload().getData();

    }

    private static List<DataSetDTO> getDatasets() throws Exception {

        RestUri restUriDataset = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriDataset);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(DataSetDTO.class);
        checkStatus(resultEnvelope);

        return resultEnvelope.getPayload().getData();

    }

    private static List<ExperimentDTO> getExperiments() throws Exception {

        RestUri restUriExperiment = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_EXPERIMENTS);
        GobiiEnvelopeRestResource<ExperimentDTO,ExperimentDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriExperiment);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ExperimentDTO.class);
        checkStatus(resultEnvelope);

        return resultEnvelope.getPayload().getData();

    }

    private static List<ProjectDTO> getProjects() throws Exception {

        RestUri restUriProject = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PROJECTS);
        GobiiEnvelopeRestResource<ProjectDTO,ProjectDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriProject);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ProjectDTO.class);

        checkStatus(resultEnvelope);

        return resultEnvelope.getPayload().getData();

    }

    private static List<MapsetDTO> getMapsets() throws Exception {

        RestUri restUriMapset = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MAPSET);
        GobiiEnvelopeRestResource<MapsetDTO,MapsetDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriMapset);
        PayloadEnvelope<MapsetDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(MapsetDTO.class);

        checkStatus(resultEnvelope);

        return resultEnvelope.getPayload().getData();

    }


    private static GobiiExtractFilterType getGobiiExtractFilterType(String extractType) {

        for (GobiiExtractFilterType extractFilterType : GobiiExtractFilterType.values()) {

            if (extractFilterType.toString().equals(extractType)) {

                return extractFilterType;
            }

        }

        processError("\n" + extractType + "is not a valid Gobii Extract type.", GobiiStatusLevel.ERROR);

        return null;

    }

    private static GobiiFileType getGobiiFileType(String fileType) {

        for (GobiiFileType currFileType : GobiiFileType.values()) {

            if (currFileType.toString().equals(fileType)) {

                return currFileType;
            }

        }

        processError("\n" + fileType + "is not a valid Gobii Extract File type.", GobiiStatusLevel.ERROR);

        return null;

    }

    private static void validateKeys(NodeList nodeList, XPath xPath, Document document) throws Exception {
        for (int i = 0; i < nodeList.getLength(); i++) {
            String parentName = nodeList.item(i).getLocalName();
            Element element = (Element) nodeList.item(i);
            String DbPKeysurrogate = element.getAttribute("DbPKeysurrogate");
            if (DbPKeysurrogate.isEmpty()) {
                processError("DbPKeysurrogate (" + DbPKeysurrogate + ") attribute for " +
                        element.getLocalName() + " entity cannot be empty.", GobiiStatusLevel.ERROR);
            }

            System.out.println("\nChecking DbPKeysurrogate (" + DbPKeysurrogate + ") for " + parentName + "......\n");
            Node node = nodeList.item(i);
            NodeList childNodes = node.getChildNodes();
            int count = 0;
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j) instanceof Element == false)
                    continue;
                System.out.println("\nRecord " + (count + 1) + "....\n");
                count++;
                String childName = childNodes.item(j).getLocalName();
                Element childElement = (Element) childNodes.item(j);

                Element props = (Element) childElement.getElementsByTagName("Properties").item(0);
                validateNode(props, childName, "Properties");
                Node dbPkeysurrogateNode = props.getElementsByTagName(DbPKeysurrogate).item(0);
                validateNode(dbPkeysurrogateNode, childName, "DbPKeysurrogate " + DbPKeysurrogate);

                String dbPkeysurrogateValue = dbPkeysurrogateNode.getTextContent();
                System.out.println("\nChecking for value....\n");

                if (dbPkeysurrogateValue.isEmpty()) {
                    processError("DbPKeysurrogate (" + DbPKeysurrogate + ") attribute for " +
                            childName + " entity cannot be empty.", GobiiStatusLevel.ERROR);
                }

                System.out.println("\nChecking for duplicates...\n");
                String expr = "count(//Entities/" + parentName + "/" + childName + "/Properties[" + DbPKeysurrogate + "='" + dbPkeysurrogateValue + "'])";

                XPathExpression xPathExpressionCount = xPath.compile(expr);
                Double countDuplicate = (Double) xPathExpressionCount.evaluate(document, XPathConstants.NUMBER);

                if (countDuplicate > 1) {
                    processError("Duplicate DbPKeysurrogate (" + DbPKeysurrogate + ") value (" + dbPkeysurrogateValue + ") " +
                            "for " + childName + " entity.", GobiiStatusLevel.ERROR);
                }

                System.out.println("\nChecking for foreign keys...\n");

                Element keys = (Element) childElement.getElementsByTagName("Keys").item(0);
                validateNode(keys, childName, "Keys");
                NodeList fkeys = keys.getElementsByTagName("Fkey");

                for (int k = 0; k < fkeys.getLength(); k++) {
                    Element fkey = (Element) fkeys.item(k);
                    String entity = fkey.getAttribute("entity");
                    if (entity == null || entity.isEmpty()) {
                        processError("Entity attribute for Fkey of " + childName + " (" + dbPkeysurrogateValue + ") cannot be empty.", GobiiStatusLevel.ERROR);
                    }

                    NodeList fkeyDbPkey = fkey.getElementsByTagName("DbPKeySurrogate");
                    if (fkeyDbPkey.getLength() < 1) {
                        processError("FKey property for " + childName + " (" + dbPkeysurrogateValue + ") should have <DbPKeySurrogate> tag.", GobiiStatusLevel.ERROR);
                    }

                    String fkeyDbPkeyValue = fkeyDbPkey.item(0).getTextContent();
                    if (fkeyDbPkeyValue == null || fkeyDbPkeyValue.isEmpty()) {
                        processError("DbPKeySurrogate property for " + entity + " FKey of " + childName + " (" + dbPkeysurrogateValue + ") cannot be empty.", GobiiStatusLevel.ERROR);
                    }

                    // get parent node of fkey entity
                    XPathExpression exprParentFkey = xPath.compile("//" + entity + "/parent::*");
                    Element ancestor = (Element) exprParentFkey.evaluate(document, XPathConstants.NODE);
                    validateNode(ancestor, childName, entity);
                    String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");
                    String exprCheckIfFKeyExists = "count(//Entities/" + ancestor.getNodeName() + "/" + entity + "/Properties[" + fkeyPKey + "='" + fkeyDbPkeyValue + "'])";

                    XPathExpression xPathExpressionCountFkey = xPath.compile(exprCheckIfFKeyExists);
                    Double countIfExists = (Double) xPathExpressionCountFkey.evaluate(document, XPathConstants.NUMBER);

                    if (countIfExists < 1) {
                        processError(entity + " (" + fkeyDbPkeyValue + ") fkey value for "
                                + childName + "(" + dbPkeysurrogateValue + ")" +
                                " doesn't exist in the file.", GobiiStatusLevel.ERROR);
                    }
                }
            }
        }
    }

    private static void validateNode(Node props, String element, String property) {
        if (props == null) {
            processError("Could not find " + property + " in element " + element, GobiiStatusLevel.ERROR);
        }
    }

    private static String processPropName(String propertyName) {
        char c[] = propertyName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        propertyName = new String(c);
        return propertyName;

    }

    private static <E> E processTypes(Object value, Class<E> type) throws ParseException {

        if (type.equals(Integer.class)) {
            return type.cast(Integer.parseInt(value.toString()));
        } else if (type.equals(Date.class)) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = formatter.parse(value.toString());
            return type.cast(date);
        }
        return type.cast(value);
    }


    private static void checkStatus(PayloadEnvelope payloadEnvelope) throws Exception {
        checkStatus(payloadEnvelope, false);
    } // checkStatus()

    private static void checkStatus(PayloadEnvelope payloadEnvelope, boolean verifyResultExists) throws Exception {

        Header header = payloadEnvelope.getHeader();
        if (!header.getStatus().isSucceeded() ||
                header
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(headerStatusMessage -> headerStatusMessage.getGobiiStatusLevel().equals(GobiiStatusLevel.VALIDATION))
                        .count() > 0) {

            System.out.println("\n***** Header errors: *****");
            String message = "";
            for (HeaderStatusMessage currentStatusMessage : header.getStatus().getStatusMessages()) {
                message = message + "\n" + currentStatusMessage.getMessage();
            }
            processError(message, GobiiStatusLevel.ERROR);
        } else {
            if (verifyResultExists) {
                if (payloadEnvelope.getPayload().getData().size() <= 0) {
                    processError("Request succeeded but there is no payload data", GobiiStatusLevel.ERROR);
                }
            }
        }
    }

    private static Map<String, Integer> getCvTermsWithId(String filterValue) throws Exception {

        Map<String, Integer> returnVal = new HashMap<>();

        RestUri namesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();
        GobiiEnvelopeRestResource<NameIdDTO,NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
        namesUri.setParamValue("entity", GobiiEntityNameType.CV.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_TYPE_NAME.toString().toUpperCase()));
        namesUri.setParamValue("filterValue", filterValue);

        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResource.get(NameIdDTO.class);
        List<NameIdDTO> nameIdDTOList = resultEnvelope.getPayload().getData();

        for (NameIdDTO currentNameIdDTO : nameIdDTOList) {
            returnVal.put(currentNameIdDTO.getName().toLowerCase(), currentNameIdDTO.getId());
        }
        return returnVal;
    }

    private static Integer createOrganization(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                              XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        RestUri restUriOrganization = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION);
        GobiiEnvelopeRestResource<OrganizationDTO,OrganizationDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriOrganization);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(OrganizationDTO.class);

        checkStatus(resultEnvelope);

        List<OrganizationDTO> organizationDTOSList = resultEnvelope.getPayload().getData();
        for (OrganizationDTO currentOrganizationDTO : organizationDTOSList) {
            if (currentOrganizationDTO.getName().equals(dbPkeysurrogateValue)) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForExistingEntity(fkeys, OrganizationDTO.class, currentOrganizationDTO);
                return currentOrganizationDTO.getId();
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        OrganizationDTO newOrganizationDTO = new OrganizationDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");

        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            Field field = OrganizationDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newOrganizationDTO, processTypes(propKey.getTextContent(), field.getType()));
        }

        newOrganizationDTO.setCreatedBy(1);
        newOrganizationDTO.setModifiedBy(1);
        newOrganizationDTO.setStatusId(1);
        setFKeyDbPKeyForNewEntity(fkeys, OrganizationDTO.class, newOrganizationDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create organization */
        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<OrganizationDTO,OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = gobiiEnvelopeRestResource.post(OrganizationDTO.class,
                payloadEnvelope);

        checkStatus(organizationDTOResponseEnvelope);
        OrganizationDTO organizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return organizationDTOResponse.getOrganizationId();
    }

    private static PayloadEnvelope<ContactDTO> getContactByEmail(String email) throws Exception{

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .contactsByQueryParams();
        restUriContact.setParamValue("email", email);
        GobiiEnvelopeRestResource<ContactDTO,ContactDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ContactDTO.class);

        checkStatus(resultEnvelope);
        return resultEnvelope;

    }

    private static Integer createContact(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                         XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        PayloadEnvelope<ContactDTO> resultEnvelope = getContactByEmail(dbPkeysurrogateValue);

        if (resultEnvelope.getPayload().getData().size() > 0) {
            ContactDTO currentContactDTO = resultEnvelope.getPayload().getData().get(0);
            System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

            /* set fkey dbpkey for entity */
            setFKeyDbPKeyForExistingEntity(fkeys, ContactDTO.class, currentContactDTO);
            return currentContactDTO.getId();
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        ContactDTO newContactDTO = new ContactDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");

        // get roles
        RestUri rolesUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .nameIdListByQueryParams();

        GobiiEnvelopeRestResource<NameIdDTO,NameIdDTO> gobiiEnvelopeNameResource = new GobiiEnvelopeRestResource<>(rolesUri);
        rolesUri.setParamValue("entity", GobiiEntityNameType.ROLE.toString().toLowerCase());
        PayloadEnvelope<NameIdDTO> resultEnvelopeRoles = gobiiEnvelopeNameResource.get(NameIdDTO.class);
        List<NameIdDTO> nameIdDTOList = resultEnvelopeRoles.getPayload().getData();
        Map<String, Integer> rolesMap = new HashMap<>();

        for (NameIdDTO currentNameIdDTO : nameIdDTOList) {
            rolesMap.put(currentNameIdDTO.getName(), currentNameIdDTO.getId());
        }

        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();

            switch (propKeyLocalName) {
                case "Roles":
                    break;
                case "Role":
                    Integer roleId = rolesMap.get(propKey.getTextContent());
                    newContactDTO.getRoles().add(roleId);
                    break;
                default:
                    propKeyLocalName = processPropName(propKeyLocalName);
                    Field field = ContactDTO.class.getDeclaredField(propKeyLocalName);
                    field.setAccessible(true);
                    field.set(newContactDTO, processTypes(propKey.getTextContent(), field.getType()));
                    break;
            }
        }
        newContactDTO.setCreatedBy(1);
        newContactDTO.setModifiedBy(1);

        /* check roles */
        if (newContactDTO.getRoles().size() <= 0) {
            processError("Roles are required to create a contact. Please add role/s for Contact (" + newContactDTO.getEmail() + ").", GobiiStatusLevel.ERROR);
        }

        setFKeyDbPKeyForNewEntity(fkeys, ContactDTO.class, newContactDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create contact */
        PayloadEnvelope<ContactDTO> payloadEnvelopeContact = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ContactDTO,ContactDTO> gobiiEnvelopeRestResourceContact = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONTACTS));
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResourceContact.post(ContactDTO.class,
                payloadEnvelopeContact);
        checkStatus(contactDTOResponseEnvelope);
        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return contactDTOResponse.getContactId();
    }

    private static Integer createPlatform(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {
        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        RestUri restUriPlatform = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM);
        GobiiEnvelopeRestResource<PlatformDTO,PlatformDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriPlatform);
        PayloadEnvelope<PlatformDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(PlatformDTO.class);
        checkStatus(resultEnvelope);
        List<PlatformDTO> platformDTOSList = resultEnvelope.getPayload().getData();
        for (PlatformDTO currentPlatformDTO : platformDTOSList) {
            if (currentPlatformDTO.getPlatformName().equals(dbPkeysurrogateValue)) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForExistingEntity(fkeys, PlatformDTO.class, currentPlatformDTO);
                return currentPlatformDTO.getId();
            }
        }
        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        PlatformDTO newPlatformDTO = new PlatformDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        Element propertiesElement = null;

        /* get cv's from platform_type group */
        Map<String, Integer> platformTypeMap = getCvTermsWithId("platform_type");
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            if (propKeyLocalName.equals("properties")) {
                propertiesElement = propKey;
                continue;
            } else if (propKeyLocalName.equals("typeId")) {
                Integer typeId = platformTypeMap.get(propKey.getTextContent().toLowerCase());
                newPlatformDTO.setTypeId(typeId);
                continue;
            }
            if (propKey.getParentNode().equals(propertiesElement)) { // add to properties attribute of platform
                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();
                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());
                newPlatformDTO.getProperties().add(entityPropertyDTO);
            } else {
                Field field = PlatformDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newPlatformDTO, processTypes(propKey.getTextContent(), field.getType()));
            }
        }

        newPlatformDTO.setCreatedBy(1);
        newPlatformDTO.setModifiedBy(1);
        setFKeyDbPKeyForNewEntity(fkeys, PlatformDTO.class, newPlatformDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create platform */
        PayloadEnvelope<PlatformDTO> payloadEnvelopePlatform = new PayloadEnvelope<>(newPlatformDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<PlatformDTO,PlatformDTO> gobiiEnvelopeRestResourcePlatform = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM));
        PayloadEnvelope<PlatformDTO> platformDTOResponseEnvelope = gobiiEnvelopeRestResourcePlatform.post(PlatformDTO.class,
                payloadEnvelopePlatform);
        checkStatus(platformDTOResponseEnvelope);
        PlatformDTO platformDTOResponse = platformDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return platformDTOResponse.getPlatformId();
    }

    private static Integer createProtocol(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        RestUri restUriProtocol = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PROTOCOL);
        GobiiEnvelopeRestResource<ProtocolDTO,ProtocolDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriProtocol);
        PayloadEnvelope<ProtocolDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ProtocolDTO.class);
        checkStatus(resultEnvelope);
        List<ProtocolDTO> protocolDTOSList = resultEnvelope.getPayload().getData();

        // get platform Id from XML

        XPathExpression exprParentKey = xPath.compile("//" + "Platform" + "/parent::*");
        Element ancestor = (Element) exprParentKey.evaluate(document, XPathConstants.NODE);

        String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");
        Integer platformId = null;

        if (fkeyPKey.isEmpty()) {
            platformId =null;
        } else {

            Element currentFkeyElement = null;

            if (fkeys != null && fkeys.getLength() > 0) {

                for (int fkeysI = 0; fkeysI < fkeys.getLength(); fkeysI++) {

                    currentFkeyElement = (Element) fkeys.item(fkeysI);
                    String currentEntity = currentFkeyElement.getAttribute("entity");

                    if (currentEntity.equals("Platform")) {
                        break;
                    }
                }
            }

            if (currentFkeyElement == null) {
                platformId = null;
            } else {
                String currentPlatformfKeyDbPkeyValue = currentFkeyElement.getElementsByTagName("DbPKeySurrogate").item(0).getTextContent();
                String exprCheckIfKeyExists = "//Entities/" + ancestor.getNodeName() + "/" + "Platform" + "/Properties[" + fkeyPKey + "='" + currentPlatformfKeyDbPkeyValue + "']";
                XPathExpression xPathExprNodeFKey = xPath.compile(exprCheckIfKeyExists);
                Element nodeFKey = (Element) xPathExprNodeFKey.evaluate(document, XPathConstants.NODE);
                Element parentNode = (Element) nodeFKey.getParentNode();
                Element dbPkeyNode = ((Element) parentNode.getElementsByTagName("Keys").item(0));
                validateNode(dbPkeyNode, parentElement.getTagName(), "Keys");

                String platformDbPkeyValue = dbPkeyNode.getElementsByTagName("DbPKey").item(0).getTextContent();

                if (!platformDbPkeyValue.isEmpty()) {
                    platformId = Integer.parseInt(platformDbPkeyValue);
                }


            }

        }

        for (ProtocolDTO currentProtocolDTO : protocolDTOSList) {
            if (currentProtocolDTO.getName().equals(dbPkeysurrogateValue)) {

                if (platformId != null && currentProtocolDTO.getPlatformId().equals(platformId)) {
                    System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                    /* set fkey dbpkey for entity */
                    setFKeyDbPKeyForExistingEntity(fkeys, ProtocolDTO.class, currentProtocolDTO);
                    return currentProtocolDTO.getId();
                }
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        ProtocolDTO newProtocolDTO = new ProtocolDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        Element propsElement = null;
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            if (propKeyLocalName.equals("vendorProtocols")) {
                continue;
            }
            if (propKeyLocalName.equals("props")) {
                propsElement = propKey;
                continue;
            }
            if (propKey.getParentNode().equals(propsElement)) {
                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();
                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());
                newProtocolDTO.getProps().add(entityPropertyDTO);
            } else {
                Field field = ProtocolDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newProtocolDTO, processTypes(propKey.getTextContent(), field.getType()));
            }
        }

        newProtocolDTO.setCreatedBy(1);
        newProtocolDTO.setModifiedBy(1);

        setFKeyDbPKeyForNewEntity(fkeys, ProtocolDTO.class, newProtocolDTO, parentElement, dbPkeysurrogateValue, document, xPath);

        System.out.println("Calling the web service...\n");

        /* create protocol */
        PayloadEnvelope<ProtocolDTO> payloadEnvelopeProtocol = new PayloadEnvelope<>(newProtocolDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ProtocolDTO,ProtocolDTO> gobiiEnvelopeRestResourceProtocol = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PROTOCOL));
        PayloadEnvelope<ProtocolDTO> protocolDTOResponseEnvelope = gobiiEnvelopeRestResourceProtocol.post(ProtocolDTO.class,
                payloadEnvelopeProtocol);

        checkStatus(protocolDTOResponseEnvelope);

        ProtocolDTO protocolDTOResponse = protocolDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return protocolDTOResponse.getProtocolId();
    }

    private static Integer createVendorProtocol(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                                XPath xPath, Document document, NodeList propKeyList) throws Exception {

        VendorProtocolDTO newVendorProtocolDTO = new VendorProtocolDTO();
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            Field field = VendorProtocolDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newVendorProtocolDTO, processTypes(propKey.getTextContent(), field.getType()));
        }

        setFKeyDbPKeyForNewEntity(fkeys, VendorProtocolDTO.class, newVendorProtocolDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", newVendorProtocolDTO.getOrganizationId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO,OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById.get(OrganizationDTO.class);

        checkStatus(resultEnvelopeForGetById);
        OrganizationDTO currentOrganizationDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        for (VendorProtocolDTO vendorProtocolDTO : currentOrganizationDTO.getVendorProtocols()) {
            if (vendorProtocolDTO.getProtocolId().equals(newVendorProtocolDTO.getProtocolId())) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");
                return vendorProtocolDTO.getId();
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");

        System.out.println("Calling the web service...\n");

        /* create vendor protcol */
        // get organization/vendor
        RestUri restUriForGetOrganizationById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriForGetOrganizationById.setParamValue("id", newVendorProtocolDTO.getOrganizationId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO,OrganizationDTO> gobiiEnvelopeRestResourceForGetOrganizationById =
                new GobiiEnvelopeRestResource<>(restUriForGetOrganizationById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetOrganizationByID = gobiiEnvelopeRestResourceForGetOrganizationById
                .get(OrganizationDTO.class);
        OrganizationDTO organizationDTO = resultEnvelopeForGetOrganizationByID.getPayload().getData().get(0);
        organizationDTO.getVendorProtocols().add(newVendorProtocolDTO);

        RestUri restUriProtocoLVendor = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .childResourceByUriIdParam(GobiiServiceRequestId.URL_PROTOCOL,
                        GobiiServiceRequestId.URL_VENDORS);
        restUriProtocoLVendor.setParamValue("id", newVendorProtocolDTO.getProtocolId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO,OrganizationDTO> protocolVendorResource =
                new GobiiEnvelopeRestResource<>(restUriProtocoLVendor);
        PayloadEnvelope<OrganizationDTO> vendorPayloadEnvelope =
                new PayloadEnvelope<>(organizationDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<OrganizationDTO> protocolVendorResult =
                protocolVendorResource.post(OrganizationDTO.class, vendorPayloadEnvelope);

        checkStatus(protocolVendorResult);
        OrganizationDTO vendorResult = protocolVendorResult.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        for (VendorProtocolDTO vendorProtocolDTO : vendorResult.getVendorProtocols()) {
            if (vendorProtocolDTO.getName().equals(newVendorProtocolDTO.getName())) {
                return vendorProtocolDTO.getId();
            }
        }
        return null;
    }

    private static Integer createReference(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                           XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        RestUri restUriReference = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_REFERENCE);
        GobiiEnvelopeRestResource<ReferenceDTO,ReferenceDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriReference);
        PayloadEnvelope<ReferenceDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ReferenceDTO.class);
        checkStatus(resultEnvelope);
        List<ReferenceDTO> referenceDTOSList = resultEnvelope.getPayload().getData();
        for (ReferenceDTO currentReferenceDTO : referenceDTOSList) {
            if (currentReferenceDTO.getName().equals(dbPkeysurrogateValue)) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForExistingEntity(fkeys, ReferenceDTO.class, currentReferenceDTO);
                return currentReferenceDTO.getId();
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        ReferenceDTO newReferenceDTO = new ReferenceDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            Field field = ReferenceDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newReferenceDTO, processTypes(propKey.getTextContent(), field.getType()));
        }

        newReferenceDTO.setCreatedBy(1);
        newReferenceDTO.setModifiedBy(1);
        setFKeyDbPKeyForNewEntity(fkeys, ReferenceDTO.class, newReferenceDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create reference */
        PayloadEnvelope<ReferenceDTO> payloadEnvelopeReference = new PayloadEnvelope<>(newReferenceDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO,ReferenceDTO> gobiiEnvelopeRestResourceReference = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTOResponseEnvelope = gobiiEnvelopeRestResourceReference.post(ReferenceDTO.class,
                payloadEnvelopeReference);

        checkStatus(referenceDTOResponseEnvelope);
        ReferenceDTO referenceDTOResponse = referenceDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return referenceDTOResponse.getReferenceId();
    }

    private static Integer createMapset(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                        XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        List<MapsetDTO> mapsetDTOSList = getMapsets();
        for (MapsetDTO currentMapsetDTO : mapsetDTOSList) {
            if (currentMapsetDTO.getName().equals(dbPkeysurrogateValue)) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForExistingEntity(fkeys, MapsetDTO.class, currentMapsetDTO);
                return currentMapsetDTO.getId();
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        MapsetDTO newMapsetDTO = new MapsetDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        Element propertiesElement = null;

        /* get cv's from mapset_type group */
        Map<String, Integer> mapsetTypeMap = getCvTermsWithId("mapset_type");
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            if (propKeyLocalName.equals("properties")) {
                propertiesElement = propKey;
                continue;
            }

            if (propKeyLocalName.equals("mapType")) {
                Integer typeId = mapsetTypeMap.get(propKey.getTextContent().toLowerCase());
                newMapsetDTO.setMapType(typeId);
                continue;
            }

            if (propKey.getParentNode().equals(propertiesElement)) {
                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();
                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());
                newMapsetDTO.getProperties().add(entityPropertyDTO);
            } else {
                Field field = MapsetDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newMapsetDTO, processTypes(propKey.getTextContent(), field.getType()));
            }
        }
        newMapsetDTO.setCreatedBy(1);
        newMapsetDTO.setModifiedBy(1);
        setFKeyDbPKeyForNewEntity(fkeys, MapsetDTO.class, newMapsetDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create mapset */
        PayloadEnvelope<MapsetDTO> payloadEnvelopeMapset = new PayloadEnvelope<>(newMapsetDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MapsetDTO,MapsetDTO> gobiiEnvelopeRestResourceMapset = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MAPSET));
        PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelope = gobiiEnvelopeRestResourceMapset.post(MapsetDTO.class,
                payloadEnvelopeMapset);

        checkStatus(mapsetDTOResponseEnvelope);
        MapsetDTO mapsetDTOResponse = mapsetDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return mapsetDTOResponse.getMapsetId();
    }

    private static Integer createProject(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                         XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        List<ProjectDTO> projectDTOSList = getProjects();
        ProjectDTO newProjectDTO = new ProjectDTO();
        for (ProjectDTO currentProjectDTO : projectDTOSList) {
            if (currentProjectDTO.getProjectName().equals(dbPkeysurrogateValue)) {

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForNewEntity(fkeys, ProjectDTO.class, newProjectDTO, parentElement, dbPkeysurrogateValue, document, xPath);

                // get contactID
                String newExpr = "//Project/Keys/Fkey[@entity='Contact']/DbPKey";
                XPathExpression xPathExpression = xPath.compile(newExpr);
                Element contactEntity = (Element) xPathExpression.evaluate(document, XPathConstants.NODE);

                if (!contactEntity.getTextContent().isEmpty()) {
                    Integer contactEntityId = Integer.parseInt(contactEntity.getTextContent());
                    if (contactEntityId.equals(currentProjectDTO.getPiContact())) {
                        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");
                        return currentProjectDTO.getId();
                    }
                } else {
                    return currentProjectDTO.getId();
                }
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        Element propertiesElement = null;
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            if (propKeyLocalName.equals("cvProps") || propKeyLocalName.equals("cvTerm") || propKeyLocalName.equals("cvPropValue")) {
                continue;
            }

            if (propKeyLocalName.equals("cvProp")) {

                NodeList cvTermElement = propKey.getElementsByTagName("CvTerm");
                NodeList cvValueElement = propKey.getElementsByTagName("CvPropValue");

                if (cvTermElement.getLength() != 0 && cvValueElement.getLength() != 0) {

                    String cvTerm = cvTermElement.item(0).getTextContent();
                    String cvValue = cvValueElement.item(0).getTextContent();

                    newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, cvTerm, cvValue));

                }

                continue;
            }

            Field field = ProjectDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newProjectDTO, processTypes(propKey.getTextContent(), field.getType()));

        }
        newProjectDTO.setCreatedBy(1);
        newProjectDTO.setModifiedBy(1);
        setFKeyDbPKeyForNewEntity(fkeys, ProjectDTO.class, newProjectDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create project */
        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS);
        GobiiEnvelopeRestResource<ProjectDTO,ProjectDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(newProjectDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<ProjectDTO> projectDTOResponseEnvelope = gobiiEnvelopeRestResourceForProjects.post(ProjectDTO.class, payloadEnvelope);

        checkStatus(projectDTOResponseEnvelope);
        ProjectDTO projectDTOResponse = projectDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return projectDTOResponse.getProjectId();
    }

    private static Integer createManifest(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {
        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        RestUri restUriManifest = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST);
        GobiiEnvelopeRestResource<ManifestDTO,ManifestDTO> gobiiEnvelopeRestResourceGet = new GobiiEnvelopeRestResource<>(restUriManifest);
        PayloadEnvelope<ManifestDTO> resultEnvelope = gobiiEnvelopeRestResourceGet.get(ManifestDTO.class);
        checkStatus(resultEnvelope);
        List<ManifestDTO> manifestDTOSList = resultEnvelope.getPayload().getData();
        for (ManifestDTO currentManifestDTO : manifestDTOSList) {
            if (currentManifestDTO.getName().equals(dbPkeysurrogateValue)) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForExistingEntity(fkeys, ManifestDTO.class, currentManifestDTO);
                return currentManifestDTO.getId();
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        ManifestDTO newManifestDTO = new ManifestDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");

        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            Field field = ManifestDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newManifestDTO, processTypes(propKey.getTextContent(), field.getType()));
        }
        newManifestDTO.setCreatedBy(1);
        newManifestDTO.setModifiedBy(1);

        setFKeyDbPKeyForNewEntity(fkeys, ManifestDTO.class, newManifestDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create manifest */
        PayloadEnvelope<ManifestDTO> payloadEnvelopeManifest = new PayloadEnvelope<>(newManifestDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ManifestDTO,ManifestDTO> gobiiEnvelopeRestResourceManifest = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST));
        PayloadEnvelope<ManifestDTO> manifestDTOResponseEnvelope = gobiiEnvelopeRestResourceManifest.post(ManifestDTO.class,
                payloadEnvelopeManifest);

        checkStatus(manifestDTOResponseEnvelope);
        ManifestDTO manifestDTOResponse = manifestDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return manifestDTOResponse.getManifestId();
    }

    private static Integer createExperiment(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                            XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */
        List<ExperimentDTO> experimentDTOSList = getExperiments();

        // get project Id from XML

        XPathExpression exprParentKey = xPath.compile("//" + "Project" + "/parent::*");
        Element ancestor = (Element) exprParentKey.evaluate(document, XPathConstants.NODE);

        String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");
        Integer projectId = null;
        if (fkeyPKey.isEmpty()) {
            projectId = null;
        } else {

            Element currentFkeyElement = null;

            if (fkeys != null && fkeys.getLength() > 0) {

                for (int fkeysI = 0; fkeysI < fkeys.getLength(); fkeysI++) {

                    currentFkeyElement = (Element) fkeys.item(fkeysI);
                    String currentEntity = currentFkeyElement.getAttribute("entity");

                    if (currentEntity.equals("Project")) {
                        break;
                    }
                }

            }

            if (currentFkeyElement == null) {
                projectId = null;
            } else {
                String currentProjfKeyDbPkeyValue = currentFkeyElement.getElementsByTagName("DbPKeySurrogate").item(0).getTextContent();
                String exprCheckIfFKeyExists = "//Entities/" + ancestor.getNodeName() + "/" + "Project" + "/Properties[" + fkeyPKey + "='" + currentProjfKeyDbPkeyValue + "']";
                XPathExpression xPathExprNodeFKey = xPath.compile(exprCheckIfFKeyExists);
                Element nodeFKey = (Element) xPathExprNodeFKey.evaluate(document, XPathConstants.NODE);
                Element parentNode = (Element) nodeFKey.getParentNode();
                Element dbPkeyNode = ((Element) parentNode.getElementsByTagName("Keys").item(0));
                validateNode(dbPkeyNode, parentElement.getTagName(), "Keys");

                String projDbPkeyValue = dbPkeyNode.getElementsByTagName("DbPKey").item(0).getTextContent();

                if (!projDbPkeyValue.isEmpty()) {
                    projectId = Integer.parseInt(projDbPkeyValue);
                }
            }
        }

        for (ExperimentDTO currentExperimentDTO : experimentDTOSList) {
            if (currentExperimentDTO.getExperimentName().equals(dbPkeysurrogateValue)) {

                if (projectId != null && currentExperimentDTO.getProjectId().equals(projectId)) {

                    System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                    /* set fkey dbpkey for entity */
                    setFKeyDbPKeyForExistingEntity(fkeys, ExperimentDTO.class, currentExperimentDTO);
                    return currentExperimentDTO.getId();

                }

            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        ExperimentDTO newExperimentDTO = new ExperimentDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            Field field = ExperimentDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newExperimentDTO, processTypes(propKey.getTextContent(), field.getType()));
        }
        newExperimentDTO.setCreatedBy(1);
        newExperimentDTO.setModifiedBy(1);

        setFKeyDbPKeyForNewEntity(fkeys, ExperimentDTO.class, newExperimentDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create experiment */
        PayloadEnvelope<ExperimentDTO> payloadEnvelopeExperiment = new PayloadEnvelope<>(newExperimentDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExperimentDTO,ExperimentDTO> gobiiEnvelopeRestResourceExperiment = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_EXPERIMENTS));
        PayloadEnvelope<ExperimentDTO> experimentDTOResponseEnvelope = gobiiEnvelopeRestResourceExperiment.post(ExperimentDTO.class,
                payloadEnvelopeExperiment);
        checkStatus(experimentDTOResponseEnvelope);
        ExperimentDTO experimentDTOResponse = experimentDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return experimentDTOResponse.getExperimentId();
    }

    private static Integer createAnalysis(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                          XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        List<AnalysisDTO> analysisDTOSList = getAnalyses();
        for (AnalysisDTO currentAnalysisDTO : analysisDTOSList) {
            if (currentAnalysisDTO.getAnalysisName().equals(dbPkeysurrogateValue)) {
                System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                /* set fkey dbpkey for entity */
                setFKeyDbPKeyForExistingEntity(fkeys, AnalysisDTO.class, currentAnalysisDTO);
                return currentAnalysisDTO.getId();
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        AnalysisDTO newAnalysisDTO = new AnalysisDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");
        Element paramElement = null;

        /* get cv's from analysis_type group */
        Map<String, Integer> analysisTypeMap = getCvTermsWithId("analysis_type");
        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            if (propKeyLocalName.equals("parameters")) {
                paramElement = propKey;
                continue;
            }
            if (propKeyLocalName.equals("anlaysisTypeId")) {
                Integer typeId = analysisTypeMap.get(propKey.getTextContent().toLowerCase());
                newAnalysisDTO.setAnlaysisTypeId(typeId);
                continue;
            }
            if (propKey.getParentNode().equals(paramElement)) {
                EntityPropertyDTO entityPropertyDTO = new EntityPropertyDTO();
                entityPropertyDTO.setPropertyName(propKeyLocalName);
                entityPropertyDTO.setPropertyValue(propKey.getTextContent());
                newAnalysisDTO.getParameters().add(entityPropertyDTO);
            } else {
                Field field = AnalysisDTO.class.getDeclaredField(propKeyLocalName);
                field.setAccessible(true);
                field.set(newAnalysisDTO, processTypes(propKey.getTextContent(), field.getType()));
            }
        }

        newAnalysisDTO.setCreatedBy(1);
        newAnalysisDTO.setModifiedBy(1);
        setFKeyDbPKeyForNewEntity(fkeys, AnalysisDTO.class, newAnalysisDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create analysis */
        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(newAnalysisDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO,AnalysisDTO> gobiiEnvelopeRestResourceAnalysis = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResourceAnalysis.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);
        checkStatus(analysisDTOResponseEnvelope);
        AnalysisDTO analysisDTOResponse = analysisDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return analysisDTOResponse.getAnalysisId();
    }

    private static Integer createDataset(Element parentElement, String entityName, NodeList fkeys, String dbPkeysurrogateValue,
                                         XPath xPath, Document document, NodeList propKeyList) throws Exception {

        System.out.println("\nChecking if " + entityName + " (" + dbPkeysurrogateValue + ") already exists in the database...\n");

        /* check if entity already exist in the database */

        List<DataSetDTO> datasetDTOSList = getDatasets();

        // get experiment ID from XML

        XPathExpression exprParentKey = xPath.compile("//" + "Experiment" + "/parent::*");
        Element ancestor = (Element) exprParentKey.evaluate(document, XPathConstants.NODE);

        String fkeyPkey = ancestor.getAttribute("DbPKeysurrogate");
        Integer experimentId = null;

        if (fkeyPkey.isEmpty()) {
            experimentId = null;
        } else {

            Element currentFkeyElement = null;

            if (fkeys != null && fkeys.getLength() > 0) {

                for (int fkeysI = 0; fkeysI < fkeys.getLength(); fkeysI++) {

                    currentFkeyElement = (Element) fkeys.item(fkeysI);
                    String currentEntity = currentFkeyElement.getAttribute("entity");

                    if (currentEntity.equals("Experiment")) {
                        break;
                    }

                }

            }

            if (currentFkeyElement == null) {
                experimentId = null;
            } else {
                String currentExpfKeyDbPkeyValue = currentFkeyElement.getElementsByTagName("DbPKeySurrogate").item(0).getTextContent();
                String exprCheckIfFKeyExists = "//Entities/" + ancestor.getNodeName() + "/" + "Experiment" + "/Properties[" + fkeyPkey + "='" + currentExpfKeyDbPkeyValue + "']";
                XPathExpression xPathExprNodeFKey = xPath.compile(exprCheckIfFKeyExists);
                Element nodeFKey = (Element) xPathExprNodeFKey.evaluate(document, XPathConstants.NODE);
                Element parentNode = (Element) nodeFKey.getParentNode();
                Element dbPkeyNode = ((Element) parentNode.getElementsByTagName("Keys").item(0));
                validateNode(dbPkeyNode, parentElement.getTagName(), "Keys");

                String exprDbPkeyValue = dbPkeyNode.getElementsByTagName("DbPKey").item(0).getTextContent();

                if (!exprDbPkeyValue.isEmpty()) {
                    experimentId = Integer.parseInt(exprDbPkeyValue);
                }
            }

        }

        for (DataSetDTO currentDatasetDTO : datasetDTOSList) {
            if (currentDatasetDTO.getDatasetName().equals(dbPkeysurrogateValue)) {

                if (experimentId != null && currentDatasetDTO.getExperimentId().equals(experimentId)) {

                    System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") already exists in the database. Return current entity ID.\n");

                    /* set fkey dbpkey for entity */
                    setFKeyDbPKeyForExistingEntity(fkeys, DataSetDTO.class, currentDatasetDTO);
                    return currentDatasetDTO.getId();
                }
            }
        }

        System.out.println("\n" + entityName + "(" + dbPkeysurrogateValue + ") doesn't exist in the database.\nCreating new record...\n");
        DataSetDTO newDataSetDTO = new DataSetDTO();
        System.out.println("Populating " + entityName + "DTO with attributes from XML file...");

        /* get cv's from dataset_type group */
        Map<String, Integer> datasetTypeMap = getCvTermsWithId("dataset_type");
        List<AnalysisDTO> analysisDTOList = getAnalyses();

        for (int j = 0; j < propKeyList.getLength(); j++) {
            Element propKey = (Element) propKeyList.item(j);
            String propKeyLocalName = propKey.getLocalName();
            propKeyLocalName = processPropName(propKeyLocalName);
            if (propKeyLocalName.equals("analysesIds")) {
                continue;
            }
            if (propKeyLocalName.equals("analysisId")) {

                // get analysis by name

                String analysisName = propKey.getTextContent();
                Integer currentAnalysisId = null;

                for (AnalysisDTO currentAnalysisDTO : analysisDTOList) {

                    if (currentAnalysisDTO.getAnalysisName().equals(analysisName)) {
                        currentAnalysisId = currentAnalysisDTO.getId();
                        break;
                    }
                }

                if (currentAnalysisId != null) {
                    newDataSetDTO.getAnalysesIds().add(currentAnalysisId);
                } else {
                    processError("Failed to add analysis: " + analysisName + " to dataset: " +dbPkeysurrogateValue+ ". Analysis not found.", GobiiStatusLevel.WARNING);
                }

                continue;
            }
            if (propKeyLocalName.equals("typeId")) {
                Integer typeId = datasetTypeMap.get(propKey.getTextContent().toLowerCase());
                newDataSetDTO.setDatatypeId(typeId);
                continue;
            }
            if (propKeyLocalName.equals("scores")) {
                continue;
            }
            if (propKeyLocalName.equals("score")) {
                newDataSetDTO.getScores().add(Integer.parseInt(propKey.getTextContent()));
                continue;
            }

            Field field = DataSetDTO.class.getDeclaredField(propKeyLocalName);
            field.setAccessible(true);
            field.set(newDataSetDTO, processTypes(propKey.getTextContent(), field.getType()));
        }

        newDataSetDTO.setCreatedBy(1);
        newDataSetDTO.setModifiedBy(1);
        setFKeyDbPKeyForNewEntity(fkeys, DataSetDTO.class, newDataSetDTO, parentElement, dbPkeysurrogateValue, document, xPath);
        System.out.println("Calling the web service...\n");

        /* create dataset */
        PayloadEnvelope<DataSetDTO> payloadEnvelopeDataSet = new PayloadEnvelope<>(newDataSetDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceDataSet = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS));
        PayloadEnvelope<DataSetDTO> dataSetDTOResponseEnvelope = gobiiEnvelopeRestResourceDataSet.post(DataSetDTO.class,
                payloadEnvelopeDataSet);

        checkStatus(dataSetDTOResponseEnvelope);
        DataSetDTO dataSetDTOResponse = dataSetDTOResponseEnvelope.getPayload().getData().get(0);
        System.out.println(entityName + "(" + dbPkeysurrogateValue + ") is successfully created!\n");
        return dataSetDTOResponse.getDataSetId();
    }

    private static Integer createEntity(Element parentElement, String entityName, NodeList fKeys, String dbPkeysurrogateValue, XPath xPath, Document document) throws Exception {

        Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);
        validateNode(props, parentElement.getTagName(), "Properties");
        NodeList propKeyList = props.getElementsByTagName("*");
        if (propKeyList.getLength() < 1) {
            processError("Properties for " + parentElement.getLocalName() + " entity cannot be empty.", GobiiStatusLevel.ERROR);
        }
        Integer returnVal = null;
        switch (entityName) {

            case "Organization":
                returnVal = createOrganization(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Contact":
                returnVal = createContact(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Platform":
                returnVal = createPlatform(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Protocol":
                returnVal = createProtocol(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "VendorProtocol":
                returnVal = createVendorProtocol(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Reference":
                returnVal = createReference(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Mapset":
                returnVal = createMapset(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Project":
                returnVal = createProject(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Manifest":
                returnVal = createManifest(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Experiment":
                returnVal = createExperiment(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Analysis":
                returnVal = createAnalysis(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            case "Dataset":
                returnVal = createDataset(parentElement, entityName, fKeys, dbPkeysurrogateValue, xPath, document, propKeyList);
                break;
            default:
                break;
        }
        return returnVal;
    }

    private static void setFKeyDbPKeyForNewEntity(NodeList fkeys, Class currentClass, Object currentDTO, Element parentElement, String dbPkeysurrogateValue,
                                                  Document document, XPath xPath) throws Exception {

        if (fkeys != null && fkeys.getLength() > 0) {
            for (int i = 0; i < fkeys.getLength(); i++) {
                Element currentFkeyElement = (Element) fkeys.item(i);
                String fkproperty = currentFkeyElement.getAttribute("fkproperty");
                if (fkproperty.isEmpty()) {
                    processError("FkProperty attribute for "
                            + currentFkeyElement.getParentNode().getParentNode().getLocalName() + " does not exist.", GobiiStatusLevel.ERROR);
                }
                Integer fKeyDbPkey = getFKeyDbPKey(currentFkeyElement, parentElement, dbPkeysurrogateValue, document, xPath);
                Field field = currentClass.getDeclaredField(fkproperty);
                field.setAccessible(true);
                field.set(currentDTO, fKeyDbPkey);
            }
        }
    }

    private static void setFKeyDbPKeyForExistingEntity(NodeList fkeys, Class currentClass, Object currentDTO) throws Exception {

        if (fkeys != null && fkeys.getLength() > 0) {
            for (int i = 0; i < fkeys.getLength(); i++) {
                Element currentFkeyElement = (Element) fkeys.item(i);
                String fkproperty = currentFkeyElement.getAttribute("fkproperty");
                if (fkproperty.isEmpty()) {
                    processError("FkProperty attribute for "
                            + currentFkeyElement.getParentNode().getParentNode().getLocalName() + " does not exist.", GobiiStatusLevel.ERROR);
                }
                Field field = currentClass.getDeclaredField(fkproperty);
                field.setAccessible(true);

                Element currentFkeydbPKeyElement = (Element) currentFkeyElement.getElementsByTagName("DbPKey").item(0);
                validateNode(currentFkeydbPKeyElement, currentFkeyElement.getTagName(), "DbPKey");
                currentFkeydbPKeyElement.setTextContent(field.get(currentDTO).toString());
            }
        }
    }

    private static Integer getFKeyDbPKey(Element currentFkeyElement, Element parentElement, String dbPkeysurrogateValue, Document document, XPath xPath) throws Exception {

        String entity = currentFkeyElement.getAttribute("entity");
        if (entity.isEmpty()) {
            processError("Entity attribute for "
                    + currentFkeyElement.getParentNode().getParentNode().getLocalName() + " does not exist.", GobiiStatusLevel.ERROR);
        }
        String fKeyDbPkeyValue = currentFkeyElement.getElementsByTagName("DbPKeySurrogate").item(0).getTextContent();
        if (fKeyDbPkeyValue.isEmpty()) {
            processError("DbPKeySurrogate attribute for "
                    + currentFkeyElement.getParentNode().getParentNode().getLocalName() + " does not exist.", GobiiStatusLevel.ERROR);
        }

        System.out.println("\nWriting " + entity + " (" + fKeyDbPkeyValue + ") FkeyDbPkey for " + parentElement.getLocalName() +
                "  (" + dbPkeysurrogateValue + " ) to file...\n");

        XPathExpression exprParentFkey = xPath.compile("//" + entity + "/parent::*");
        Element ancestor = (Element) exprParentFkey.evaluate(document, XPathConstants.NODE);

        String fkeyPKey = ancestor.getAttribute("DbPKeysurrogate");
        if (fkeyPKey.isEmpty()) {
            processError("DbPKeySurrogate attribute for " + ancestor.getLocalName() + " does not exist.", GobiiStatusLevel.ERROR);
        }

        String exprCheckIfFKeyExists = "//Entities/" + ancestor.getNodeName() + "/" + entity + "/Properties[" + fkeyPKey + "='" + fKeyDbPkeyValue + "']";
        XPathExpression xPathExprNodeFKey = xPath.compile(exprCheckIfFKeyExists);
        Element nodeFKey = (Element) xPathExprNodeFKey.evaluate(document, XPathConstants.NODE);
        Element parentNode = (Element) nodeFKey.getParentNode();
        Element dbPkeyNode = ((Element) parentNode.getElementsByTagName("Keys").item(0));
        validateNode(dbPkeyNode, parentElement.getTagName(), "Keys");

        String dbPkeyValue = dbPkeyNode.getElementsByTagName("DbPKey").item(0).getTextContent();
        if (dbPkeyValue.isEmpty()) {
            processError("DbPKey attribute for " + dbPkeyNode.getLocalName() + " does not exist.", GobiiStatusLevel.ERROR);
        }

        // set to <FKey><DbPkey></DbPkey></Fkey>

        Element currentFkeydbPKeyElement = (Element) currentFkeyElement.getElementsByTagName("DbPKey").item(0);
        validateNode(currentFkeydbPKeyElement, currentFkeyElement.getTagName(), "DbPKey");
        currentFkeydbPKeyElement.setTextContent(dbPkeyValue);
        return Integer.parseInt(dbPkeyValue);
    }

    private static void writePkValues(NodeList nodeList, XPath xPath, Document document) throws Exception {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Element parentElement = (Element) element.getParentNode();
            String parentLocalName = parentElement.getLocalName();
            Element rootElement = (Element) parentElement.getParentNode();
            String DBPKeysurrogateName = rootElement.getAttribute("DbPKeysurrogate");
            if (DBPKeysurrogateName.isEmpty()) {
                processError("DbPKeysurrogate (" + DBPKeysurrogateName + ") attribute for " +
                        rootElement.getLocalName() + " entity cannot be empty.", GobiiStatusLevel.ERROR);
            }
            Element props = (Element) parentElement.getElementsByTagName("Properties").item(0);
            validateNode(props, parentElement.getTagName(), "Properties");
            Node dbPkeysurrogateNode = props.getElementsByTagName(DBPKeysurrogateName).item(0);
            validateNode(dbPkeysurrogateNode, parentElement.getTagName(), DBPKeysurrogateName);

            String dbPkeysurrogateValue = dbPkeysurrogateNode.getTextContent();
            Element dbPKey = (Element) element.getElementsByTagName("DbPKey").item(0);
            validateNode(dbPKey, element.getTagName(), "DbPKey");
            NodeList fkeys = element.getElementsByTagName("Fkey");

            Integer returnEntityId = createEntity(parentElement, parentLocalName, fkeys, dbPkeysurrogateValue, xPath, document);
            System.out.println("\nWriting DbPKey for " + parentLocalName + " (" + dbPkeysurrogateValue + ") " + " to file...\n");
            dbPKey.setTextContent(returnEntityId.toString());
        }
    }

    private static void getEntities(XPath xPath, Document document, File fXmlFile) throws Exception {

        /* get nodes with no FKey dependencies to update DbPKey */

        String expr = "//*[local-name() = 'Keys' and not(descendant::*[local-name() = 'Fkey'])]";
        XPathExpression xPathExpression = xPath.compile(expr);
        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        writePkValues(nodeList, xPath, document);
        writeToFile(document, fXmlFile);

        /* update DbPKey of elements with FKey dependencies */
        String constantStr = "//*[local-name() = 'Keys' and descendant::*[local-name() = 'Fkey'] and parent::*[local-name() = ";
        List<String> entityList = new ArrayList<>();
        entityList.add("Contact");
        entityList.add("Protocol");
        entityList.add("Project");
        entityList.add("VendorProtocol");
        entityList.add("Experiment");
        entityList.add("Dataset");

        for (String anEntityList : entityList) {
            expr = constantStr + "'" + anEntityList + "']]";
            xPathExpression = xPath.compile(expr);
            nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
            writePkValues(nodeList, xPath, document);
        }
        writeToFile(document, fXmlFile);
    }

    private static void writeToFile(Document document, File fXmlFile) throws Exception {

        // Get file ready to write
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new FileWriter(fXmlFile));
        transformer.transform(new DOMSource(document), result);

        // Write file out
        result.getWriter().flush();
    }

    private static String createDirectory(String folderName) throws Exception {

        LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO();
        RestUri previewTestUriCreate = GobiiClientContext
                .getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_FILE_LOAD);
        previewTestUriCreate.setParamValue("id", folderName);
        GobiiEnvelopeRestResource<LoaderFilePreviewDTO,LoaderFilePreviewDTO> gobiiEnvelopeRestResourceCreate = new GobiiEnvelopeRestResource<>(previewTestUriCreate);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeCreate = gobiiEnvelopeRestResourceCreate.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));
        checkStatus(resultEnvelopeCreate);
        return resultEnvelopeCreate.getPayload().getData().get(0).getPreviewFileName();
    }

    private static String uploadFiles(String jobName, String sourcePath, String filesPath) throws Exception {

        String fileDirectoryName = null;
        File file = new File(sourcePath);
        try {
            RestUri restUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .fileForJob(jobName, GobiiFileProcessDir.RAW_USER_FILES, file.getName());
            HttpMethodResult httpMethodResult = GobiiClientContext.getInstance(null, false)
                    .getHttp()
                    .upload(restUri, file);
            if (httpMethodResult.getResponseCode() != HttpStatus.SC_OK) {
                processError("Error uploading data: " + file.getName(), GobiiStatusLevel.ERROR);
            } else {
                fileDirectoryName = filesPath;
                System.out.println("\nSuccessfully uploaded file.");
            }
        } catch (Exception e) {
            processError("Error uploading data: " + file.getName() + ": " + e.getMessage(), GobiiStatusLevel.ERROR);
        }
        return fileDirectoryName;
    }

    private static void downloadFiles(String localPathName, String jobName, List<GobiiDataSetExtract> returnGobiiDatasetExtractList) throws Exception {

        System.out.println("\nDownloading files for " + jobName + " to " + localPathName + ".");

        File localDir = new File(localPathName);
        if (!localDir.exists() || !localDir.isDirectory()) {
            localDir.mkdir();
        }

        for (GobiiDataSetExtract currentGobiiDatasetExtract : returnGobiiDatasetExtractList) {

            // loop through the existing extracted files

            for (File currentFile : currentGobiiDatasetExtract.getExtractedFiles()) {

                String currentFileName = currentFile.getName();

                System.out.println("\nDownloading " + currentFileName + "....\n");

                RestUri restUri = GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .fileForJob(jobName, GobiiFileProcessDir.EXTRACTOR_OUTPUT, currentFileName)
                        .withDestinationFqpn(localPathName + "/" + currentFileName);


                HttpMethodResult httpMethodResult = GobiiClientContext.getInstance(null, false)
                        .getHttp()
                        .get(restUri);

                if (httpMethodResult.getResponseCode() != HttpStatus.SC_OK) {

                    processError("\nError in downloading " + currentFileName + ": " + httpMethodResult.getResponseCode() + ": " + httpMethodResult.getReasonPhrase(), GobiiStatusLevel.WARNING);
                    continue;
                }

                System.out.println("\nSuccessfully downloaded " + currentFileName);
            }
        }

    }


    private static void compareExtractedFiles(String localPathName, File[] extractedFiles) throws Exception {

        System.out.println("\nComparing extracted files by ADL with extracted files from extractor UI...\n");

        for (File currentFile : extractedFiles) {

            String fileName = currentFile.getName();

            if (fileName.endsWith(".log") || fileName.endsWith(".XML") || fileName.equals("summary.file")) {
                //skip
                continue;
            }

            File localFile = new File(localPathName + "/" + fileName);

            if (!localFile.exists()) {
                processError("\nFile: " + fileName + " does not exist in the local files.", GobiiStatusLevel.WARNING);
                continue;
            }

            if (localFile.isDirectory()) {
                processError("\nFile: " + fileName + " is a directory.", GobiiStatusLevel.WARNING);
                continue;
            }

            boolean isFileEqual = FileUtils.contentEquals(currentFile, localFile);

            if (!isFileEqual) {

                processError("\nExtracted file: " + fileName +" from ADL and extractor UI are not equal.", GobiiStatusLevel.WARNING);
                continue;

            }

            System.out.println("\nExtracted file: " + fileName + " from ADL and extractor UI are equal");


        }



    }


    private static LoaderInstructionFilesDTO createInstructionFileDTO(String instructionFilePath, String folderName) throws Exception {

        LoaderInstructionFilesDTO loaderInstructionFilesDTO = new LoaderInstructionFilesDTO();
        try {
            File instructionFile = new File(instructionFilePath);
            InstructionFileAccess<GobiiLoaderInstruction> instructionInstructionFileAccess = new InstructionFileAccess<>(GobiiLoaderInstruction.class);
            List<GobiiLoaderInstruction> instructions =
                    instructionInstructionFileAccess.getInstructions(instructionFilePath,
                            GobiiLoaderInstruction[].class);
            if (null != instructions) {
                String instructionFileName = instructionFile.getName();
                String justName = instructionFileName.replaceFirst("[.][^.]+$", "");
                loaderInstructionFilesDTO.setInstructionFileName(folderName + "_" + justName);
                loaderInstructionFilesDTO.setGobiiLoaderInstructions(instructions);
            } else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The instruction file exists, but could not be read: " + instructionFilePath);
            }
        } catch (Exception e) {
            processError("Error creating instruction file DTO: " + e.getMessage(), GobiiStatusLevel.ERROR);
        }
        return loaderInstructionFilesDTO;
    }

    private static boolean submitInstructionFile(LoaderInstructionFilesDTO loaderInstructionFilesDTO, String jobPayloadType) throws Exception {

        boolean returnVal = false;

        InstructionFileValidator instructionFileValidator = new InstructionFileValidator(loaderInstructionFilesDTO.getGobiiLoaderInstructions());
        instructionFileValidator.processInstructionFile();
        String validationStatus = instructionFileValidator.validate();
        if (validationStatus != null) {
            processError("Instruction file validation failed. " + validationStatus, GobiiStatusLevel.ERROR);
        } else {
            try {
                if (jobPayloadType.equals(JobPayloadType.CV_PAYLOADTYPE_MARKERS.getCvName())) {
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions().get(0).setJobPayloadType(JobPayloadType.CV_PAYLOADTYPE_MARKERS);
                } else if (jobPayloadType.equals(JobPayloadType.CV_PAYLOADTYPE_SAMPLES.getCvName())) {
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions().get(0).setJobPayloadType(JobPayloadType.CV_PAYLOADTYPE_SAMPLES);
                } else if (jobPayloadType.equals(JobPayloadType.CV_PAYLOADTYPE_MATRIX.getCvName())) {
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions().get(0).setJobPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX);
                } else {
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions().get(0).setJobPayloadType(JobPayloadType.CV_PAYLOADTYPE_MATRIX);
                }
                PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(loaderInstructionFilesDTO, GobiiProcessType.CREATE);
                GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
                PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResource.post(LoaderInstructionFilesDTO.class,
                        payloadEnvelope);
                checkStatus(loaderInstructionFileDTOResponseEnvelope);
                Payload<LoaderInstructionFilesDTO> payload = loaderInstructionFileDTOResponseEnvelope.getPayload();
                if (payload.getData() == null || payload.getData().size() < 1) {
                    System.out.println("Could not get a valid response from server. Please try again.");
                } else {
                    String instructionFileName = payload.getData().get(0).getInstructionFileName();
                    System.out.println("Request " + instructionFileName + " submitted.");
                    returnVal = checkJobStatusLoad(instructionFileName);
                }
            } catch (Exception err) {
                processError("Error submitting instruction file: " + err.getMessage(), GobiiStatusLevel.ERROR);
            }
        }

        return returnVal;
    }

    private static boolean checkJobStatusLoad(String instructionFileName) throws Exception {

        boolean returnVal = false;

        GobiiEnvelopeRestResource<LoaderInstructionFilesDTO,LoaderInstructionFilesDTO> loaderJobResponseEnvolope = new GobiiEnvelopeRestResource<>(
                GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS)
                        .addUriParam("instructionFileName", instructionFileName));


        boolean statusDetermined = false;
        String currentStatus = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long startTime = System.currentTimeMillis();

        while (!statusDetermined && ((System.currentTimeMillis() - startTime) <= timeoutInMillis)) {

            PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFilesDTOPayloadEnvelope = loaderJobResponseEnvolope.get(LoaderInstructionFilesDTO.class);
            checkStatus(loaderInstructionFilesDTOPayloadEnvelope, true);

            // because we called checkStatus() with second parameter true, we know that there is at least one payload item
            List<LoaderInstructionFilesDTO> data = loaderInstructionFilesDTOPayloadEnvelope.getPayload().getData();
            GobiiLoaderInstruction gobiiLoaderInstruction = data.get(0).getGobiiLoaderInstructions().get(0);

            if (!currentStatus.equals(gobiiLoaderInstruction.getGobiiJobStatus().getCvName())) {
                currentStatus = gobiiLoaderInstruction.getGobiiJobStatus().getCvName();
                System.out.println("\nJob " + instructionFileName + " current status: " + currentStatus + " at " + dateFormat.format(new Date()));
            }

            if (gobiiLoaderInstruction.getGobiiJobStatus().getCvName().equalsIgnoreCase("failed") ||
                    gobiiLoaderInstruction.getGobiiJobStatus().getCvName().equalsIgnoreCase("aborted")) {

                System.out.println("\nJob " + instructionFileName + " failed. \n" + gobiiLoaderInstruction.getLogMessage());
                returnVal = false;
                statusDetermined = true;

            } else if (gobiiLoaderInstruction.getGobiiJobStatus().getCvName().equalsIgnoreCase("completed")) {
                returnVal = true;
                statusDetermined = true;
            }

            System.out.print(".");
            Thread.sleep(1000);
        }

        if (!statusDetermined) {
            processError("\n\nMaximum execution time of " + timeout + " minute/s exceeded\n", GobiiStatusLevel.ERROR);
        }

        return returnVal;
    }

    private static boolean checkJobStatusExtract(String instructionFileName) throws Exception {

        boolean returnVal = false;
        dataSetExtractReturnList = null;

        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO,ExtractorInstructionFilesDTO> extractJobResponseEnvelope = new GobiiEnvelopeRestResource<>(
                GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceColl(GobiiServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS)
                        .addUriParam("instructionFileName", instructionFileName));

        boolean statusDetermined = false;
        String currentStatus = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long startTime = System.currentTimeMillis();

        while (!statusDetermined && ((System.currentTimeMillis() - startTime) <= timeoutInMillis)) {

            PayloadEnvelope<ExtractorInstructionFilesDTO>  extractorInstructionFilesDTOPayloadEnvelope = extractJobResponseEnvelope.get(ExtractorInstructionFilesDTO.class);
            checkStatus(extractorInstructionFilesDTOPayloadEnvelope, true);

            List<ExtractorInstructionFilesDTO> data = extractorInstructionFilesDTOPayloadEnvelope.getPayload().getData();
            GobiiExtractorInstruction gobiiExtractorInstruction = data.get(0).getGobiiExtractorInstructions().get(0);
            GobiiDataSetExtract gobiiDataSetExtract = gobiiExtractorInstruction.getDataSetExtracts().get(0);

            if (!currentStatus.equals(gobiiDataSetExtract.getGobiiJobStatus().getCvName())) {

                currentStatus = gobiiDataSetExtract.getGobiiJobStatus().getCvName();
                System.out.println("\nJob " + instructionFileName + " current status: " + currentStatus + " at " + dateFormat.format(new Date()));

            }

            if (gobiiDataSetExtract.getGobiiJobStatus().getCvName().equalsIgnoreCase("failed") ||
                    gobiiDataSetExtract.getGobiiJobStatus().getCvName().equalsIgnoreCase("aborted")) {

                System.out.println("\nJob " + instructionFileName + " failed. \n" + gobiiDataSetExtract.getLogMessage());
                statusDetermined = true;

            } else if (gobiiDataSetExtract.getGobiiJobStatus().getCvName().equalsIgnoreCase("completed")) {

                returnVal = true;
                statusDetermined = true;

                dataSetExtractReturnList = gobiiExtractorInstruction.getDataSetExtracts();
            }


            System.out.print(".");
            Thread.sleep(1000);

        }

        if (!statusDetermined) {

            processError("\n\nMaximum execution time of " + timeout + " minute/s exceeded\n", GobiiStatusLevel.ERROR);

        }

        return returnVal;

    }

    private static String getScenarioValues(Element currentElement, String attribute) {

        Node node = currentElement.getElementsByTagName(attribute).item(0);
        validateNode(node, currentElement.getTagName(), attribute);

        return node.getTextContent();


    }

    private static boolean parseLoadScenarios(NodeList nodeList, XPath xPath, Document document, File fXmlFile, String subDirectoryName) throws Exception {

        JsonParser parser = new JsonParser();
        String folderName, filesPath, digestPath, jobId;
        boolean returnVal = false;

        Map<GobiiFileProcessDir, String> fileLocations = serverConfig.getFileLocations();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element currentElement = (Element) nodeList.item(i);
            Node scenarioNode = currentElement.getElementsByTagName("Name").item(0);
            validateNode(scenarioNode, currentElement.getTagName(), "Name");
            String scenarioName = scenarioNode.getTextContent();
            Node jobPayloadTypeNode = currentElement.getElementsByTagName("PayloadType").item(0);
            validateNode(jobPayloadTypeNode, currentElement.getTagName(), "PayloadType");
            String jobPayloadType = jobPayloadTypeNode.getTextContent();
            System.out.println("Parsing scenario: " + scenarioName);
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            jobId = dateFormat.format(new Date());
            folderName = "data_" + jobId + "_" + scenarioName;
            filesPath = fileLocations.get(GobiiFileProcessDir.RAW_USER_FILES) + folderName;
            digestPath = fileLocations.get(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES) + folderName;

            String dataExpr = "//Scenario[Name='" + scenarioName + "']/Files/Data";
            XPathExpression xPathExpressionData = xPath.compile(dataExpr);
            String dataFileName = (String) xPathExpressionData.evaluate(document, XPathConstants.STRING);
            String sourcePath = getParentSourcePath(subDirectoryName) + "/" + dataFileName;
            boolean writeSourcePath = true;
            if (!(new File(sourcePath).exists())) {
                writeSourcePath = false;
                processError("Data file " + sourcePath + " for scenario " + scenarioName + " not found.", GobiiStatusLevel.ERROR);
            }

            String fileExpr = "//Scenario[Name='" + scenarioName + "']/Files/Instruction";
            XPathExpression xPathExpressionFiles = xPath.compile(fileExpr);
            String instructionFileName = (String) xPathExpressionFiles.evaluate(document, XPathConstants.STRING);
            String instructionFilePath = getParentSourcePath(subDirectoryName) + "/" + instructionFileName;
            Object obj;
            if (!new File(instructionFilePath).exists()) {
                ClassLoader classLoader = GobiiAdl.class.getClassLoader();
                if (classLoader.getResourceAsStream(instructionFilePath) == null) {
                    processError(" Instruction file template " + instructionFilePath + " not found.", GobiiStatusLevel.ERROR);
                }

                InputStream inputStream = classLoader.getResourceAsStream(instructionFilePath);
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                obj = parser.parse(sb.toString());
            } else {
                obj = parser.parse(new FileReader(instructionFilePath));
            }
            JsonArray jsonArray = (JsonArray) obj;
            NodeList dbPkeys = currentElement.getElementsByTagName("DbFkey");
            for (int j = 0; j < dbPkeys.getLength(); j++) {
                Element currentDbPkeyElement = (Element) dbPkeys.item(j);
                String entityName = currentDbPkeyElement.getAttribute("entity");
                if (entityName.isEmpty()) {
                    processError("Entity attribute for " + currentElement.getLocalName() + " entity cannot be empty.", GobiiStatusLevel.ERROR);
                }

                Element dbPkeySurrogateElement = (Element) currentDbPkeyElement.getElementsByTagName("DbPKeySurrogate").item(0);
                validateNode(dbPkeySurrogateElement, currentElement.getTagName(), "DbPKeySurrogate");
                String dbPkeySurrogateValue = dbPkeySurrogateElement.getTextContent();
                if (dbPkeySurrogateValue.isEmpty()) {
                    processError("DbPKeySurrogate attribute for " + currentElement.getLocalName() + " entity cannot be empty.", GobiiStatusLevel.ERROR);
                }
                // get DbPKeysurrogate attribute of entity (ie Contacts - Email)
                String expr = "//" + entityName + "s/@DbPKeysurrogate";
                XPathExpression xPathExpression = xPath.compile(expr);
                String refAttr = (String) xPathExpression.evaluate(document, XPathConstants.STRING);
                // check if entity with the specify dbPkeysurrogate value exist in the file
                expr = "count(//Entities/" + entityName + "s/" + entityName + "/Properties[" + refAttr + "='" + dbPkeySurrogateValue + "'])";
                xPathExpression = xPath.compile(expr);
                Double count = (Double) xPathExpression.evaluate(document, XPathConstants.NUMBER);
                if (count <= 0) {
                    processError(entityName + ": " + dbPkeySurrogateValue + " does not exist in the file.", GobiiStatusLevel.ERROR);
                }

                expr = "//" + entityName + "[Properties/" + refAttr + "='" + dbPkeySurrogateValue + "']/Keys/DbPKey";
                xPathExpression = xPath.compile(expr);
                Element currentEntity = (Element) xPathExpression.evaluate(document, XPathConstants.NODE);
                if (currentEntity.getTextContent().isEmpty()) {
                    processError("The primary DB key of " + entityName + ": " + dbPkeySurrogateValue + " is not written in the file", GobiiStatusLevel.ERROR);
                }

                Element dbPkeyElement = (Element) currentDbPkeyElement.getElementsByTagName("DbPKey").item(0);
                validateNode(dbPkeyElement, currentElement.getTagName(), "DbPKey");
                String currentEntityId = currentEntity.getTextContent();
                dbPkeyElement.setTextContent(currentEntityId);
                writeToFile(document, fXmlFile);

                //write to instruction file
                if (entityName.equals("Dataset")) {
                    entityName = "dataSet";
                } else {
                    entityName = entityName.toLowerCase();
                }

                for (int k = 0; k < jsonArray.size(); k++) {
                    JsonObject instructionObject = (JsonObject) jsonArray.get(k);

                    if (instructionObject.has("gobiiCropType")) {
                        instructionObject.addProperty("gobiiCropType", crop);
                    }

                    if (entityName.equals("contact")) {
                        if (instructionObject.has("contactId")) {
                            instructionObject.addProperty("contactId", currentEntityId);
                        }
                        if (instructionObject.has("contactEmail")) {
                            instructionObject.addProperty("contactEmail", dbPkeySurrogateValue);
                        }
                        continue;
                    }

                    if (entityName.equals("dataSet")) {
                        if (instructionObject.has("dataSetId")) {
                            instructionObject.addProperty("dataSetId", currentEntityId);
                        }

                        // get datasetType ID

                        RestUri datasetGetUri = GobiiClientContext.getInstance(null, false)
                                .getUriFactory()
                                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
                        datasetGetUri.setParamValue("id", currentEntityId);
                        GobiiEnvelopeRestResource<DataSetDTO,DataSetDTO> gobiiEnvelopeRestResourceForDatasetGet = new GobiiEnvelopeRestResource<>(datasetGetUri);
                        PayloadEnvelope<DataSetDTO> resultEnvelopeForDatasetGet = gobiiEnvelopeRestResourceForDatasetGet.get(DataSetDTO.class);
                        checkStatus(resultEnvelopeForDatasetGet);

                        DataSetDTO dataSetDTOGetResponse = resultEnvelopeForDatasetGet.getPayload().getData().get(0);

                        // set datasetType fields in instruction file template
                        JsonObject datasetTypeObj = (JsonObject) instructionObject.get("datasetType");
                        datasetTypeObj.addProperty("name", dataSetDTOGetResponse.getDatatypeName().toUpperCase());
                        datasetTypeObj.addProperty("id", dataSetDTOGetResponse.getDatatypeId());

                        instructionObject.add("datasetType", datasetTypeObj);
                    }

                    JsonObject tempObject = (JsonObject) instructionObject.get(entityName);
                    tempObject.addProperty("name", dbPkeySurrogateValue);
                    tempObject.addProperty("id", currentEntityId);

                    instructionObject.add(entityName, tempObject);

                    if (writeSourcePath) {
                        JsonObject gobiiFileObject = (JsonObject) instructionObject.get("gobiiFile");
                        gobiiFileObject.addProperty("source", filesPath);
                        gobiiFileObject.addProperty("destination", digestPath);
                        instructionObject.add("gobiiFile", gobiiFileObject);
                    }

                    // modify gobiiFileColumns attribute
                    JsonArray gobiiFileColumnsArr = (JsonArray) instructionObject.get("gobiiFileColumns");
                    for (int o = 0; o < gobiiFileColumnsArr.size(); o++) {
                        JsonObject fileColumnObj = (JsonObject) gobiiFileColumnsArr.get(o);
                        if (fileColumnObj.has("gobiiColumnType")) {
                            String columnType = fileColumnObj.get("gobiiColumnType").getAsString();
                            if (columnType.equals("CONSTANT")) {
                                if (fileColumnObj.has("name")) {
                                    String columnName = fileColumnObj.get("name").getAsString();
                                    switch (columnName) {
                                        case "project_id":
                                            if (entityName.equals("project")) {
                                                fileColumnObj.addProperty("constantValue", currentEntityId);
                                            }
                                            break;
                                        case "experiment_id":
                                            if (entityName.equals("experiment")) {
                                                fileColumnObj.addProperty("constantValue", currentEntityId);
                                            }
                                            break;
                                        case "platform_id":
                                            if (entityName.equals("platform")) {
                                                fileColumnObj.addProperty("constantValue", currentEntityId);
                                            }
                                            break;
                                        case "map_id":
                                            if (entityName.equals("mapset")) {
                                                fileColumnObj.addProperty("constantValue", currentEntityId);
                                            }
                                            break;
                                        case "dataset_id":
                                            if (entityName.equals("dataSet")) {
                                                fileColumnObj.addProperty("constantValue", currentEntityId);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                gobiiFileColumnsArr.set(o, fileColumnObj);
                            }
                        }
                    }
                    instructionObject.add("gobiiFileColumns", gobiiFileColumnsArr);
                    jsonArray.set(k, instructionObject);
                }
            } // iterate instruciton file json

            // update instruction file
            System.out.println("\nWriting instruction file for " + scenarioName + "\n");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJsonString = gson.toJson(jsonArray);
            if (new File(instructionFilePath).exists()) {
                FileWriter instructionFileWriter = new FileWriter(instructionFilePath);
                instructionFileWriter.write(prettyJsonString);
                instructionFileWriter.close();

                // CREATE DIRECTORY
                String jobName = createDirectory(folderName);

                // UPLOAD FILES
                if (writeSourcePath) {
                    System.out.println("\nUploading data file, this may take a few minutes...");
                    uploadFiles(jobName, sourcePath, filesPath);
                }


                // CREATE LOADER INSTRUCTION FILE
                LoaderInstructionFilesDTO loaderInstructionFilesDTO = createInstructionFileDTO(instructionFilePath, folderName);

                System.out.println("Submitting file " + instructionFilePath + " - " + folderName);
                // SUBMIT INSTRUCTION FILE DTO
                returnVal = submitInstructionFile(loaderInstructionFilesDTO, jobPayloadType);
            }
        } // iterate scenarios

        return returnVal;
    }

    private static void setOption(Options options,
                                  String argument,
                                  boolean requiresValue,
                                  String helpText,
                                  String shortName) throws Exception {

        if (options.getOption(argument) != null) {
            processError("Option is already defined: " + argument, GobiiStatusLevel.ERROR);
        }

        //There does not appear to be a way to set argument name with the variants on addOption()
        options
                .addOption(argument, requiresValue, helpText)
                .getOption(argument)
                .setArgName(shortName);
    }

    private static String getMessageForMissingOption(String optionName, Options options) {

        Option option = options.getOption(optionName);
        if (option == null) {
            return "Invalid argument: " + optionName;
        }
        return "Value is required: " + option.getArgName() + ", " + option.getDescription();
    }

    private static void processError(String message, GobiiStatusLevel gobiiStatusLevel) {

        System.out.println(message);
        if (gobiiStatusLevel.equals(GobiiStatusLevel.ERROR)) {
            ErrorLogger.logError("GobiiADL", message);
            System.err.print(message);
            System.exit(1);
        }

    }

    private static File[] getChildrenFiles(File fileName) {

        File[] directoryListing = null;

        if (fileName.list().length == 0) {

            processError("\nDirectory " + fileName.getName() + " is empty!", GobiiStatusLevel.ERROR);

        }

        directoryListing = fileName.listFiles();

        return directoryListing;

    }


    private static void validateSubDirectory(File currentSubDir) throws Exception {

        File[] subDirFiles = getChildrenFiles(currentSubDir);

        /** check if directory has
         * 1 XML file
         * subdirectory called data_files
         * subdirectory called instruction_templates
         * **/

        xmlFile = null;
        hasXmlFile = false;

        for (File currentFile : subDirFiles) {
            String fileName = currentFile.getName();
            if (fileName.endsWith(".xml") || fileName.endsWith(".XML")) {

                if (!hasXmlFile) {
                    hasXmlFile = true;
                    xmlFile = currentFile;
                } else {
                    processError("\nDirectory " + currentSubDir.getName() + " has more than one (1) XML file.", GobiiStatusLevel.ERROR);
                }

            }

        }

        if (!hasXmlFile) {
            processError("\nDirectory " + currentSubDir.getName() + " does not have an XML file.", GobiiStatusLevel.ERROR);
        }

        /** check if the files in scenarios in the XML file exists **/

        /** validate for LOAD scenarios **/

        Document document = createDocument(xmlFile);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String getAllScenarios = "//Scenarios/Load/*";
        XPathExpression xPathExpression = xPath.compile(getAllScenarios);
        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);


        for (int i = 0; i < nodeList.getLength(); i++) {
            Element currentElement = (Element) nodeList.item(i);
            Node scenarioNode = currentElement.getElementsByTagName("Name").item(0);
            validateNode(scenarioNode, currentElement.getTagName(), "Name");
            String scenarioName = scenarioNode.getTextContent();
            String dataExpr = "//Scenario[Name='" + scenarioName + "']/Files/Data";
            XPathExpression xPathExpressionData = xPath.compile(dataExpr);
            String dataFileName = (String) xPathExpressionData.evaluate(document, XPathConstants.STRING);
            String sourcePath = getParentSourcePath(currentSubDir.getName()) + "/" + dataFileName;

            if (!(new File(sourcePath).exists())) {
                processError("Data file " + sourcePath + " for scenario " + scenarioName + " not found.", GobiiStatusLevel.ERROR);
            }

            String fileExpr = "//Scenario[Name='" + scenarioName + "']/Files/Instruction";
            XPathExpression xPathExpressionFiles = xPath.compile(fileExpr);
            String instructionFileName = (String) xPathExpressionFiles.evaluate(document, XPathConstants.STRING);
            String instructionFilePath = getParentSourcePath(currentSubDir.getName()) + "/" + instructionFileName;
            if (!new File(instructionFilePath).exists()) {
                processError(" Instruction file template " + instructionFilePath + " not found.", GobiiStatusLevel.ERROR);
            }

        }

    }

    private static Document createDocument(File xmlFile) throws Exception{

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        return document;

    }

    private static String getParentSourcePath(String subDirectoryName) {


        String parentSourcePath;
        if (batchMode) {
            parentSourcePath = parentDirectoryPath.getAbsolutePath() + "/" + subDirectoryName;
        } else {
            parentSourcePath = parentDirectoryPath.getAbsolutePath();
        }

        return parentSourcePath;

    }

    private static boolean processXMLLoadScenario(File xmlFile, String subDirectoryName) throws Exception {

        boolean returnVal;

        Document document = createDocument(xmlFile);
        XPath xPath = XPathFactory.newInstance().newXPath();

        //check if all DbPKeysurrogate value is unique for each entity
        String getAllNotesExpr = "//Entities/*";
        XPathExpression xPathExpression = xPath.compile(getAllNotesExpr);
        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        validateKeys(nodeList, xPath, document);
        System.out.println("\nFile passed key checks...\n");

        getEntities(xPath, document, xmlFile);
        System.out.println("\n\n\nSuccessfully saved DbPKeys to file\n");
        System.out.println("\nParsing Scenarios...\n");
        String getAllScenarios = "//Scenarios/Load/*";
        xPathExpression = xPath.compile(getAllScenarios);
        nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        returnVal = parseLoadScenarios(nodeList, xPath, document, xmlFile, subDirectoryName);


        return returnVal;

    }

    private static void processXMLExtractScenario(File xmlFile, File subDirectory) throws Exception{

        String jobId, jobName;

        String subDirectoryName = subDirectory.getName();

        System.out.println("\nChecking for extract scenarios for " + subDirectoryName);

        Document document = createDocument(xmlFile);
        XPath xPath = XPathFactory.newInstance().newXPath();

        String getAllScenarios = "//Scenarios/Extract/*";
        XPathExpression xPathExpression = xPath.compile(getAllScenarios);
        NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);

        if (nodeList.getLength() == 0) {

            processError("\nNo extract scenarios for " + subDirectoryName, GobiiStatusLevel.INFO);

        }


        // loop through scenarios

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element currentElement = (Element) nodeList.item(i);
            String scenarioName = getScenarioValues(currentElement, "Name");

            System.out.println("\nParsing scenario: " + scenarioName);

            String extractType = getScenarioValues(currentElement, "ExtractType");
            // get extractType enum
            GobiiExtractFilterType gobiiExtractFilterType = getGobiiExtractFilterType(extractType);

            String fileType = getScenarioValues(currentElement, "FileType");
            // get filetype enum
            GobiiFileType gobiiFileType = getGobiiFileType(fileType);

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            jobId = dateFormat.format(new Date());
            jobName = "extract_" + jobId + "_" + scenarioName;

            // get contact
            String contactEmail = getScenarioValues(currentElement, "Contact");

            PayloadEnvelope<ContactDTO> resultEnvelope = getContactByEmail(contactEmail);

            if (resultEnvelope.getPayload().getData().size() == 0) {

                processError("\nContact email " + contactEmail + " doesn't exist.", GobiiStatusLevel.ERROR);

            }

            ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);

            GobiiExtractorInstruction gobiiExtractorInstruction = new GobiiExtractorInstruction();
            gobiiExtractorInstruction.setContactEmail(contactDTO.getEmail());
            gobiiExtractorInstruction.setContactId(contactDTO.getContactId());
            gobiiExtractorInstruction.setGobiiCropType(crop);

            // get mapsets
            Element mapsets = (Element) currentElement.getElementsByTagName("Mapsets").item(0);
            List<MapsetDTO> mapsetDTOList = getMapsets();

            if (mapsets != null) {
                NodeList mapsetList = mapsets.getElementsByTagName("Mapset");

                if (mapsetList.getLength() != 0) {

                    for (int j=0; j<mapsetList.getLength();j++) {

                        Element currentMapset = (Element) mapsetList.item(j);
                        String mapsetName = currentMapset.getTextContent();
                        // get mapset ID

                        for (MapsetDTO currentMapsetDTO : mapsetDTOList) {

                            if (currentMapsetDTO.getName().equals(mapsetName)) {
                                gobiiExtractorInstruction.getMapsetIds().add(currentMapsetDTO.getMapsetId());
                            }

                        }

                    }

                }
            } // end of adding mapsets


            // get datasets
            Element datasets = (Element) currentElement.getElementsByTagName("Datasets").item(0);
            validateNode(datasets, currentElement.getTagName(), "Datasets");

            NodeList datasetList = datasets.getElementsByTagName("Dataset");

            if (datasetList.getLength() == 0) {
                processError("\nNo datasets for scenario " + scenarioName, GobiiStatusLevel.WARNING);
            }

            List<DataSetDTO> datasetDTOList = getDatasets();
            List<ExperimentDTO> experimentDTOList = getExperiments();
            List<ProjectDTO> projectDTOList = getProjects();

            List<GobiiDataSetExtract> gobiiDataSetExtractsList = new ArrayList<>();

            for (int j = 0; j < datasetList.getLength(); j++) {

                Element currentDataset = (Element) datasetList.item(j);
                String datasetName = getScenarioValues(currentDataset, "DatasetName");
                String experimentName = getScenarioValues(currentDataset, "ExperimentName");
                String projectName = getScenarioValues(currentDataset, "ProjectName");
                String piContact = getScenarioValues(currentDataset, "PIContact");

                PayloadEnvelope<ContactDTO> resultEnvelopePIContact = getContactByEmail(piContact);

                if (resultEnvelopePIContact.getPayload().getData().size() == 0) {

                    processError("\nContact email " + piContact + " doesn't exist.", GobiiStatusLevel.WARNING);
                    break;

                }

                ContactDTO piContactDTO = resultEnvelopePIContact.getPayload().getData().get(0);

                Integer piContactId = piContactDTO.getContactId();

                if (piContactId == null) {
                    processError("\nPI Contact " + piContact + " not found.", GobiiStatusLevel.WARNING);
                    break;
                }

                // get projectId
                Integer projectId = null;
                Integer experimentId = null;


                for (ProjectDTO currentProjectDTO : projectDTOList) {

                    if (currentProjectDTO.getProjectName().equals(projectName) && currentProjectDTO.getPiContact().equals(piContactId)) {

                        projectId = currentProjectDTO.getProjectId();
                        break;
                    }

                }

                if (projectId == null) {
                    processError("\nProject " + projectName + " not found.", GobiiStatusLevel.WARNING);
                    break;
                }

                // get experiment Id

                for (ExperimentDTO currentExperimentDTO : experimentDTOList) {

                    if (currentExperimentDTO.getExperimentName().equals(experimentName) && currentExperimentDTO.getProjectId().equals(projectId)) {

                        experimentId = currentExperimentDTO.getExperimentId();
                        break;
                    }
                }

                if (experimentId == null) {
                    processError("\nExperiment " + experimentName + " not found.", GobiiStatusLevel.WARNING);
                    break;
                }

                // get dataset id

                for (DataSetDTO currentDatasetDTO : datasetDTOList) {

                    if (currentDatasetDTO.getDatasetName().equals(datasetName) && currentDatasetDTO.getExperimentId().equals(experimentId)) {

                        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
                        gobiiDataSetExtract.setDataSet(new PropNameId(currentDatasetDTO.getDataSetId(), currentDatasetDTO.getDatasetName()));
                        gobiiDataSetExtract.setGobiiDatasetType(null);
                        gobiiDataSetExtract.setGobiiExtractFilterType(gobiiExtractFilterType);
                        gobiiDataSetExtract.setGobiiFileType(gobiiFileType);
                        gobiiDataSetExtract.setAccolate(false);
                        gobiiDataSetExtract.setPrincipleInvestigator(null);
                        gobiiDataSetExtract.setProject(null);

                        gobiiDataSetExtractsList.add(gobiiDataSetExtract);
                        break;

                    }

                }

            }


            if (gobiiDataSetExtractsList.size() == 0) {

                processError("\nGobii Dataset extracts is empty for scenario " + scenarioName, GobiiStatusLevel.WARNING);
                continue;

            }

            // send extract request
            gobiiExtractorInstruction.setDataSetExtracts(gobiiDataSetExtractsList);

            boolean isExtractSuccessful = submitExtractInstruction(gobiiExtractorInstruction, jobName);

            int idx = 0;

            if (isExtractSuccessful && dataSetExtractReturnList != null) {

                // download files and compare to known good extracted files


                // check if existing extracted files exist
                String dataExpr = "//Scenario[Name='"+ scenarioName +"']/Files/Data";
                XPathExpression xPathData = xPath.compile(dataExpr);
                String dataFileStr = (String) xPathData.evaluate(document, XPathConstants.STRING);

                String dataFilePath = subDirectory.getAbsolutePath() + "/" + dataFileStr;

                File dataFileDir = new File(dataFilePath);

                File[] extractedFiles = dataFileDir.listFiles();


                String localPathName = subDirectory.getAbsoluteFile() + "/" + jobName;

                downloadFiles(localPathName, jobName, dataSetExtractReturnList);

                if (dataFileDir.exists() && dataFileDir.isDirectory() && dataFileDir.listFiles().length > 0) {

                    compareExtractedFiles(localPathName, extractedFiles);

                }

            }

        }


    }

    private static boolean submitExtractInstruction(GobiiExtractorInstruction gobiiExtractorInstruction, String jobName) throws Exception {

        boolean returnVal = false;
        try {

            System.out.println("\nSubmitting extract request with job name: " + jobName +".\n");

            ExtractorInstructionFilesDTO extractorInstructionFilesDTO = new ExtractorInstructionFilesDTO();
            extractorInstructionFilesDTO.setInstructionFileName(jobName);
            extractorInstructionFilesDTO.getGobiiExtractorInstructions().add(gobiiExtractorInstruction);


            PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTO, GobiiProcessType.CREATE);
            GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO,ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(GobiiServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS));
            PayloadEnvelope<ExtractorInstructionFilesDTO> resultEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                    payloadEnvelope);
            checkStatus(resultEnvelope);

            Payload<ExtractorInstructionFilesDTO> payload = resultEnvelope.getPayload();
            if (payload.getData() == null || payload.getData().size() < 1) {
                System.out.println("Could not get a valid response from server. Please try again.");
            } else {

                String instructionFileName = payload.getData().get(0).getInstructionFileName();
                System.out.println("Request " + instructionFileName + " submitted.");
                returnVal = checkJobStatusExtract(instructionFileName);

                if (returnVal) {
                    System.out.println("\nExtract job " + instructionFileName + " completed.");
                }

            }
        } catch (Exception err) {

            processError("Error submitting extractor instruction file: " + err.getMessage(), GobiiStatusLevel.ERROR);

        }

        return returnVal;


    }

    private static void processDirectory(File currentDir) throws Exception {

        System.out.println("\nChecking files for " + currentDir.getName());

        validateSubDirectory(currentDir);

        /*** Process XML file ***/

        System.out.println("\nProcessing XML: " + xmlFile.getName() + " for subdirectory: " + currentDir.getName());
        boolean isLoadSuccessful = processXMLLoadScenario(xmlFile, currentDir.getName());

        if (isLoadSuccessful) {

            System.out.print("\nLoad successful for " +  currentDir.getName());

            // do extract

            processXMLExtractScenario(xmlFile, currentDir);

        }


    }

    public static void main(String[] args) throws Exception {


        // define commandline options
        Options options = new Options();
        setOption(options, INPUT_HOST, true, "The URL of the gobii server to connect to", "gobii server");
        setOption(options, INPUT_USER, true, "Username of the user doing the load", "username");
        setOption(options, INPUT_PASSWORD, true, "Password of the user doing the load", "password");
        setOption(options, INPUT_TIMEOUT, true, "Maximum waiting time in minutes", "timeout");
        setOption(options, INPUT_SCENARIO, true, "Specifies the path of one subdirectory under the main directory. When specified, tool is run in single-scenario mode", "scenario");
        setOption(options, INPUT_DIRECTORY, true, "Specifies the path to the directory where the files are in", "directory");

        // parse the commandline
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        HelpFormatter helpFormatter = new HelpFormatter();

        String message;

        if (args.length == 0) {
            helpFormatter.printHelp(NAME_COMMAND, options);
            System.exit(1);
        }


        if (!commandLine.hasOption(INPUT_HOST)) {
            message = getMessageForMissingOption(INPUT_HOST, options);
            processError(message, GobiiStatusLevel.ERROR);
        }

        if (!commandLine.hasOption(INPUT_USER)) {
            message = getMessageForMissingOption(INPUT_USER, options);
            processError(message, GobiiStatusLevel.ERROR);
        }

        if (!commandLine.hasOption(INPUT_PASSWORD)) {
            message = getMessageForMissingOption(INPUT_PASSWORD, options);
            processError(message, GobiiStatusLevel.ERROR);
        }


        //set default to 10minutes;
        Integer timeoutInt = 10;

        if (commandLine.hasOption(INPUT_TIMEOUT)) {

            String inputTimeout = commandLine.getOptionValue(INPUT_TIMEOUT);

            // check if valid integer
            try {
                timeoutInt = Integer.parseInt(inputTimeout);
            } catch (NumberFormatException e) {
                processError("Invalid input for timeout: " + inputTimeout, GobiiStatusLevel.ERROR);
            }

        }

        timeout = timeoutInt.longValue();
        timeoutInMillis = TimeUnit.MINUTES.toMillis(timeout);

        String directory;
        File parentDirectory = null;

        if (commandLine.hasOption(INPUT_DIRECTORY)){

            /** Check if specified directory exists **/

            directory = commandLine.getOptionValue(INPUT_DIRECTORY);
            parentDirectory = new File(directory);

        } else if (commandLine.hasOption(INPUT_SCENARIO)) {

            batchMode = false;
            directory = commandLine.getOptionValue(INPUT_SCENARIO);
            parentDirectory = new File(directory);

        } else {
            processError("\n Either -d (path to directory) or -s (path to subdirectory) should be specified.", GobiiStatusLevel.ERROR);
        }


        if (!parentDirectory.exists()) {
            processError("\nThe directory, " + parentDirectory.getName() + " ,  does not exist. ", GobiiStatusLevel.ERROR);
        }

        // check if directory

        if (!parentDirectory.isDirectory()) {
            processError("\nFile " + parentDirectory.getName() + " is not directory.", GobiiStatusLevel.ERROR);
        }

        parentDirectoryPath = parentDirectory;


        /** log in to specified host **/

        String url = commandLine.getOptionValue(INPUT_HOST);
        String username = commandLine.getOptionValue(INPUT_USER);
        String password = commandLine.getOptionValue(INPUT_PASSWORD);

        try {
            GobiiClientContext.getInstance(url, true).getCurrentClientCropType();

            // parse URL for the context root
            URL iURL = new URL(url);
            String contextRoot = iURL.getPath();

            if ('/' != contextRoot.charAt(contextRoot.length() - 1)) {
                contextRoot = contextRoot + "/";
            }

            List<String> crops = GobiiClientContext.getInstance(null, false).getCropTypeTypes();
            for (String currentCrop : crops) {
                ServerConfig currentServerConfig = GobiiClientContext.getInstance(null, false).getServerConfig(currentCrop);
                if (contextRoot.equals(currentServerConfig.getContextRoot())) {
                    // use the crop for this server config
                    crop = currentCrop;
                    serverConfig = currentServerConfig;
                    break;
                }
            }

            if (crop == null || crop.isEmpty()) {
                processError("Undefined crop for server: " + url, GobiiStatusLevel.ERROR);
            }

            System.out.println("Logging in to " + url + " ...");
            boolean login = GobiiClientContext.getInstance(url, true).login(crop, username, password);
            if (!login) {
                String failureMessage = GobiiClientContext.getInstance(null, false).getLoginFailure();
                processError("Error logging in: " + failureMessage, GobiiStatusLevel.ERROR);
            }
            System.out.println("\nSuccessfully logged in!");
        } catch (IOException e) {
            processError("Initialization and authentication error: " + e.getMessage(), GobiiStatusLevel.ERROR);
        }


        if (batchMode) {

            File[] directoryListing = getChildrenFiles(parentDirectory);

            for (File currentSubDir : directoryListing) {

                if (!currentSubDir.isDirectory()) {
                    System.out.println("\nWarning: " + currentSubDir.getName() + " is not a directory. Skipping...");
                    continue;
                }

                /** Do load process for each scenario file ***/

                processDirectory(currentSubDir);

            }
        } else {

            processDirectory(parentDirectory);

        }

        System.out.println("\nADL process successfully finished!");
        System.exit(0);
    }
}
