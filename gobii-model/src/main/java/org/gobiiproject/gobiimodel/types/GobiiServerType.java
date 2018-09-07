package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 5/18/2016.
 */
public enum GobiiServerType {
    // these must correspond to the jdbc driver name
    POSTGRESQL,
    WEB,
    COMPUTE_NODE,
    KDC,
    GENERIC,
    UNKNOWN
}
