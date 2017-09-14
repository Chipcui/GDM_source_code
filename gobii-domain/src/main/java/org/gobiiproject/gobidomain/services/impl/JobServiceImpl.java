package org.gobiiproject.gobidomain.services.impl;

import org.apache.commons.lang.time.DateUtils;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobidomain.services.JobService;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.DtoMapJob;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VCalaminos on 8/30/2017.
 */
public class JobServiceImpl implements JobService {

    Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private DtoMapJob dtoMapJob = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Override
    public JobDTO createJob(JobDTO jobDTO) throws GobiiDomainException {

        JobDTO returnVal;

        // check if the payload type of the job being submitted is a matrix
        // if it is a matrix, the datasetId of the JobDTO should not be empty

        if (jobDTO.getPayloadType().equals(JobDTO.CV_PAYLOADTYPE_MATRIX) && (null == jobDTO.getDatasetId())) {

            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Missing dataset ID for job: " +
                            jobDTO.getJobName() + " with payload type matrix.");

        }


        returnVal = dtoMapJob.createJob(jobDTO);

        if (returnVal.getPayloadType().equals(JobDTO.CV_PAYLOADTYPE_MATRIX)) {

            // get DatasetDTO

            DataSetDTO dataSetDTO = dataSetService.getDataSetById(jobDTO.getDatasetId());

            String[] datePattern = {"yyyy-MM-dd"};

            Date parsedDate;

            try {

                parsedDate = DateUtils.parseDateStrictly(dataSetDTO.getCreatedDate().toString(), datePattern);

            } catch (Exception e) {

                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Something went wrong with setting the createDate of the datasetDTO");
            }

            dataSetDTO.setCreatedDate(parsedDate);
            dataSetDTO.setJobId(jobDTO.getJobId());
            dataSetService.replaceDataSet(returnVal.getDatasetId(), dataSetDTO);

        }

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        return returnVal;
    }

    @Override
    public JobDTO replaceJob(String jobName, JobDTO jobDTO) throws GobiiDomainException {

        JobDTO returnVal;

        if (null == jobDTO.getJobName() || jobDTO.getJobName().equals(jobName)) {

            JobDTO existingJobDTO = dtoMapJob.getJobDetailsByJobName(jobName);

            if (null != existingJobDTO.getJobName() && existingJobDTO.getJobName().equals(jobName)) {

                returnVal = dtoMapJob.replaceJob(jobName, jobDTO);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The job name specified in the dto ("
                                + jobDTO.getJobName()
                                + ") does not match the job name passed as a parameter "
                                + "("
                                + jobName
                                + ")");

            }

        } else {

            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "The jobId specified in the dto ("
                            + jobDTO.getJobId()
                            + ") does not match the jobId passed as a parameter "
                            + "("
                            + jobName
                            + ")");

        }

        return returnVal;
    }

    @Override
    public List<JobDTO> getJobs() throws GobiiDomainException {

        List<JobDTO> returnVal;

        returnVal = dtoMapJob.getJobs();

        for (JobDTO currentJobDTO : returnVal) {

            currentJobDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentJobDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        if (null == returnVal) {

            returnVal = new ArrayList<>();

        }

        return returnVal;

    }

    @Override
    public JobDTO getJobByJobName(String jobName) throws GobiiDomainException {

        JobDTO returnVal;

        returnVal = dtoMapJob.getJobDetailsByJobName(jobName);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified jobId ("
                            + jobName
                            + ") does not match an existing job");

        }

        return returnVal;

    }


}
