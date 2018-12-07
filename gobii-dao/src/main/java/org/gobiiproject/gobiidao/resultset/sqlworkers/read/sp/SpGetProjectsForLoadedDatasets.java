package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by VCalaminos on 3/13/2018.
 */
public class SpGetProjectsForLoadedDatasets implements Work {

    public SpGetProjectsForLoadedDatasets() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "distinct on (p.name)\n" +
                "p.*\n" +
                "from\n" +
                "project p,\n" +
                "experiment e,\n" +
                "dataset d\n" +
                "inner join \n" +
                "job j \n" +
                "on \n" +
                "j.job_id = d.job_id \n" +
                "where\n" +
                "p.project_id = e.project_id\n" +
                "and e.experiment_id = d.experiment_id\n" +
                "and (\n" +
                "(j.type_id::text = (select cvid::text from getcvid('load', 'job_type', 1)) and j.status::text = (select cvid::text from getcvid('completed', 'job_status', 1)))\n" +
                "or (j.type_id::text = (select cvid::text from getcvid('extract', 'job_type', 1)) and j.status::text <> (select cvid::text from getcvid('failed', 'job_status', 1)))\n" +
                ")\n" +
                "order by p.name";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
