package org.gobiiproject.gobiimodel.types;

/**
 * Created by Phil on 10/16/2016.
 */
public enum GobiiVertexType {
    UNKNOWN,
    ENTITY, // values correspond to a column from a table
    SUBENTITY, // values correspond to a column from a table filtered to sub type (e.g., contacts that are only of role principle investigator)
    CVGROUP, // the values are all of those within a specific cv group
    CVTERM // all values that have been assigned to the property of an entity, where the property's key is a cv term
}
