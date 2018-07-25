package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 */


public enum CvGroup {

    // corrrelate enum with known-cvgroup values from seed data
    UNKNOWN("unknown"),
    ANY("any"),
    JOBTYPE("job_type"),
    PAYLOADTYPE("payload_type"),
    JOBSTATUS("job_status"),
    ANALYSIS_TYPE("analysis_type"),
    DATASET_TYPE("dataset_type"),
    DNARUN_PROP("dnarun_prop"),
    DNASAMPLE_PROP("dnasample_prop"),
    PROJECT_PROP("project_prop"),
    GERMPLASM_PROP("germplasm_prop"),
    GERMPLASM_SPECIES("germplasm_species"),
    GERMPLASM_TYPE("germplasm_type"),
    GOBII_DATAWAREHOUSE("gobii_datawarehouse"),
    MAPSET_TYPE("mapset_type"),
    MARKER_PROP("marker_prop"),
    MARKER_STRAND("marker_strand"),
    PLATFORM_TYPE("platform_type"),
    STATUS("status");


    private String cvGroupName;

    CvGroup(String cvGroupName) {
        this.cvGroupName = cvGroupName;
    }

    public String getCvGroupName() {
        return this.cvGroupName;
    }

}
