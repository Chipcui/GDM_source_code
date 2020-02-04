package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/11/2019.
 */
public interface DtoMapAnalysisBrapi {

    List<AnalysisBrapiDTO> getList() throws GobiiDtoMappingException;

}
