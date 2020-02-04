package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public class SpGetMarkerByMarkerIdBrapi implements Work {

    private Map<String, Object> parameters = null;

    public SpGetMarkerByMarkerIdBrapi(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "SELECT  \n" +
                    "mr.marker_id, \n" +
                    "mr.platform_id, \n" +
                    "mr.variant_id, \n" +
                    "mr.name, \n" +
                    "mr.code, \n" +
                    "mr.reference_id, \n" +
                    "mr.dataset_marker_idx  ,\n" +
                    "r.name as reference_name, \n" +
                    "p.name as platform_name,\n" +
                    "lg.name as linkage_group_name,\n" +
                    "mlg.start,\n" +
                    "mlg.stop,\n" +
                    "mp.name as mapset_name\n"+
                "FROM  \n" +
                    "marker mr\n" +
                "LEFT OUTER JOIN reference r \n" +
                "USING(reference_id)\n" +
                "LEFT OUTER JOIN platform p\n" +
                "USING (platform_id)\n" +
                "LEFT OUTER JOIN marker_linkage_group mlg\n" +
                "USING (marker_id)\n" +
                "LEFT OUTER JOIN linkage_group lg\n" +
                "USING (linkage_group_id)\n" +
                "LEFT OUTER JOIN mapset mp\n" +
                "ON mp.mapset_id = lg.map_id\n" +
                "WHERE mr.marker_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer markerId = (Integer) parameters.get("markerId");
        preparedStatement.setInt(1, markerId);
        resultSet = preparedStatement.executeQuery();
    }

}
