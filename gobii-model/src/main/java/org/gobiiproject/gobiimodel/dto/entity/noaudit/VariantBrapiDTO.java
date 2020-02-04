package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiimodel.entity.Reference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType", "datasetMarkerIndex"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariantBrapiDTO extends DTOBase {


    @GobiiEntityMap(paramName="alts", entity = Marker.class)
    private String[] alternateBases;

    private List<Integer> ciend = new ArrayList<>();

    private List<Integer> cipos = new ArrayList<>();

    private boolean filtersApplied;

    private List<String> filtersFailed = new ArrayList<>();

    private List<String> filtersPassed;

    @GobiiEntityMap(paramName="referenceName", entity = Reference.class, deep=true)
    private String referenceName;

    @GobiiEntityMap(paramName="ref", entity = Marker.class)
    private String referenceBases;

    @GobiiEntityMap(paramName="platformName", entity = Platform.class, deep=true)
    private String platformName;

    private String svlen;

    @GobiiEntityMap(paramName="markerId", entity = Marker.class)
    private int variantDbId;

    private List<String> variantNames = new ArrayList<>();

    private List<String> variantSetDbId = new ArrayList<>();


    private String variantType = "marker";

    private BigDecimal start;

    protected BigDecimal stop;

    public List<Integer> getCiend() {
        return ciend;
    }

    public void setCiend(List<Integer> ciend) {
        this.ciend = ciend;
    }

    public List<Integer> getCipos() {
        return cipos;
    }

    public void setCipos(List<Integer> cipos) {
        this.cipos = cipos;
    }

    public boolean isFiltersApplied() {
        return filtersApplied;
    }

    public void setFiltersApplied(boolean filtersApplied) {
        this.filtersApplied = filtersApplied;
    }

    public List<String> getFiltersFailed() {
        return filtersFailed;
    }

    public void setFiltersFailed(List<String> filtersFailed) {
        this.filtersFailed = filtersFailed;
    }

    public List<String> getFiltersPassed() {
        return filtersPassed;
    }

    public void setFiltersPassed(List<String> filtersPassed) {
        this.filtersPassed = filtersPassed;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferenceBases() {
        return referenceBases;
    }

    public void setReferenceBases(String referenceBases) {
        this.referenceBases = referenceBases;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getSvlen() {
        return svlen;
    }

    public void setSvlen(String svlen) {
        this.svlen = svlen;
    }

    public int getVariantDbId() {
        return variantDbId;
    }

    public void setVariantDbId(int variantDbId) {
        this.variantDbId = variantDbId;
    }

    public List<String> getVariantNames() {
        return variantNames;
    }

    public void setVariantNames(List<String> variantNames) {
        this.variantNames = variantNames;
    }


    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public BigDecimal getStart() {
        return start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getStop() {
        return stop;
    }

    public void setStop(BigDecimal stop) {
        this.stop = stop;
    }

    public List<String> getVariantSetDbId() {
        return variantSetDbId;
    }

    public void setVariantSetDbId(List<String> variantSetDbId) {
        this.variantSetDbId = variantSetDbId;
    }

    public String[] getAlternateBases() {
        return alternateBases;
    }

    public void setAlternateBases(String[] alternateBases) {
        this.alternateBases = alternateBases;
    }

    @Override
    public Integer getId() {
        return this.variantDbId;
    }

    @Override
    public void setId(Integer id) {
        this.variantDbId = id;
    }


}
