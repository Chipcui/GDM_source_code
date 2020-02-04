package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;

import java.util.List;

public interface VariantSetsBrapiService {

    //TODO: If cropType only dependency in this service. Leave it like this.
    // If more than one service happens to have cropType dependency, move it to constructor agrument
    // and pass it using dependency injection
    void setCropType(String cropType);

    List<VariantSetDTO> listVariantSets(Integer pageNum, Integer pageSize,
                                        Integer variantSetDbId);

    VariantSetDTO getVariantSetById(Integer variantSetDbId);

}
