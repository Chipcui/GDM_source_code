package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.junit.Assert;

import java.util.concurrent.Callable;

/**
 * Created by VCalaminos on 1/30/2019.
 */
public class DtoCrudRequestProjectTestCallable implements Callable<Object> {

    private ProjectDTO projectDTO = null;

    public DtoCrudRequestProjectTestCallable(ProjectDTO projectDTO) throws Exception {
        this.projectDTO = projectDTO;
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }


    @Override
    public Object call() throws Exception {

        String message = null;

        String projectId = this.projectDTO.getProjectId().toString();

        String newCode  = "new code";
        projectDTO.setProjectCode(newCode);

        RestUri restUriProject = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(RestResourceId.GOBII_PROJECTS);
        restUriProject.setParamValue("id", projectId);

        PayloadEnvelope<ProjectDTO> postRequestEnvelope = new PayloadEnvelope<>(projectDTO, GobiiProcessType.UPDATE);
        GobiiEnvelopeRestResource<ProjectDTO, ProjectDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriProject);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResource
                .put(ProjectDTO.class, postRequestEnvelope);

        Header header = resultEnvelope.getHeader();
        if (!header.getStatus().isSucceeded() ||
                header
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(headerStatusMessage -> headerStatusMessage.getGobiiStatusLevel().equals(GobiiStatusLevel.VALIDATION))
                        .count() > 0) {
            message = "*** Header errors: ";
            for (HeaderStatusMessage currentStatusMessage : header.getStatus().getStatusMessages()) {
                message += currentStatusMessage.getMessage();
            }
        } else {

            ProjectDTO projectDTORetrieved = resultEnvelope.getPayload().getData().get(0);
            if (!projectDTORetrieved.getId().equals(projectDTO.getProjectId())) {
                message = "The project ID of the DTO did not match that of the URI parameter: " + projectId;
            }

            if (!projectDTORetrieved.getProjectCode().equals(newCode)) {
                message = "The code field of the retrieved DTO " + projectDTORetrieved.getProjectCode()
                        + " did not match the code it should have been updated to: " + newCode;
            }
        }

        return message;

    }

}
