package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SpGetMapsetByMapsetIdBrapi implements Work {

    private Map<String, Object> parameters = null;

    public SpGetMapsetByMapsetIdBrapi(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        if(parameters.containsKey("mapsetId")) {
            Integer mapsetId = (Integer) parameters.get("mapsetId");
            preparedStatement.setInt(1, mapsetId);
        }
        else {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN, "mapsetId not defined");
        }
        resultSet = preparedStatement.executeQuery();
    }

}
