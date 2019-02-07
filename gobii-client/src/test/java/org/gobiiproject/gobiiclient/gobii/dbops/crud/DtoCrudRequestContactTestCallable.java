package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ContactDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.junit.Assert;

import java.util.concurrent.Callable;

/**
 * Created by VCalaminos on 1/29/2019.
 */
public class DtoCrudRequestContactTestCallable implements Callable<Object> {

    private ContactDTO contactDTO = null;

    public DtoCrudRequestContactTestCallable(ContactDTO contactDTO) throws Exception {
        this.contactDTO = contactDTO;
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }


    @Override
    public Object call() throws Exception {

        String message = null;

        String userName = this.contactDTO.getUserName();

        String newCode = "new code";
        contactDTO.setCode(newCode);

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(RestResourceId.GOBII_CONTACTS);
        restUriContact.setParamValue("id", this.contactDTO.getId().toString());

        PayloadEnvelope<ContactDTO> postRequestEnvelope = new PayloadEnvelope<>(contactDTO, GobiiProcessType.UPDATE);
        GobiiEnvelopeRestResource<ContactDTO,ContactDTO> gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResourceForGet
                .put(ContactDTO.class, postRequestEnvelope);

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

            ContactDTO contactDTORetrieved = resultEnvelope.getPayload().getData().get(0);
            if (!contactDTORetrieved.getUserName().equals(userName)) {
                message = "The contact username of the DTO did not match that of the URI parameter: " + userName;
            }

            if (!contactDTORetrieved.getCode().equals(newCode)) {
                message = "The code field of the retrieved DTO " + contactDTORetrieved.getCode()
                        + " did not match the code it should have been updated to : " + newCode;
            }
        }

        return message;

    }

}
