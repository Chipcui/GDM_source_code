package org.gobiiproject.gobiimodel.dto.entity.flex;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.List;

public class VertexFilterDTO extends DTOBase {

    @Override
    public Integer getId() {
        return this.getDestinationVertexDTO().getVertexId();
    }

    @Override
    public void setId(Integer id) {

        this.getDestinationVertexDTO().setVertexId(id);
    }

    // the vertex for which values are being retrieved
    // for a list of available vertices, will be empty
    private VertexDTO destinationVertexDTO = new VertexDTO();

    // will be populated when requesting a list of available vertices
    private List<VertexDTO> vertices = new ArrayList<>();

    // will be populated when requesting values for a vertex and its path
    private List<NameIdDTO> vertexValues = new ArrayList<>();

    // marker and sample counts given a path (the destination vertex is by definition the marker,sample tables
    Integer markerCount = 0;
    Integer sampleCount = 0;



    public VertexDTO getDestinationVertexDTO() {
        return destinationVertexDTO;
    }

    public void setDestinationVertexDTO(VertexDTO destinationVertexDTO) {
        this.destinationVertexDTO = destinationVertexDTO;
    }

    public List<VertexDTO> getVertices() {
        return vertices;
    }

    public void setVertices(List<VertexDTO> vertices) {
        this.vertices = vertices;
    }

    public List<NameIdDTO> getVertexValues() {
        return vertexValues;
    }

    public void setVertexValues(List<NameIdDTO> vertexValues) {
        this.vertexValues = vertexValues;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    public Integer getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
    }
}