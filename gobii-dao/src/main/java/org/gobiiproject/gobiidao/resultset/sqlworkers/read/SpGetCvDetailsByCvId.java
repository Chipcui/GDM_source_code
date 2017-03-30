package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/29/2016.
 */
public class SpGetCvDetailsByCvId implements Work {
    /**
     * Created by Phil on 4/29/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetCvDetailsByCvId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select c.cv_id, c.term::text, c.definition, c.rank, c.cvgroup_id, c.abbreviation, c.dbxref_id, c.status, c.props,\n" +
                "g.cvgroup_id, g.name, g.definition, g.props, g.type as group_type from cv c, cvgroup g where c.cvgroup_id = g.cvgroup_id and cv_id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer projectId = (Integer) parameters.get("cvId");
        preparedStatement.setInt(1, projectId);
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
