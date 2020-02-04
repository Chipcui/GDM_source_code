package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.LinkageGroupBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;

import java.util.List;

public interface DtoMapLinkageGroupBrApi {

    List<LinkageGroupBrapiDTO> listLinkageGroup(Integer pageNum, Integer pageSize);
    List<LinkageGroupBrapiDTO> listLinkageGroupByMapId(Integer mapId, Integer pageNum, Integer pageSize);

}
