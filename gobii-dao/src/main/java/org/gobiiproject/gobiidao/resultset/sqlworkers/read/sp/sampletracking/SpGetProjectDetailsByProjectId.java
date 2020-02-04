package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp.sampletracking;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SpGetProjectDetailsByProjectId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetProjectDetailsByProjectId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "SELECT project.*, project_prop.properties AS system_properties " +
                "FROM project LEFT OUTER JOIN (" +
                "SELECT p.project_id AS project_id, " +
                "json_object(array_agg(cv.term::text), array_agg(p.props->>p.props_keys::text)) AS properties " +
                "FROM(SELECT project.project_id, project.props, jsonb_object_keys(props)::int as props_keys " +
                "FROM project WHERE project_id=?) AS p " +
                "INNER JOIN cv AS cv ON (cv.cv_id = p.props_keys) GROUP BY 1) AS project_prop " +
                "USING(project_id) WHERE project_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer projectId = (Integer) parameters.get("projectId");
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, projectId);
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
