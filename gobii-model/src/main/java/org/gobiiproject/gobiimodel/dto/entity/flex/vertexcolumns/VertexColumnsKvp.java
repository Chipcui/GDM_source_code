package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VertexColumnsKvp implements VertexColumns {


    VertexColumnsNameIdGeneric vertexColumnsNameIdGeneric = new VertexColumnsNameIdGeneric();


    @Override
    public List<String> getColumnNames() {
        return vertexColumnsNameIdGeneric.getColumnNames();
    }

    @Override
    public NameIdDTO vertexValueFromLine(String line) throws Exception {

        NameIdDTO returnVal = vertexColumnsNameIdGeneric.vertexValueFromLine(line);

        String strippedName = returnVal.getName().replace("\"\"\"","");
        returnVal.setName(strippedName);

        return returnVal;

    }
}
