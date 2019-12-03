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
import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DNASAMPLE_NAMES_BY_UUIDLIST;

/**
 * Created by VCalaminos on 9/18/2018.
 */
public class ListStatementDnaSampleNamesByUuidList implements ListStatement {

    private final String PARAM_NAME_NAME_LIST = "nameArray";

    @Override
    public ListSqlId getListSqlId() { return QUERY_ID_DNASAMPLE_NAMES_BY_UUIDLIST; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        List<NameIdDTO> nameArray = (ArrayList) sqlParamVals.get(PARAM_NAME_NAME_LIST);

        // parse array into csv

        String parsedNameList = ListStatementUtil.generateParsedUuidList(nameArray);

        ParameterizedSql parameterizedSql =
                new ParameterizedSql("select dnasample_id, name, num, uuid" +
                        " from dnasample " +
                        " where uuid IN ("+ PARAM_NAME_NAME_LIST+ ")",
                        new HashMap<String, String>(){
                            {
                                put(PARAM_NAME_NAME_LIST, null);
                            }
                        });

        String sql = parameterizedSql
                .setParamValue(PARAM_NAME_NAME_LIST, parsedNameList)
                .getSql();

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);


        return returnVal;
    }

}
