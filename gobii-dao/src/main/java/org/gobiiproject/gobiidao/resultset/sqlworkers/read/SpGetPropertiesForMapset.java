package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetPropertiesForMapset implements Work {

    private Map<String,Object> parameters = null;
    public SpGetPropertiesForMapset(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {
        String sql = "select property_id, property_name::text, property_value from getallpropertiesofmapset(?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        preparedStatement.setInt(1, (Integer) parameters.get(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
