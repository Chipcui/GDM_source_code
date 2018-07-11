package org.gobiiproject.gobiimodel.dto.entity.flex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.VertexNameType;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumns;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumnsKvp;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumnsNameIdGeneric;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumnsPrincipleInvestigator;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class VertexDTO extends DTOBase {


    public VertexDTO() {
    }


    public VertexDTO(Integer vertexId,
                     VertexNameType vertexNameType,
                     GobiiVertexType gobiiVertexType,
                     String vertexName,
                     GobiiEntityNameType entityType,
                     GobiiEntitySubType entitySubType,
                     CvGroup cvGroup,
                     String cvTerm) {
        this.vertexId = vertexId;
        this.vertexNameType = vertexNameType;
        this.gobiiVertexType = gobiiVertexType;
        this.vertexName = vertexName;
        this.entityType = entityType;
        this.entitySubType = entitySubType;
        this.cvGroup = cvGroup;
        this.cvTerm = cvTerm;
    }

    @Override
    public Integer getId() {
        return this.getVertexId();
    }

    @Override
    public void setId(Integer id) {
        this.vertexId = id;
    }


    private Integer vertexId = 0;
    private VertexNameType vertexNameType;
    private GobiiVertexType gobiiVertexType = GobiiVertexType.UNKNOWN;
    private String vertexName;
    private List<NameIdDTO> filterVals = new ArrayList<>();
    private GobiiEntityNameType entityType;
    private GobiiEntitySubType entitySubType;
    private CvGroup cvGroup;
    private String cvTerm;


    public Integer getVertexId() {
        return vertexId;
    }

    public void setVertexId(Integer vertexId) {
        this.vertexId = vertexId;
    }

    public VertexNameType getVertexNameType() {
        return vertexNameType;
    }

    public void setVertexNameType(VertexNameType vertexNameType) {
        this.vertexNameType = vertexNameType;
    }

    public GobiiVertexType getGobiiVertexType() {
        return gobiiVertexType;
    }

    public void setGobiiVertexType(GobiiVertexType gobiiVertexType) {
        this.gobiiVertexType = gobiiVertexType;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public List<NameIdDTO> getFilterVals() {
        return filterVals;
    }

    public void setFilterVals(List<NameIdDTO> filterVals) {
        this.filterVals = filterVals;
    }

    public GobiiEntityNameType getEntityType() {
        return entityType;
    }

    public void setEntityType(GobiiEntityNameType entityType) {
        this.entityType = entityType;
    }

    public GobiiEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(GobiiEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public CvGroup getCvGroup() {
        return cvGroup;
    }

    public void setCvGroup(CvGroup cvGroup) {
        this.cvGroup = cvGroup;
    }

    public String getCvTerm() {
        return cvTerm;
    }

    public void setCvTerm(String cvTerm) {
        this.cvTerm = cvTerm;
    }

    @JsonIgnore
    public VertexColumns getVertexColumns() throws GobiiException {

        VertexColumns returnVal;

        if (this.getGobiiVertexType().equals(GobiiVertexType.CVTERM)) {
            returnVal = new VertexColumnsKvp();
        } else if (this.getVertexNameType().getVertexName()
                .equals(VertexNameType.VERTEX_TYPE_PRINCIPLE_INVESTIGATOR.getVertexName())) {
            returnVal = new VertexColumnsPrincipleInvestigator();
        } else {
            returnVal = new VertexColumnsNameIdGeneric();
        }

        return returnVal;

    } // function get vertex columns


    @JsonIgnore
    public String toGqlSubPathElement() {

        StringBuilder returnVal = new StringBuilder();

        returnVal.append("\"" + this.getVertexNameType().getVertexName() + "\"");
        returnVal.append(":");
        returnVal.append("[");
        Iterator<NameIdDTO> iterator = this.getFilterVals().iterator();
        while (iterator.hasNext()) {

            NameIdDTO currentNameId = iterator.next();
            String currentVal;
            if (this.getGobiiVertexType() == GobiiVertexType.CVTERM) {
                currentVal = "\"" + currentNameId.getName() + "\"";
            } else {
                currentVal = currentNameId.getId().toString();
            }
            returnVal.append(currentVal);
            if (iterator.hasNext()) {
                returnVal.append(",");
            }
        }
        returnVal.append("]");

        return returnVal.toString();
    }


} // class
