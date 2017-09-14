package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.JobDTO;

import java.text.ParseException;
import java.util.List;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public interface DtoMapJob {

    List<JobDTO> getJobs() throws GobiiDtoMappingException;
    JobDTO getJobDetailsByJobName(String jobName) throws GobiiDtoMappingException;
    JobDTO createJob(JobDTO jobDTO) throws GobiiDtoMappingException;
    JobDTO replaceJob(String jobName, JobDTO jobDTO) throws GobiiDtoMappingException;
    JobDTO getJobDetailsByDatasetId(Integer datasetId);

}
