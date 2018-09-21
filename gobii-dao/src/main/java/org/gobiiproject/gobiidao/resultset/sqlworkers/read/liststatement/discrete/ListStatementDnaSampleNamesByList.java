package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiidao.resultset.core.listquery.ParameterizedSql;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DNASAMPLE_NAMES_BYLIST;

/**
 * Created by VCalaminos on 9/18/2018.
 */
public class ListStatementDnaSampleNamesByList implements ListStatement {

    private final String PARAM_NAME_NAME_LIST = "nameArray";
    private final String PARAM_NAME_PROJECT_ID = "projectId";

    @Override
    public ListSqlId getListSqlId() { return QUERY_ID_DNASAMPLE_NAMES_BYLIST; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String parsedNameList = "";

        List<NameIdDTO> nameArray = (ArrayList) sqlParamVals.get(PARAM_NAME_NAME_LIST);

        // parse array into csv

        for (NameIdDTO nameIdDTO : nameArray) {

            String quotedName = "'" + nameIdDTO.getName() + "'";

            parsedNameList = (parsedNameList.equals("")) ? quotedName : parsedNameList + ", " + quotedName;
        }

        ParameterizedSql parameterizedSql =
                new ParameterizedSql("select dnasample_id, name " +
                        "from dnasample " +
                        "where name in (" + PARAM_NAME_NAME_LIST + ") " +
                        "and project_id::varchar = ?",
                        new HashMap<String, String>(){
                            {
                                put(PARAM_NAME_NAME_LIST, null);
                            }
                        });

        String sql = parameterizedSql
                .setParamValue(PARAM_NAME_NAME_LIST, parsedNameList)
                .getSql();

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        String projectId = (String) jdbcParamVals.get(PARAM_NAME_PROJECT_ID);
        returnVal.setString(1, projectId);

        return returnVal;
    }

}
