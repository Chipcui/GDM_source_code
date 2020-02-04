package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;

import java.util.List;

public interface DtoMapMapsetBrApi {

    List<MapsetBrapiDTO> listMapset(Integer pageNum, Integer pageSize);
    MapsetBrapiDTO getMapsetById(Integer mapSetId);
}
