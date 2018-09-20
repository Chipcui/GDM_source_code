package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.FlexQueryService;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FlexQueryServiceImpl implements FlexQueryService {

    @Autowired
    DtoMapFlexQuery dtoMapFlexQuery;

    @Override
    public List<VertexDTO> getVertices() throws GobiiDomainException {
        return dtoMapFlexQuery.getVertices();
    }

    @Override
    public VertexFilterDTO getVerticesValues(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDomainException {
        return this.dtoMapFlexQuery.getVertexValues(cropType, jobId, vertexFilterDTO);
    }

    @Override
    public VertexFilterDTO getVertexValuesCounts(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDomainException {
        return this.dtoMapFlexQuery.getVertexValuesCounts(cropType, jobId, vertexFilterDTO);
    }


}
