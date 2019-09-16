package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class ListStatementMapsetBrapi implements ListStatement {

    @Override
    public ListSqlId getListSqlId() { return ListSqlId.QUERY_ID_MAPSET_BRAPI_BYLIST; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
        throws SQLException, GobiiException {

        String pageSizeCondition = "";

        Integer pageSize = 0;
        Integer pageNumber = 0;

        if (sqlParamVals != null) {
            if (sqlParamVals.containsKey("pageSize")
                    && sqlParamVals.get("pageSize") instanceof Integer) {

                pageSize = (Integer) sqlParamVals.getOrDefault("pageSize", 0);

                if (pageSize > 0) {

                    pageSizeCondition = "LIMIT ? ";

                    if (sqlParamVals.containsKey("pageNum")
                            && sqlParamVals.get("pageNum") instanceof Integer) {

                        pageNumber = (Integer) sqlParamVals.getOrDefault("pageNum", 0);

                        if (pageNumber > 0) {

                            pageSizeCondition += "OFFSET ?";

                        }
                    }
                }
                else {
                    pageSizeCondition = "";
                }
            }
        }

        String sql = "WITH mapset_paged AS (" +
                "SELECT * FROM mapset "+pageSizeCondition+") " +
                "SELECT mapset.mapset_id AS mapset_id, mapset.name AS name, cv.term AS type, mapset.description, " +
                "COUNT(DISTINCT linkage_group_id)::INT linkage_group_count, " +
                "COUNT(marker_id)::INT marker_count " +
                "FROM mapset_paged AS mapset " +
                "LEFT JOIN cv ON(cv.cv_id = mapset.type_id AND cv.cvgroup_id = (" +
                    "SELECT cv.cv_id FROM cv " +
                    "INNER JOIN cvgroup USING(cvgroup_id) " +
                    // cvgroup.type = 1 and denotes it is a system property. No relationship that defines cvgroup type
                    // was found, so hard coding it as 1
                    "WHERE cvgroup.name LIKE ? AND cvgroup.type = 1 LIMIT 1" +
                ")) " +
                "LEFT JOIN linkage_group ON(mapset.mapset_id = linkage_group.map_id) " +
                "LEFT JOIN marker_linkage_group USING(linkage_group_id) " +
                "GROUP BY mapset_id, mapset.name, mapset.description, cv.term;";


        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        if(pageSize > 0) {
            returnVal.setInt(1, pageSize);
            if(pageNumber > 0) {
                returnVal.setInt(2, (pageNumber-1)*pageSize);
                returnVal.setString(3, CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName());
            }
            else {
                returnVal.setString(2, CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName());
            }
        }
        else {
            returnVal.setString(1, CvGroup.CVGROUP_MAPSET_TYPE.getCvGroupName());
        }

        return returnVal;
    }
}
