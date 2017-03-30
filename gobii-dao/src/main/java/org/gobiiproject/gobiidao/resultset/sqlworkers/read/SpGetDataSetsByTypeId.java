package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 2/7/2017.
 */
public class SpGetDataSetsByTypeId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDataSetsByTypeId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return  resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select dataset_id,\n" +
            "experiment_id,\n" +
            "callinganalysis_id,\n" +
            "analyses,\n" +
            "data_table,\n" +
            "data_file,\n" +
            "quality_table,\n" +
            "quality_file,\n" +
            "scores,\n" +
            "created_by,\n" +
            "created_date,\n" +
            "modified_by,\n" +
            "modified_date,\n" +
            "status,\n" +
            "type_id,\n" +
            "name::text\n" +
            "from dataset where type_id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        Integer typeId = (Integer) parameters.get("typeId");
        preparedStatement.setInt(1, typeId);

        resultSet = preparedStatement.executeQuery();

    }// execute ()
}
