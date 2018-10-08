package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.RestRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;


/**
 * Encapsulate calling and updating status.
 *
 * Note: Status is created when the instruction file is, and the base elements are populated there.
 * This is important * If you're making your own jobs, you need to create a Status and deal with it there.*
 */
public class JobStatus {
    /**
     * Creates a new job object based on information about the job already in progress.
     * @param config
     * @param cropName
     * @param jobName the name of the instruction file, minus the .json suffix
     */
    GobiiUriFactory uriFactory;
    String jobName;
	JobDTO lastStatus;
	/**List of valid statuses. Update as appropriate. Stops 'random argument' assignment, even though Progress Status
	 * has been stringly typed.
	 */
	private static Set<String> acceptedStatuses=new HashSet<>(Arrays.asList(
			JobProgressStatusType.CV_PROGRESSSTATUS_ABORTED.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_DIGEST.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_INPROGRESS.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_MATRIXLOAD.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_METADATALOAD.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_TRANSFORMATION.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_FAILED.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_METADATAEXTRACT.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_FINALASSEMBLY.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_QCPROCESSING.getCvName()
	));
    public JobStatus(ConfigSettings config, String cropName, String jobName) throws Exception {
		this.jobName=jobName;
		// set up authentication and so forth
		// you'll need to get the current from the instruction file
		GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);
		uriFactory = context.getUriFactory();
    }

    public void set(String status,String message){
    	if(status==null || !acceptedStatuses.contains(status)){
    		ErrorLogger.logError("JobStatus","Invalid status passed to set: "+status+"\nMessage: "+message,new Exception());//passing a new exception throws a stack trace in there
		}
            try{
                RestUri restUri=uriFactory
                        .resourceByUriIdParam(RestRequestId.URL_JOB);
                restUri.setParamValue("id", jobName);
            GobiiEnvelopeRestResource<JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
            PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource
                    .get(JobDTO.class);

				JobDTO dataSetResponse;
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                System.out.println();
                logError("Digester", "Job table response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
			}
				List<JobDTO> responses = resultEnvelope.getPayload().getData();
				if(responses.size()==0){
					logError("JobStatus","No Job record returned for job " + jobName);
					return;
				}
				dataSetResponse = responses.get(0);


				dataSetResponse.setMessage(message);
				dataSetResponse.setStatus(status);

				resultEnvelope = gobiiEnvelopeRestResource
						.put(JobDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));

				//Set 'lastStatus' to the current status
				lastStatus = dataSetResponse;
				// if you didn't succeed, do not pass go, but do log errors to your log file
				if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
					logError("Digester", "Data set response response errors");
					for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
						logError("HeaderError", currentStatusMesage.getMessage());
					}
					return;
				}
			} catch (Exception e) {
				logError("Digester", "Exception while referencing Job table in Postgresql", e);
				return;
			}
	}

	/**
     * Sets the status of the job underway. Adds a message based on the error automatically.
     *
     */
	public void setError(String message){
        String errorMessage="";
        if(lastStatus!=null){
            errorMessage="Status: " + lastStatus.getStatus()+" - " + lastStatus.getMessage() + " | \n";
        }
        errorMessage += message + " : " + ErrorLogger.getFirstErrorReason();
        set(JobProgressStatusType.CV_PROGRESSSTATUS_FAILED.getCvName(),errorMessage);
    }
}