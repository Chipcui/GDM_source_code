package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 * These are intended to codify cv term names. You cannot add new values unless there exists a corresponding cv term in the database
 *
 */


public enum VertexNameType {

    VERTEX_TYPE_PROJECT("project"),
    VERTEX_TYPE_SAMPLING_DATE("sampling_date"),
    VERTEX_TYPE_GENOTYPING_PURPOSE("genotyping_purpose"),
    VERTEX_TYPE_DIVISION("division"),
    VERTEX_TYPE_TRIAL_NAME("trial_name"),
    VERTEX_TYPE_EXPERIMENT("experiment"),
    VERTEX_TYPE_DATASET("dataset"),
    VERTEX_TYPE_DATASET_TYPE("dataset_type"),
    VERTEX_TYPE_ANALYSIS("analysis"),
    VERTEX_TYPE_ANALYSIS_TYPE("analysis_type"),
    VERTEX_TYPE_REFERENCE_SAMPLE("reference_sample"),
    VERTEX_TYPE_PLATFORM("platform"),
    VERTEX_TYPE_VENDOR("vendor"),
    VERTEX_TYPE_PROTOCOL("protocol"),
    VERTEX_TYPE_VENDOR_PROTOCOL("vendor_protocol"),
    VERTEX_TYPE_MAPSET("mapset"),
    VERTEX_TYPE_MAPSET_TYPE("mapset_type"),
    VERTEX_TYPE_LINKAGE_GROUP("linkage_group"),
    VERTEX_TYPE_GERMPLAM_SUBSPECIES("germplasm_subspecies"),
    VERTEX_TYPE_GERMPLASM_SPECIES("germplasm_species"),
    VERTEX_TYPE_GERMPLASM_TYPE("germplasm_type"),
    VERTEX_TYPE_PRINCIPLE_INVESTIGATOR("principal_investigator"),
    VERTEX_TYPE_MARKER("marker"),
    VERTEX_TYPE_DNASAMPLE("dnasample");

    private String vertexName;

    VertexNameType(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getVertexName() {
        return this.vertexName;
    }

    public static VertexNameType byValue(String val) {

        VertexNameType returnVal = null;

        for (VertexNameType currentVertexNameType : values()) {
            if (currentVertexNameType.getVertexName().equals(val)) {
                returnVal =  currentVertexNameType;
                break;
            }
        }

        return returnVal;

    } // end byValue()
} // end enum
