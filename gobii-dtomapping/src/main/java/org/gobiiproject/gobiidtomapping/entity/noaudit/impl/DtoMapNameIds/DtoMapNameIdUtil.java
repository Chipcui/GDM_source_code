package org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by VCalaminos on 9/20/2018.
 */
public class DtoMapNameIdUtil {


    public static Integer getIdFromResultSet(List<NameIdDTO> nameIdDTOList, ResultSet resultSet, String columnName, String columnId) throws SQLException {

        return DtoMapNameIdUtil.getIdFromResultSet(nameIdDTOList, resultSet, columnName, columnId, GobiiFilterType.NAMES_BY_NAME_LIST);
    }

    public static Integer getIdFromResultSet(List<NameIdDTO> nameIdDTOList, ResultSet resultSet, String columnName, String columnId, GobiiFilterType gobiiFilterType) throws SQLException {

        Collections.sort(nameIdDTOList);
        Integer index;

        List<NameIdDTO> nameList = new ArrayList<>();

        while (resultSet.next()) {

            NameIdDTO searchNameDTO = new NameIdDTO();
            searchNameDTO.setName(resultSet.getString(columnName));

            index = Collections.binarySearch(nameIdDTOList, searchNameDTO);

            if (index > -1) {

                NameIdDTO nameIdDTO  = nameIdDTOList.get(index);
                nameIdDTO.setId(resultSet.getInt(columnId));
                nameIdDTOList.set(index, nameIdDTO);

                if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_ABSENT) {
                    nameIdDTOList.remove(nameIdDTOList.get(index));

                } else if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS) {
                    nameList.add(nameIdDTO);
                }

            }

        }

        if (gobiiFilterType == GobiiFilterType.NAMES_BY_NAME_LIST_RETURN_EXISTS) {

            nameIdDTOList.removeAll(nameIdDTOList);
            nameIdDTOList.addAll(nameList);
        }

        return resultSet.getFetchSize();

    }
}
