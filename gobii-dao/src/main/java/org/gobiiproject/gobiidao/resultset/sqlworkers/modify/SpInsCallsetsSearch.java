package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.hibernate.jdbc.Work;
import org.postgresql.util.PGobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 7/29/2019.
 */
public class SpInsCallsetsSearch implements Work {

    private Map<String, Object> parameters = null;

    public SpInsCallsetsSearch(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "insert into search_query(uuid, query, type) " +
                "values (?,?,?) returning uuid";

        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue((String) parameters.get("search_query"));

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setString(1, (String) parameters.get("uuid"));
        preparedStatement.setObject(2, jsonObject);
        preparedStatement.setString(3, (String) parameters.get("type"));

        resultSet = preparedStatement.executeQuery();
    }

}
