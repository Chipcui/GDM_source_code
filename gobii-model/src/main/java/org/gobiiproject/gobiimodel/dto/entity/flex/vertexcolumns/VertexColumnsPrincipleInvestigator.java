package org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns;

import org.apache.commons.lang.math.NumberUtils;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VertexColumnsPrincipleInvestigator implements VertexColumns {

    private final String FIELD_CONTACT_ID = "contact_id";
    private final String FIELD_LAST_NAME = "lastname";
    private final String FIELD_FIRST_NAME = "firstname";

    private List<String> columns = new ArrayList<>(Arrays.asList(
            this.FIELD_CONTACT_ID,
            this.FIELD_LAST_NAME,
            this.FIELD_FIRST_NAME));

    @Override
    public List<String> getColumnNames() {
        return columns;
    }

    @Override
    public NameIdDTO vertexValueFromLine(String line) throws Exception {

        NameIdDTO returnVal = new NameIdDTO();

        String[] values = line.split("\t", -1);
        if( values.length < 3 ) {
            throw new Exception("The line does not contain three fields");
        }

        if (values.length < this.columns.size()) {
            throw new GobiiException("The parsed line "
                    + line +
                    " contains fewer fields than the column list "
                    + String.join(",", this.columns));
        }
        String lastName = values[this.columns.indexOf(FIELD_LAST_NAME)];
        String firstName = values[this.columns.indexOf(FIELD_FIRST_NAME)];
        String contactid = values[this.columns.indexOf(FIELD_CONTACT_ID)];

        returnVal.setName(lastName + ", " + firstName);

        if (!NumberUtils.isNumber(contactid)) {
            throw new Exception("The id column is not numeric");
        }

        returnVal.setId(Integer.parseInt(contactid));

        return returnVal;

    }
}
