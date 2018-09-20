package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VertexColumnsNameIdGeneric implements VertexColumns {



    // this works for almost all cases because the name of the ID field
    // will always be "id" even when the actual entity (e.g., dataset) has
    // a different name (e.g., dataset_id)
    // The only cases in which we will need a different implementation
    // is when we need to capture different fields in addition to name, such as
    // with principle investigator
    // this class could almost be made completely general by adding a constructor that
    // takes in a list of fields. However, there are cases in which the ways in which
    // the columns are combined for the output (e.g., principal_investigator) have to
    // be implemented in a specific way
    private final String FIELD_ID = "id";
    private String valueFiledName = "name";


    private List<String> columns;
    private boolean useIdColumn = true;
    public VertexColumnsNameIdGeneric() {

        this.columns = new ArrayList<>(Arrays.asList(
                FIELD_ID,
                valueFiledName));
    }

    public VertexColumnsNameIdGeneric(String valueColumnName, boolean useIdColumn) {

        this.useIdColumn = useIdColumn;
        this.valueFiledName = valueColumnName;
        this.columns = new ArrayList<>(Arrays.asList(
                FIELD_ID,
                valueFiledName));
    }

    @Override
    public List<String> getColumnNames() {
        return columns;
    }

    @Override
    public NameIdDTO vertexValueFromLine(String line) throws Exception {

        // As long as the target file used getColumnNames() to specify
        // the columns to be used, the ordering by which they are retrieved
        // from the line will work out
        Integer valueIndex;
        NameIdDTO returnVal = new NameIdDTO();
        String[] values = line.split("\t", -1);
        if(this.useIdColumn) {

            valueIndex = this.columns.indexOf(valueFiledName);
            if( values.length < 2 ) {
                throw new Exception("Line does not contain at least two columns");
            }

            String id = values[this.columns.indexOf(FIELD_ID)];

            if( !NumberUtils.isNumber(id))
                throw new Exception("Column named 'id' is not numeric ");

            returnVal.setId(Integer.parseInt(id));
        } else {
            if( values.length < 1) {
                throw new Exception("Line does not contain at least one column");
            }

            valueIndex = 0;
        }

        String name = values[valueIndex];

        returnVal.setName(name);

        return returnVal;

    }
}
