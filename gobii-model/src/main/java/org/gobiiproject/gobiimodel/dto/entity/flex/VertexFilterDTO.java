package org.gobiiproject.gobiimodel.dto.entity.flex;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.ArrayList;
import java.util.List;

public class VertexFilterDTO extends DTOBase {

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {

        this.id = id;
    }




    // the vertex for which values are being retrieved
    // for a list of available filterVertices, will be empty
    private VertexDTO destinationVertexDTO = new VertexDTO();

    // will be populated when requesting a list of available filterVertices
    private List<VertexDTO> filterVertices = new ArrayList<>();

    // will be populated when requesting values for a vertex and its path
    private List<NameIdDTO> vertexValues = new ArrayList<>();

    // marker and sample counts given a path (the destination vertex is by definition the marker,sample tables
    private Integer markerCount = 0;
    private String markerFileFqpn;
    private Integer markerCountMs;

    private Integer sampleCount = 0;
    private String sampleFileFqpn;
    private Integer sampleCountMs;
    private Integer id = 1;



    public VertexDTO getDestinationVertexDTO() {
        return destinationVertexDTO;
    }

    public void setDestinationVertexDTO(VertexDTO destinationVertexDTO) {
        this.destinationVertexDTO = destinationVertexDTO;
    }

    public List<VertexDTO> getFilterVertices() {
        return filterVertices;
    }

    public void setFilterVertices(List<VertexDTO> filterVertices) {
        this.filterVertices = filterVertices;
    }

    public List<NameIdDTO> getVertexValues() {
        return vertexValues;
    }

    public void setVertexValues(List<NameIdDTO> vertexValues) {
        this.vertexValues = vertexValues;
    }

    public void setSampleCount(Integer sampleCount, String sampleFileFqpn,Integer sampleCountMs) {
        this.sampleCount = sampleCount;
        this.sampleFileFqpn = sampleFileFqpn;
        this.sampleCountMs = sampleCountMs;

    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount, String markerFileFqpn,Integer markerCountMs) {
       this.markerCount = markerCount;
       this.markerFileFqpn = markerFileFqpn;
       this.markerCountMs = markerCountMs;
       
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

    public String getMarkerFileFqpn() {
        return markerFileFqpn;
    }

    public void setMarkerFileFqpn(String markerFileFqpn) {
        this.markerFileFqpn = markerFileFqpn;
    }

    public Integer getMarkerCountMs() {
        return markerCountMs;
    }

    public void setMarkerCountMs(Integer markerCountMs) {
        this.markerCountMs = markerCountMs;
    }

    public String getSampleFileFqpn() {
        return sampleFileFqpn;
    }

    public void setSampleFileFqpn(String sampleFileFqpn) {
        this.sampleFileFqpn = sampleFileFqpn;
    }

    public Integer getSampleCountMs() {
        return sampleCountMs;
    }

    public void setSampleCountMs(Integer sampleCountMs) {
        this.sampleCountMs = sampleCountMs;
    }
}
