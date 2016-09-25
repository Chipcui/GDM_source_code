// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiclient.core.restmethods.dtopost.EnvelopeDtoRequest;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;

import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestContact {

    public PayloadEnvelope<ContactDTO> process(PayloadEnvelope<ContactDTO> payloadEnvelope) throws Exception {

        return new EnvelopeDtoRequest<ContactDTO>().processEnvelope(payloadEnvelope,
                ContactDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CONTACTS);
    }
}
