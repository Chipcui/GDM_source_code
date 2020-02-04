package org.gobiiproject.gobiimodel.modelmapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityFieldBean {

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private String columnName;

    private String tableName;

    private String instructionFileColumnName;

    public String getInstructionFileColumnName() {
        return instructionFileColumnName;
    }

    public void setInstructionFileColumnName(String instructionFileColumnName) {
        this.instructionFileColumnName = instructionFileColumnName;
    }
}
