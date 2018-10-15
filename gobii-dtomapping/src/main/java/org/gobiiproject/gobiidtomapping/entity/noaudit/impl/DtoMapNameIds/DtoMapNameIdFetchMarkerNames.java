package org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIdFetch;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VCalaminos on 9/19/2018.
 */
public class DtoMapNameIdFetchMarkerNames implements DtoMapNameIdFetch {

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchMarkerNames.class);

    @Override
    public GobiiEntityNameType getEntityTypeName() { return GobiiEntityNameType.MARKER; }


    private List<NameIdDTO> getMarkerNamesByNameList(List<NameIdDTO> nameIdDTOList, String platformId, GobiiFilterType gobiiFilterType) {

        try {

            ResultSet resultSet = dtoListQueryColl.getResultSet(ListSqlId.QUERY_ID_MARKER_NAMES_BYLIST,
                    new HashMap<String, Object>() {{
                        put("platformId", platformId);
                    }}, new HashMap<String, Object>() {{
                        put("nameArray", nameIdDTOList);
                    }});

            Integer resultSize = DtoMapNameIdUtil.getIdFromResultSet(nameIdDTOList, resultSet, "name", "marker_id", gobiiFilterType);

        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

        return nameIdDTOList;

    }

    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        GobiiFilterType gobiiFilterType = dtoMapNameIdParams.getGobiiFilterType();

        if (GobiiFilterType.NAMES_BY_NAME_LIST == gobiiFilterType || GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT == gobiiFilterType || GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS == gobiiFilterType) {

            returnVal = this.getMarkerNamesByNameList(dtoMapNameIdParams.getNameIdDTOList(), dtoMapNameIdParams.getFilterValueAsString(), gobiiFilterType);

        } else {

            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Unsupported filter type for "
                            + this.getEntityTypeName().toString().toLowerCase()
                            + ": " + dtoMapNameIdParams.getGobiiFilterType());
        }

        return returnVal;
    }

}
