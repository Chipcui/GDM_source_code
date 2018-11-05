package org.gobiiproject.gobiimodel.cvnames;

/**
 * Created by Phil on 5/13/2016.
 * These are intended to codify cv term names. You cannot add new values unless there exists a corresponding cv term in the database
 *
 */


public enum JobPayloadType {

    CV_PAYLOADTYPE_SAMPLES ("samples"),
    CV_PAYLOADTYPE_SAMPLE_KEYS ("samplekeys"),
    CV_PAYLOADTYPE_MARKERS ("markers"),
    CV_PAYLOADTYPE_MARKERMAP ("markermap"),
    CV_PAYLOADTYPE_MARKER_KEYS ("markerkeys"),
    CV_PAYLOADTYPE_MATRIX ("matrix"),
    CV_PAYLOADTYPE_MARKERSAMPLES ("marker_samples"),
    CV_PAYLOADTYPE_ALLMETA ("all_meta");

    private String cvName;

    JobPayloadType(String cvName) {
        this.cvName = cvName;
    }

    public String getCvName() {
        return this.cvName;
    }

    public static JobPayloadType byValue(String val) {
        for (JobPayloadType en : values()) {
            if (en.getCvName().equals(val)) {
                return en;
            }
        }
        return null;
    }

}
