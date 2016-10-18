package org.gobiiproject.gobiiclient.core.restmethods.dtopost;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiapimodel.restresources.ResourceBuilder;

import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class EnvelopeDtoRequest<T> {

    Logger LOGGER = LoggerFactory.getLogger(EnvelopeDtoRequest.class);


    public PayloadEnvelope<T> processEnvelope(PayloadEnvelope<T> payloadEnvelope,
                                              Class<T> DtoType,
                                              ControllerType controllerType,
                                              ServiceRequestId requestId) throws Exception {

        String token = ClientContext.getInstance(null, false).getUserToken();
        String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();
        String cropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();

        PayloadEnvelope<T> returnVal = null;

        EnvelopeRestRequest<T> envelopeRestRequest= new EnvelopeRestRequest<>(host,
                port,
                cropContextRoot,
                DtoType);


        if (null == token || token.isEmpty()) {
            throw (new Exception("there is no user token; user must log in"));
        }

        String url = ResourceBuilder.getRequestUrl(controllerType,
                cropContextRoot,
                requestId);



        returnVal = envelopeRestRequest.getTypedHtppResponseForDtoEnvelope(url,
                payloadEnvelope,
                token);

        return returnVal;
    }

}