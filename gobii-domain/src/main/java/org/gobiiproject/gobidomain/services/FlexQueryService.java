package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 * Modified by Yanii on 1/27/2017
 */
public interface FlexQueryService {

    List<VertexDTO> getVertices() throws GobiiDomainException;
    VertexFilterDTO getVerticesValues(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDomainException;
    VertexFilterDTO getVertexValuesCounts(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDomainException;
}
