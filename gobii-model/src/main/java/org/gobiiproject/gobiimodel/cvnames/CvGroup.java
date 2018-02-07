package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 */


public enum CvGroup {

    CVGROUP_JOBTYPE("job_type"),
    CVGROUP_PAYLOADTYPE("payload_type"),
    CVGROUP_JOBSTATUS("job_status"),
    CVGROUP_DATASETTYPES("dataset_type");

    private String cvGroupName;

    CvGroup(String cvGroupName) {
        this.cvGroupName = cvGroupName;
    }

    public String getCvGroupName() {
        return this.cvGroupName;
    }

}
