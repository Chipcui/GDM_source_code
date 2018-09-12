package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.List;

public class VertexColumnsKvp implements VertexColumns {


    private VertexColumnsNameIdGeneric vertexColumnsNameIdGeneric;
    public VertexColumnsKvp(String kvpColumnName) {
        vertexColumnsNameIdGeneric = new VertexColumnsNameIdGeneric(kvpColumnName,false);
    }

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
