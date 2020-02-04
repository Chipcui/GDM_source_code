package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Model for JobStatus for sample tracking endpoints.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
        "datasetIds"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobStatusDTO extends JobDTO {

    public JobStatusDTO() {}

    private String JobStatusLink;

    public String getJobStatusLink() {
        return JobStatusLink;
    }

    public void setJobStatusLink(String jobStatusLink) {
        JobStatusLink = jobStatusLink;
    }
}
