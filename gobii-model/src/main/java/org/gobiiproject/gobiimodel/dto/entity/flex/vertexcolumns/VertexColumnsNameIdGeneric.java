package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

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
    private final String FIELD_NAME = "name";


    private List<String> columns;
    public VertexColumnsNameIdGeneric() {

        this.columns = new ArrayList<>(Arrays.asList(
                FIELD_ID,
                FIELD_NAME));
    }

    @Override
    public List<String> getColumnNames() {
        return columns;
    }

    @Override
    public NameIdDTO vertexValueFromLine(String line) throws Exception {

        NameIdDTO returnVal = new NameIdDTO();

        String[] values = line.split("\t", -1);

        String id = values[this.columns.indexOf(FIELD_ID)];
        String name = values[this.columns.indexOf(FIELD_NAME)];

        returnVal.setName(name);
        returnVal.setId( Integer.parseInt(id));

        return returnVal;

    }
}
