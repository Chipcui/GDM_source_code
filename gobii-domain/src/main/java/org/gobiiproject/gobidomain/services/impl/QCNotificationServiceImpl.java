package org.gobiiproject.gobidomain.services.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.JsonObject;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.QCNotificationService;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.common.RestResourceUtils;
import org.gobiiproject.gobiidtomapping.DtoMapQCData;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class QCNotificationServiceImpl implements QCNotificationService {

    private Logger LOGGER = LoggerFactory.getLogger(QCNotificationServiceImpl.class);

    @Autowired
    private DtoMapQCData dtoMapQCData = null;

    @Override
    public void createQCData(List<QCDataDTO> qcDataDTOsList,
                             ConfigSettings configSettings,
                             String cropType) throws GobiiException {

        try {
            // Each one of the QCDataDTO items has the same QC job id
            // First we need to know their QC status
            // Then we copy the QC output files from their original directories to the user directories
            Long qcJobID = qcDataDTOsList.get(0).getContactId();
            ClientContext clientContext = ClientContext.getInstance(configSettings, cropType, GobiiAutoLoginType.USER_RUN_AS);
            if (LineUtils.isNullOrEmpty(clientContext.getUserToken())) {
                ErrorLogger.logError("QC Notification Service","Unable to log in with user: " + GobiiAutoLoginType.USER_RUN_AS.toString());
                return;
            }
            String currentQCContextRoot = clientContext.getInstance(null, false).getCurrentQCContextRoot();
            UriFactory uriFactory = new UriFactory(currentQCContextRoot);
            RestUri restUri = uriFactory.qcStatus();
            restUri.setParamValue("jobid", String.valueOf(qcJobID));
            RestResourceUtils restResourceUtils = new RestResourceUtils();
            HttpMethodResult httpMethodResult = restResourceUtils.getClientContext().getHttp().get(restUri, clientContext.getUserToken());
            JsonObject jsonObject = httpMethodResult.getPayLoad();
            if (jsonObject == null) {
                throw new GobiiDomainException("No JSON object");
            }
            else {
                if (!(jsonObject.has("status"))) {
                    throw new GobiiDomainException("No status data");
                }
                else {
                    String status = jsonObject.get("status").toString();
                    ErrorLogger.logInfo("Extractor","----> status: " + status + " <----");
                    if (status.toLowerCase().equals("completed")) {
                        String qcNotificationsDirectory = configSettings.getProcessingPath(cropType,
                                                                                           GobiiFileProcessDir.QC_NOTIFICATIONS);
                        // Each one of the QCDataDTO items has the same dataset id
                        Long datasetId = qcDataDTOsList.get(0).getDataSetId();
                        // The new QC data directory must have an unique timestamp for all the QCDataDTO items
                        String newQCDataDirectory = new StringBuilder("ds_")
                                .append(datasetId)
                                .append("_")
                                .append(DateUtils.makeDateIdString()).toString();
                        Path newQCDataDirectoryPath = Paths.get(qcNotificationsDirectory, newQCDataDirectory);
                        dtoMapQCData.createData(qcDataDTOsList, qcJobID, newQCDataDirectoryPath.toString());
                    }
                    else {
                        throw new GobiiDomainException("Job ID " + qcJobID + " not completed, its status: " + status);
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }
}
