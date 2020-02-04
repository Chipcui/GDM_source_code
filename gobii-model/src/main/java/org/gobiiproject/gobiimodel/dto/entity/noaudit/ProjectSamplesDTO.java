package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSamplesDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer projectId;

    public List<DnaSampleDTO> samples;


    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<DnaSampleDTO> getSamples() {
       return this.samples;
    }

    public void setSamples(List<DnaSampleDTO> sampleList) {
        this.samples = sampleList;
    }
}
