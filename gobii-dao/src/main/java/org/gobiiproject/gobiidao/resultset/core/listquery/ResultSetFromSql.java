package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.hibernate.jdbc.Work;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 */
public class ResultSetFromSql implements Work {


    private ListStatement listStatement;
    private Map<String, Object> jdbcParameters = null;
    private Map<String, Object> sqlParameters = null;

    public ResultSetFromSql(ListStatement listStatement,
                            Map<String, Object> jdbcParameters,
                            Map<String, Object> sqlParameters) {
        this.listStatement = listStatement;
        this.jdbcParameters = jdbcParameters;
        this.sqlParameters = sqlParameters;
    }



    ResultSet resultSet = null;
    public ResultSet getResultSet() {
        return this.resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException, GobiiException {

            PreparedStatement preparedStatement = listStatement.makePreparedStatement(dbConnection,
                    this.jdbcParameters,
                    this.sqlParameters);

            this.resultSet = preparedStatement.executeQuery();

    } // execute()
}
