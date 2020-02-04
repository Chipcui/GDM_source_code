package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlleleMatricesSearchDTO {


    private List<String> markerProfileDbId;

    public List<String> getMarkerProfileDbId() {
        return markerProfileDbId;
    }

    public void setMarkerProfileDbId(List<String> markerProfileDbId) {
        this.markerProfileDbId = markerProfileDbId;
    }

    public List<String> getMatrixDbId() {
        return matrixDbId;
    }

    public void setMatrixDbId(List<String> matrixDbId) {
        this.matrixDbId = matrixDbId;
    }

    private List<String> matrixDbId;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private String format;

}
