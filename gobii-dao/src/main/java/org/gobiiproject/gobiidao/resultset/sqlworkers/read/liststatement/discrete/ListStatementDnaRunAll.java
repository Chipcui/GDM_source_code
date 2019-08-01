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

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DNARUN_ALL;

/**
 * Created by VCalaminos on 6/26/2019.
 */
public class ListStatementDnaRunAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DNARUN_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
            throws SQLException, GobiiException{

        String pageCondition = "";
        String pageSizeCondition = "";
        String filterCondition = "";
        Integer pageSize = 0;
        HashMap<String, Integer> filterConditionIndexArr = new HashMap<>();
        Integer parameterIndex=1;

        if(sqlParamVals != null) {
            if (sqlParamVals.containsKey("pageSize")
                    && sqlParamVals.get("pageSize") instanceof Integer) {
                pageSize = (Integer) sqlParamVals.getOrDefault("pageSize", 0);
                if (pageSize > 0) {
                    pageSizeCondition = "LIMIT ?";
                }
                else {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Size");
                }
            } else if(sqlParamVals.containsKey("pageSize")) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Size");
            }

            if (sqlParamVals.containsKey("pageToken")
                    && sqlParamVals.get("pageToken") instanceof Integer) {
                if ((Integer) sqlParamVals.getOrDefault("pageToken", 0) > 0) {
                    pageCondition = " AND dnarun_id > ?\n";
                } else {
                    pageCondition = "";
                }
            } else if(sqlParamVals.containsKey("pageToken")) {
                throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Token");
            }

            if (!pageCondition.isEmpty()) {
                parameterIndex = 2;
            }

            if (sqlParamVals.containsKey("callSetDbId")) {
                filterCondition += "and dr.dnarun_id = ?\n";
                filterConditionIndexArr.put("callSetDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("callSetName")) {
                filterCondition += "and dr.name=?\n";
                filterConditionIndexArr.put("callSetName", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("variantSetDbId")) {
                filterCondition += "and jsonb_exists(dr.dataset_dnarun_idx,?::text)\n";
                filterConditionIndexArr.put("variantSetDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("sampleDbId")) {
                filterCondition += "and dr.dnasample_id=?\n";
                filterConditionIndexArr.put("sampleDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("germplasmDbId")) {
                filterCondition += "and g.germplasm_id=?\n";
                filterConditionIndexArr.put("germplasmDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("studyDbId")) {
                filterCondition += "and dr.experiment_id=?\n";
                filterConditionIndexArr.put("studyDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("sampleName")) {
                filterCondition += "and s.name=?\n";
                filterConditionIndexArr.put("sampleName", parameterIndex);
                parameterIndex++;
            }
        }

        String sql = "SELECT \n" +
                    "dr.dnarun_id, \n" +
                    "dr.experiment_id, \n" +
                    "dr.dnasample_id,\n" +
                    "dr.name,\n" +
                    "dr.code,\n" +
                    "dr.dataset_dnarun_idx,\n" +
                    "s.name as sample_name, \n" +
                    "g.germplasm_id, \n" +
                    "g.name as germplasm_name,  \n" +
                    "g.external_code as germplasm_external_code, \n" +
                    "s.num,\n" +
                    "s.well_row,\n" +
                    "s.well_col,\n" +
                    "gtype.term as germplasm_type,\n" +
                    "species.term as species,\n" +
                    "s.props as sample_props,\n" +
                    "g.props as germplasm_props\n" +
                "FROM \n" +
                    "dnarun dr, dnasample s, germplasm g \n" +
                    "LEFT JOIN cv as gtype on g.type_id = gtype.cv_id \n" +
                    "LEFT JOIN cv as species on g.species_id = species.cv_id\n" +
                "WHERE \n" +
                    "dr.dnasample_id = s.dnasample_id \n" +
                    "and g.germplasm_id = s.germplasm_id\n" +
                pageCondition +
                filterCondition +
                "order by dr.dnarun_id\n" +
                pageSizeCondition;

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        if (!pageCondition.isEmpty()) {
            returnVal.setInt(1, (Integer) sqlParamVals.get("pageToken"));
        }

        for (Map.Entry<String, Integer> filter : filterConditionIndexArr.entrySet()) {
            if (filter.getKey().equals("callSetName") || filter.getKey().equals("sampleName")) {
                returnVal.setString(filter.getValue(), (String) sqlParamVals.get(filter.getKey()));
            } else {
                returnVal.setInt(filter.getValue(), (Integer) sqlParamVals.get(filter.getKey()));
            }
        }

        if(!pageSizeCondition.isEmpty()) {
            returnVal.setInt(parameterIndex, pageSize);
        }

        return returnVal;

    }

}
