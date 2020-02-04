package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
        "dataFilePath"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentDTO extends DTOBaseAuditable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GobiiEntityMap(paramName="experimentId", entity = Experiment.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private int experimentId;

    @GobiiEntityMap(paramName="experimentName", entity= Experiment.class)
    private String experimentName;

    @GobiiEntityMap(paramName="experimentCode", entity= Experiment.class)
    private String experimentCode;

    @GobiiEntityMap(paramName="dataFile", entity= Experiment.class)
    private String dataFilePath;

    @GobiiEntityMap(paramName="projectId", entity= Experiment.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer projectId;

    @GobiiEntityMap(paramName="vendorProtocolId", entity= Experiment.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer vendorProtocolId;

    @GobiiEntityMap(paramName="manifestId", entity= Experiment.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer manifestId;

    //Mapped by cvStatus
    @GobiiEntityMap(paramName="status.term", entity = Project.class, deep=true)
    private String experimentStatus;

    private String dataFileUrl;

    public ExperimentDTO() { super(GobiiEntityNameType.EXPERIMENT); }

    @Override
    public Integer getId() { return this.experimentId; }

    @Override
    public void setId(Integer id) { this.experimentId = id; }

    public Integer getExperimentId() { return this.experimentId; }

    public void setExperimentId(Integer id) { this.experimentId = id; }

    public String getExperimentName() { return this.experimentName; }

    public void setExperimentName(String name) { this.experimentName = name; }

    public String getCode() { return this.experimentCode; }

    public void setCode(String code) { this.experimentCode = code; }

    public String getDataFilePath() { return this.dataFilePath; }

    public void setDataFilePath(String dataFile) { this.dataFilePath = dataFile; }

    public String getDataFileUrl() { return this.dataFileUrl; }

    public void setDataFileUrl(String dataFileUrl) { this.dataFileUrl = dataFileUrl; }

    public Integer getProjectId() { return this.projectId; }

    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public Integer getVendorProtocolId() { return this.vendorProtocolId; }

    public void setVendorProtocolId(Integer vendorProtocolId) { this.vendorProtocolId = vendorProtocolId; }

    public Integer getManifestId() { return this.manifestId; }

    public void setManifestId(Integer manifestId) { this.manifestId = manifestId; }

    public String getExperimentStatus() { return this.experimentStatus; }

    public void setExperimentStatus(String status) { this.experimentStatus = status; }

}
