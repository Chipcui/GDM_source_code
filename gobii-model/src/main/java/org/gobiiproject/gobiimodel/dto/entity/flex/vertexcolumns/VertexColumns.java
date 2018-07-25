package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.List;

public interface VertexColumns {

    List<String> getColumnNames();

    NameIdDTO vertexValueFromLine(String line) throws Exception;
}
