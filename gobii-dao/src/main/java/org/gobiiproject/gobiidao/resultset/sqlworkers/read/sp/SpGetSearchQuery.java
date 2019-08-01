package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 7/30/2019.
 */
public class SpGetSearchQuery implements Work {

    private Map<String, Object> parameters = null;

    public SpGetSearchQuery(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "SELECT \n" +
                "query\n" +
                "FROM\n" +
                "search_query\n" +
                "WHERE\n" +
                "uuid=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        String uuid = (String) parameters.get("searchResultsDbId");
        preparedStatement.setString(1, uuid);
        resultSet = preparedStatement.executeQuery();

    }

}
