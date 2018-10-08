package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 5/18/2016.
 */
public enum GobiiServerType {
    // these must correspond to the jdbc driver name
    GOBII_PGSQL,
    GOBII_WEB,
    GOBII_COMPUTE,
    KDC,
    GENERIC,
    OWN_CLOUD,
    UNKNOWN,
    LDAP
}
