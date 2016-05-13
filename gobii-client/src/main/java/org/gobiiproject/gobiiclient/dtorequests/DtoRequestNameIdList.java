// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import com.google.gson.JsonObject;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.core.TypedRestRequest;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;


public class DtoRequestNameIdList {

    private final String JSON_PROP_NAMESBYID = "namesById";
    private final String JSON_PROP_ENTITYNAME = "entityName";
    private final String JSON_PROP_FILTER = "filter";


    public NameIdListDTO getContactsById(String Type) throws Exception {

        NameIdListDTO returnVal = null;


        JsonObject ContactsByRoleJson = new JsonObject();
        ContactsByRoleJson.add(JSON_PROP_NAMESBYID, new JsonObject());
        ContactsByRoleJson.addProperty(JSON_PROP_ENTITYNAME, "contact");
        ContactsByRoleJson.addProperty(JSON_PROP_FILTER, Type);

        TypedRestRequest<NameIdListDTO> typedRestRequest = new TypedRestRequest<>(NameIdListDTO.class);

//        SystemUsers systemUsers = new SystemUsers();
//        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
//        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        String token = ClientContext.getInstance().getUserToken();


        returnVal = typedRestRequest.getTypedHtppResponse(Urls.URL_NAME_ID_LIST, ContactsByRoleJson, token);

        return returnVal;

    } // getContactsById()


    public NameIdListDTO getProjectNamesById(Integer contactId) throws Exception {

        NameIdListDTO returnVal = null;


        JsonObject projectNamesByContactJson = new JsonObject();
        projectNamesByContactJson.add(JSON_PROP_NAMESBYID, new JsonObject());
        projectNamesByContactJson.addProperty(JSON_PROP_ENTITYNAME, "project");
        projectNamesByContactJson.addProperty(JSON_PROP_FILTER, contactId.toString());

        TypedRestRequest<NameIdListDTO> typedRestRequest = new TypedRestRequest<>(NameIdListDTO.class);

//        SystemUsers systemUsers = new SystemUsers();
//        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
//        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        String token = ClientContext.getInstance().getUserToken();

        returnVal = typedRestRequest.getTypedHtppResponse(Urls.URL_NAME_ID_LIST, projectNamesByContactJson, token);

        return returnVal;

    } // getContactsById()
    
    public NameIdListDTO getExperimentNamesById(Integer projectId) throws Exception {

        NameIdListDTO returnVal = null;


        JsonObject experimentNamesByProjectJson = new JsonObject();
        experimentNamesByProjectJson.add(JSON_PROP_NAMESBYID, new JsonObject());
        experimentNamesByProjectJson.addProperty(JSON_PROP_ENTITYNAME, "experiment");
        experimentNamesByProjectJson.addProperty(JSON_PROP_FILTER, projectId.toString());

        TypedRestRequest<NameIdListDTO> typedRestRequest = new TypedRestRequest<>(NameIdListDTO.class);

//        SystemUsers systemUsers = new SystemUsers();
//        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
//        String token = typedRestRequest.getTokenForUser(userDetail.getUserName(), userDetail.getPassword());

        String token = ClientContext.getInstance().getUserToken();

        returnVal = typedRestRequest.getTypedHtppResponse(Urls.URL_NAME_ID_LIST, experimentNamesByProjectJson, token);

        return returnVal;

    } // getContactsById()


    
    public NameIdListDTO getNamesById(NameIdListDTO nameIdListDTORequest) throws Exception {

        return new DtoRequestProcessor<NameIdListDTO>().process(nameIdListDTORequest,
                NameIdListDTO.class,
                Urls.RequestId.URL_NAME_ID_LIST);

    } // getContactsById()

}
