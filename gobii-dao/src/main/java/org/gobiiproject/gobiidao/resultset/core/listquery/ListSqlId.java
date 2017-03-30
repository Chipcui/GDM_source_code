package org.gobiiproject.gobiidao.resultset.core.listquery;

/**
 * Created by Phil on 10/25/2016.
 */
public enum ListSqlId {
    QUERY_ID_DATASET_ALL("select dataset_id,\n" +
            " experiment_id,\n" +
            " callinganalysis_id,\n" +
            " analyses,\n" +
            " data_table,\n" +
            " data_file,\n" +
            " quality_table,\n" +
            " quality_file,\n" +
            " scores,\n" +
            " created_by,\n" +
            " created_date,\n" +
            " modified_by,\n" +
            " modified_date,\n" +
            " status,\n" +
            " type_id,\n" +
            " name::text from dataset order by lower(name)"),
    QUERY_ID_CONTACT_ALL("select contact_id,\n" +
            " lastname::text,\n" +
            " firstname::text,\n" +
            " code,\n" +
            " email::text,\n" +
            " roles,\n" +
            " created_by,\n" +
            " created_date,\n" +
            " modified_by,\n" +
            " modified_date,\n" +
            " organization_id,\n" +
            " username from contact order by lower(lastname),lower(firstname)"),
    QUERY_ID_ORGANIZATION_ALL("select  organization_id,\n" +
            " name::text,\n" +
            " address,\n" +
            " website,\n" +
            " created_by,\n" +
            " created_date,\n" +
            " modified_by,\n" +
            " modified_date,\n" +
            " status from organization order by lower(name)"),
    QUERY_ID_PLATFORM_ALL("select platform_id,\n" +
            " name::text,\n" +
            " code,\n" +
            " description,\n" +
            " created_by,\n" +
            " created_date,\n" +
            " modified_by,\n" +
            " modified_date,\n" +
            " status,\n" +
            " type_id,\n" +
            " props from platform order by lower(name)"),
    QUERY_ID_PROJECT_ALL("select  project_id,\n" +
            " name::text,\n" +
            " code,\n" +
            " description,\n" +
            " pi_contact,\n" +
            " created_by,\n" +
            " created_date,\n" +
            " modified_by,\n" +
            " modified_date,\n" +
            " status,\n" +
            " props from project order by lower(name)"),
    QUERY_ID_EXPERIMENT("select experiment_id,\n" +
            "name::text,\n" +
            "code,\n" +
            "project_id,\n" +
            "manifest_id,\n" +
            "data_file,\n" +
            "created_by,\n" +
            "created_date,\n" +
            "modified_by,\n" +
            "modified_date,\n" +
            "status,\n" +
            "vendor_protocol_id\n" +
            "from experiment e\n" +
            "order by lower(e.name)"),
    QUERY_ID_MARKER_ALL("select m.marker_id,\n" +
            "p.platform_id,\n" +
            "m.variant_id, \n" +
            "m.name::text \"marker_name\", \n" +
            "m.code, \n" +
            "m.ref, \n" +
            "m.alts, \n" +
            "m.sequence, \n" +
            "m.reference_id, \n" +
            "m.strand_id, \n" +
            "m.status, \n" +
            "p.name::text \"platform_name\"\n" +
            "from marker m\n" +
            "join platform p on (m.platform_id=p.platform_id)\n" +
            "order by lower(m.name)"),
    QUERY_ID_PROTOCOL_ALL("select protocol_id,\n" +
            " name::text,\n" +
            " description,\n" +
            " type_id,\n" +
            " platform_id,\n" +
            " props,\n" +
            " created_by,\n" +
            " created_date,\n" +
            " modified_by,\n" +
            " modified_date,\n" +
            " status from protocol order by lower(name)");

    private String sql;

    ListSqlId(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
