package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class SpGetJobs implements Work{

    private Map<String, Object> parameters = null;

    public SpGetJobs() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select \n" +
                "j.job_id,\n" +
                "type.term as type_id,\n" +
                "payloadtype.term as payload_type_id,\n" +
                "status.term as status,\n" +
                "j.message,\n" +
                "j.submitted_by,\n" +
                "j.submitted_date,\n" +
                "j.name,\n" +
                "d.dataset_id\n"+
                "from job j\n" +
                "left JOIN\n" +
                "dataset d\n" +
                "on d.job_id = j.job_id,\n" +
                "cv type,\n" +
                "cv payloadtype,\n" +
                "cv status\n" +
                "where j.type_id = type.cv_id\n" +
                "and j.payload_type_id = payloadtype.cv_id\n" +
                "and j.status = status.cv_id\n" +
                "order by j.name";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    }

}
