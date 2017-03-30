package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Araquel on 2017-2-8.
 */
public class SpGetProtocolDetailsByExperimentId implements Work {

    private Map<String, Object> parameters = null;
    public SpGetProtocolDetailsByExperimentId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet(){ return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select p.protocol_id,\n" +
                "p.name::text,\n" +
                "p.description,\n" +
                "p.type_id,\n" +
                "p.platform_id,\n" +
                "p.props,\n" +
                "p.created_by,\n" +
                "p.created_date,\n" +
                "p.modified_by,\n" +
                "p.modified_date,\n" +
                "p.status from experiment e " +
                "join vendor_protocol vp on (e.vendor_protocol_id=vp.vendor_protocol_id) " +
                "join protocol p on (vp.protocol_id=p.protocol_id) " +
                " where e.experiment_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("experimentId"));
        resultSet = preparedStatement.executeQuery();
    }

}
