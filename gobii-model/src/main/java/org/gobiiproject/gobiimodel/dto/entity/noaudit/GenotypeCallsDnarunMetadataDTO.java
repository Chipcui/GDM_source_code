package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenotypeCallsDnarunMetadataDTO extends DTOBase{

    private Integer dnarunId;

    private String dnarunName;


    private Map<String, Object> datasetDnarunIdx;


    @Override
    public Integer getId() { return null; }

    @Override
    public void setId(Integer id) { this.dnarunId = null; }

    public Integer getDnarunId() { return this.dnarunId; }

    @GobiiEntityColumn(columnName = "dnarun_id")
    public void setDnarunId(Integer id) { this.dnarunId = id; }

    public String getDnarunName() { return this.dnarunName; }

    @GobiiEntityColumn(columnName = "dnarun_name")
    public void setDnarunName(String dnarunName) { this.dnarunName = dnarunName; }


    public String getHdf5DnarunIdx(String datasetId) {
        return (String)this.datasetDnarunIdx.getOrDefault(datasetId, null);

    }

    @GobiiEntityColumn(columnName = "dataset_dnarun_idx")
    public void setDatasetDnarunIndex(Map<String, Object> datasetDnarunIndex) {
        this.datasetDnarunIdx = datasetDnarunIndex;
    }

    public Map<String, Object> getDatasetDnarunIndex() {
        return this.datasetDnarunIdx;
    }

}
