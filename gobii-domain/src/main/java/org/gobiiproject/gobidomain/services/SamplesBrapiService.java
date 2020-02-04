package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;

import java.util.List;

public interface SamplesBrapiService {

    List<SamplesBrapiDTO> getSamples(Integer pageNum, Integer pageSize,
                                     Integer sampleDbId, Integer germplasmDbId,
                                     String observationUnitDbId);

}
