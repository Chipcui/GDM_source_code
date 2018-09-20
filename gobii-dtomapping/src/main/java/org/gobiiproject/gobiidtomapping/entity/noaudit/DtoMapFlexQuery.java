package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapFlexQuery {

    //DtoMap methods
    List<VertexDTO> getVertices() throws GobiiDtoMappingException;
    VertexFilterDTO getVertexValues(String cropType,String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException;
    VertexFilterDTO getVertexValuesCounts(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException;


}
