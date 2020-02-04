package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 7/12/2019.
 */
public class SpGetDatasetByDatasetIdBrapi implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDatasetByDatasetIdBrapi(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "SELECT \n" +
                    "d.dataset_id,\n" +
                    "d.experiment_id,\n" +
                    "d.name as variantset_name,\n" +
                    "e.name as study_name,\n" +
                    "d.callinganalysis_id,\n" +
                    "d.analyses as analysis_ids\n" +
                "FROM \n" +
                "dataset d\n" +
                "LEFT OUTER JOIN experiment e\n" +
                "USING (experiment_id)\n" +
                "WHERE d.dataset_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer dataSetId = (Integer) parameters.get("datasetId");
        preparedStatement.setInt(1, dataSetId);
        resultSet = preparedStatement.executeQuery();
    }
}
