package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 7/10/2019.
 */
public class ListStatementDatasetBrapiAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() { return ListSqlId.QUERY_ID_DATASET_ALL_BRAPI; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
        throws SQLException, GobiiException {

        String pageCondition = "";
        String pageSizeCondition = "";
        String filterCondition = "";
        Integer pageSize = 0;
        HashMap<String, Integer> filterConditionIndexArr = new HashMap<>();
        Integer parameterIndex = 1;

        if (sqlParamVals != null) {
            if (sqlParamVals.containsKey("pageSize")
                    && sqlParamVals.get("pageSize") instanceof Integer) {
                pageSize = (Integer) sqlParamVals.getOrDefault("pageSize", 0);
                if (pageSize > 0) {
                    pageSizeCondition = "LIMIT ?";
                } else {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Size"
                    );
                }
            } else if (sqlParamVals.containsKey("pageSize")) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Size"
                );
            }

            if (sqlParamVals.containsKey("pageToken")
                    && sqlParamVals.get("pageToken") instanceof Integer) {
                if ((Integer) sqlParamVals.getOrDefault("pageToken", 0) > 0) {
                    pageCondition = "WHERE d.dataset_id > ?\n";
                } else {
                    pageCondition = "";
                }
            } else if (sqlParamVals.containsKey("pageToken")) {
                throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Token");
            }

            if (!pageCondition.isEmpty()) {
                parameterIndex = 2;
            }

            if (sqlParamVals.containsKey("variantSetDbId")) {

                if (pageCondition.isEmpty()) {
                    filterCondition += "WHERE \n";
                } else {
                    filterCondition += "AND ";
                }

                filterCondition += " d.dataset_id = ?\n";
                filterConditionIndexArr.put("variantSetDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("variantSetName")) {

                if (pageCondition.isEmpty() && filterCondition.isEmpty()) {
                    filterCondition += "WHERE \n";
                } else {
                    filterCondition += "AND ";
                }

                filterCondition += " d.name = ?\n";
                filterConditionIndexArr.put("variantSetName", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("studyDbId")) {

                if (pageCondition.isEmpty() && filterCondition.isEmpty()) {
                    filterCondition += "WHERE \n";
                } else {
                    filterCondition += "AND ";
                }

                filterCondition += " e.experiment_id = ?\n";
                filterConditionIndexArr.put("studyDbId", parameterIndex);
                parameterIndex++;
            }
        }

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
                pageCondition +
                filterCondition +
                "order by d.dataset_id\n" +
                pageSizeCondition;

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        if (!pageCondition.isEmpty()) {
            returnVal.setInt(1, (Integer) sqlParamVals.get("pageToken"));
        }

        for (Map.Entry<String, Integer> filter : filterConditionIndexArr.entrySet()) {
            if (filter.getKey().equals("variantSetName")) {
                returnVal.setString(filter.getValue(), (String) sqlParamVals.get(filter.getKey()));
            } else {
                returnVal.setInt(filter.getValue(), (Integer) sqlParamVals.get(filter.getKey()));
            }
        }

        if (!pageSizeCondition.isEmpty()) {
            returnVal.setInt(parameterIndex, pageSize);
        }

        return returnVal;
    }

}
