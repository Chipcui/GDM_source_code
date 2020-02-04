package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.LinkageGroupBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;

import java.util.List;

public interface LinkageGroupBrapiService {

    List<LinkageGroupBrapiDTO> getLinkageGroups(Integer pageNumber, Integer pageSize);
    List<LinkageGroupBrapiDTO> getLinkageGroupsByMapId(
            Integer mapId,
            Integer pageNumber,
            Integer pageSize);
}
