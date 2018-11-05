package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;

import java.text.ParseException;
import java.util.List;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public interface DtoMapJob {

    List<JobDTO> getJobs() throws GobiiDtoMappingException;
    JobDTO getJobDetailsByJobName(String jobName) throws GobiiDtoMappingException;
    JobDTO createJob(JobDTO jobDTO) throws GobiiDtoMappingException, ParseException;
    JobDTO replaceJob(String jobName, JobDTO jobDTO) throws GobiiDtoMappingException;
    JobDTO getJobDetailsByDatasetId(Integer datasetId);

}
