package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 7/11/2019.
 */
public class ListStatementAnalysisBrapiAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() { return ListSqlId.QUERY_ID_ANALYSES_ALL_BRAPI; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
        throws SQLException, GobiiException {

        String sql = "SELECT \n" +
                "a.analysis_id,\n" +
                "a.name as analysis_name,\n" +
                "a.created_date,\n" +
                "cv.term as type,\n" +
                "a.description,\n" +
                "a.program as software,\n" +
                "a.created_by,\n" +
                "a.modified_by,\n" +
                "a.modified_date\n" +
                "FROM\n" +
                "analysis a\n" +
                "LEFT OUTER JOIN cv\n" +
                "on cv.cv_id = a.type_id\n" +
                "order by a.analysis_id";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;

    }

}
