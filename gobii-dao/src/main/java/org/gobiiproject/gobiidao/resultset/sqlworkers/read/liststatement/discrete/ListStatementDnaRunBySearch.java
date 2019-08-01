package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DNARUN_SEARCH;

/**
 * Created by VCalaminos on 7/30/2019.
 */
public class ListStatementDnaRunBySearch implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DNARUN_SEARCH;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
            throws SQLException, GobiiException {

        String pageCondition = "";
        String pageSizeCondition = "";
        Integer pageSize = 0;

        String filterCondition = "";
        HashMap<String, Integer> filterConditionIndexArr = new HashMap<>();
        HashMap<Integer, HashMap<String, Object>> filterValueArr = new HashMap<>();
        Integer parameterIndex = 1;
        Integer[] germplasmIds = {};
        String[] germplasmNames = {};
        Integer[] callSetDbIds = {};
        String[] callSetNames = {};
        Integer[] sampleDbIds = {};
        String[] sampleNames = {};
        Integer[] variantSetDbIds = {};

        if (sqlParamVals != null) {

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
                    pageCondition = " AND dr.dnarun_id > ?\n";
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

            if (sqlParamVals.containsKey("searchQuery")) {

                JsonObject searchQuery = (JsonObject) sqlParamVals.get("searchQuery");

                if (searchQuery.has("callSetDbIds")) {

                    JsonArray callSetIdJsonArray = (JsonArray) searchQuery.get("callSetDbIds");
                    List<Integer> list = new ArrayList<>();

                    for (int i=0; i<callSetIdJsonArray.size(); i++) {
                        list.add(callSetIdJsonArray.get(i).getAsInt());
                    }

                    callSetDbIds = list.toArray(new Integer[list.size()]);
                    filterCondition += "and dr.dnarun_id = ANY(?)\n";
                    filterConditionIndexArr.put("callSetDbIds", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "integer");
                    filterType.put("value", callSetDbIds);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

                if (searchQuery.has("callSetNames")) {

                    JsonArray callSetNameJsonArray = (JsonArray) searchQuery.get("callSetNames");
                    List<String> list = new ArrayList<>();

                    for (int i=0; i<callSetNameJsonArray.size(); i++) {
                        list.add(callSetNameJsonArray.get(i).getAsString());
                    }

                    callSetNames = list.toArray(new String[list.size()]);
                    filterCondition += "and dr.name = ANY(?)\n";
                    filterConditionIndexArr.put("callSetNames", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "varchar");
                    filterType.put("value", callSetNames);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

                if (searchQuery.has("germplasmDbIds")) {

                    JsonArray germplasmJsonArray = (JsonArray) searchQuery.get("germplasmDbIds");
                    List<Integer> list = new ArrayList<>();

                    for (int i=0; i<germplasmJsonArray.size(); i++) {
                        list.add(germplasmJsonArray.get(i).getAsInt());
                    }

                    germplasmIds = list.toArray(new Integer[list.size()]);
                    filterCondition += "and g.germplasm_id = ANY(?)\n";
                    filterConditionIndexArr.put("germplasmDbIds", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "integer");
                    filterType.put("value", germplasmIds);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

                if (searchQuery.has("germplasmNames")) {

                    JsonArray germaplasmNameJsonArray = (JsonArray) searchQuery.get("germplasmNames");
                    List<String> list = new ArrayList<>();

                    for (int i=0; i<germaplasmNameJsonArray.size(); i++) {
                        list.add(germaplasmNameJsonArray.get(i).getAsString());
                    }

                    germplasmNames = list.toArray(new String[list.size()]);
                    filterCondition += "and g.name = ANY(?)\n";
                    filterConditionIndexArr.put("germplasmNames", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "varchar");
                    filterType.put("value", germplasmNames);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

                if (searchQuery.has("sampleNames")) {

                    JsonArray sampleNameJsonArray = (JsonArray) searchQuery.get("sampleNames");
                    List<String> list = new ArrayList<>();

                    for (int i=0; i<sampleNameJsonArray.size(); i++) {
                        list.add(sampleNameJsonArray.get(i).getAsString());
                    }

                    sampleNames = list.toArray(new String[list.size()]);
                    filterCondition += "and s.name = ANY(?)\n";
                    filterConditionIndexArr.put("sampleNames", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "varchar");
                    filterType.put("value", sampleNames);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

                if (searchQuery.has("sampleDbIds")) {

                    JsonArray sampleIdJsonArray = (JsonArray) searchQuery.get("sampleDbIds");
                    List<Integer> list = new ArrayList<>();

                    for (int i=0; i<sampleIdJsonArray.size(); i++) {
                        list.add(sampleIdJsonArray.get(i).getAsInt());
                    }

                    sampleDbIds = list.toArray(new Integer[list.size()]);
                    filterCondition += "and dr.dnasample_id = ANY(?)\n";
                    filterConditionIndexArr.put("sampleDbIds", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "integer");
                    filterType.put("value", sampleDbIds);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

                if (searchQuery.has("variantSetDbIds")) {

                    JsonArray variantSetIdJsonArray = (JsonArray) searchQuery.get("variantSetDbIds");
                    List<Integer> list = new ArrayList<>();

                    for (int i=0; i<variantSetIdJsonArray.size(); i++) {
                        list.add(variantSetIdJsonArray.get(i).getAsInt());
                    }

                    variantSetDbIds = list.toArray(new Integer[list.size()]);
                    filterCondition += "and jsonb_exists_any(dr.dataset_dnarun_idx,?)\n";
                    filterConditionIndexArr.put("variantSetDbIds", parameterIndex);
                    HashMap<String, Object> filterType = new HashMap<>();
                    filterType.clear();
                    filterType.put("type", "integer");
                    filterType.put("value", variantSetDbIds);
                    filterValueArr.put(parameterIndex, filterType);
                    parameterIndex++;
                }

            }
        }

        String sql = "SELECT\n" +
                "dr.dnarun_id,\n" +
                "dr.experiment_id,\n" +
                "dr.dnasample_id,\n" +
                "dr.name,\n" +
                "dr.code,\n" +
                "dr.dataset_dnarun_idx,\n" +
                "s.name as sample_name,\n" +
                "g.germplasm_id,\n" +
                "g.name as germplasm_name,\n" +
                "g.external_code as germplasm_external_code,\n" +
                "s.num,\n" +
                "s.well_row,\n" +
                "s.well_col,\n" +
                "gtype.term as germplasm_type,\n" +
                "species.term as species,\n" +
                "s.props as sample_props,\n" +
                "g.props as germplasm_props\n" +
                "FROM\n" +
                "dnarun dr, dnasample s, germplasm g\n" +
                "LEFT JOIN cv as gtype on g.type_id = gtype.cv_id \n" +
                "LEFT JOIN cv as species on g.species_id = species.cv_id\n" +
                "WHERE\n" +
                "dr.dnasample_id = s.dnasample_id\n" +
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
            Integer index = filter.getValue();

            String type = filterValueArr.get(index).get("type").toString();
            Object[] value = (Object[]) filterValueArr.get(index).get("value");

            returnVal.setArray(index, dbConnection.createArrayOf(type, value));
        }

        if (!pageSizeCondition.isEmpty()) {
            returnVal.setInt(parameterIndex, pageSize);
        }

        return returnVal;

    }

}
