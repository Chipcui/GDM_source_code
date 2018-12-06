package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 11/5/2018.
 */
public class SpGetNameIdsForAnalysisNames implements Work {

    private Map<String, Object> parameters = null;

    public SpGetNameIdsForAnalysisNames(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select analysis_id, name from analysis order by lower(name) limit ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        int callLimit = (int) parameters.get("callLimit");
        preparedStatement.setInt(1,callLimit);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
