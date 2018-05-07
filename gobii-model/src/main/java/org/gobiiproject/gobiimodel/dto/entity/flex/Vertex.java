package org.gobiiproject.gobiimodel.dto.entity.flex;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class Vertex {


    public Vertex() {}


    public Vertex(Integer vertexId, String vertexName, GobiiEntityNameType gobiiEntityNameType, String cvGroupName) {
        this.vertexId = vertexId;
        this.vertexName = vertexName;
        this.gobiiEntityNameType = gobiiEntityNameType;
        this.cvGroupName = cvGroupName;
    }

    private Integer vertexId = 0;
    private String vertexName;
    private List<Integer> filterVals = new ArrayList<>();
    GobiiEntityNameType gobiiEntityNameType;
    String cvGroupName;


    public Integer getVertexId() {
        return vertexId;
    }

    public void setVertexId(Integer vertexId) {
        this.vertexId = vertexId;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public List<Integer> getFilterVals() {
        return filterVals;
    }

    public void setFilterVals(List<Integer> filterVals) {
        this.filterVals = filterVals;
    }

    public GobiiEntityNameType getGobiiEntityNameType() {
        return gobiiEntityNameType;
    }

    public void setGobiiEntityNameType(GobiiEntityNameType gobiiEntityNameType) {
        this.gobiiEntityNameType = gobiiEntityNameType;
    }
}