package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class SpGetDnaRunByDnaRunId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDnaRunByDnaRunId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "SELECT \n" +
                    "dr.dnarun_id, \n" +
                    "dr.experiment_id, \n" +
                    "dr.dnasample_id,\n" +
                    "dr.name,\n" +
                    "dr.code,\n" +
                    "dr.dataset_dnarun_idx,\n" +
                    "s.name as sample_name, \n" +
                    "g.germplasm_id, \n" +
                    "g.name as germplasm_name,  \n" +
                    "g.external_code as germplasm_external_code \n" +
                "FROM \n" +
                    "dnarun dr, dnasample s, germplasm g \n" +
                "WHERE \n" +
                    "dr.dnarun_id=?\n" +
                    "and dr.dnasample_id = s.dnasample_id \n" +
                    "and g.germplasm_id = s.germplasm_id\n";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer dnaRunId = (Integer) parameters.get("dnaRunId");
        preparedStatement.setInt(1, dnaRunId);
        resultSet = preparedStatement.executeQuery();
    }
}
