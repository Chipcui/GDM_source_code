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
 * Created by VCalaminos on 9/18/2018.
 */
public class DtoMapNameIdFetchDnaSampleNames implements DtoMapNameIdFetch {

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchDnaSampleNames.class);

    @Override
    public GobiiEntityNameType getEntityTypeName() { return GobiiEntityNameType.DNASAMPLE; }


    private List<NameIdDTO> getDnaSampleNamesByNameList(List<NameIdDTO> nameIdDTOList, String projectId, GobiiFilterType gobiiFilterType) {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            List<String> nameArray = new ArrayList<>();
            List<NameIdDTO> nameIdDTOListInput = new ArrayList<>();

            for (NameIdDTO currentNameIdDTO : nameIdDTOList) {

                nameArray.add(currentNameIdDTO.getName());
                currentNameIdDTO.setId(0);
                nameIdDTOListInput.add(currentNameIdDTO);
            }

            ResultSet resultSet = dtoListQueryColl.getResultSet(ListSqlId.QUERY_ID_DNASAMPLE_NAMES_BYLIST,
                    new HashMap<String, Object>(){{
                        put("projectId", projectId);
                    }}, new HashMap<String, Object>(){{
                      put("nameArray", nameArray);
                    }});

            for (NameIdDTO currentNameIdDTO : nameIdDTOListInput) {

                while (resultSet.next()) {

                    if (currentNameIdDTO.getName().equals(resultSet.getString("name"))) {

                        currentNameIdDTO.setId(resultSet.getInt("dnasample_id"));
                        break;
                    }
                }


                if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST) {
                    returnVal.add(currentNameIdDTO);
                } else if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT) {
                    if (currentNameIdDTO.getId() == 0) {
                        returnVal.add(currentNameIdDTO);
                    }
                } else if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS) {
                    if (currentNameIdDTO.getId() > 0) {
                        returnVal.add(currentNameIdDTO);
                    }
                }
            }

        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;
    }

    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        GobiiFilterType gobiiFilterType = dtoMapNameIdParams.getGobiiFilterType();

        if (GobiiFilterType.NAMES_BY_NAME_LIST == gobiiFilterType || GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT == gobiiFilterType || GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS == gobiiFilterType) {

            returnVal = this.getDnaSampleNamesByNameList(dtoMapNameIdParams.getNameIdDTOList(), dtoMapNameIdParams.getFilterValueAsString(), gobiiFilterType);

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
