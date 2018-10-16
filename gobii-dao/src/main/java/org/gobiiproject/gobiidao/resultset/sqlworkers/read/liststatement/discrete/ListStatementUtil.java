package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.List;

/**
 * Created by VCalaminos on 9/21/2018.
 */
public class ListStatementUtil {


    public static String generateParsedNameList(List<NameIdDTO> nameIdDTOList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (NameIdDTO nameIdDTO : nameIdDTOList) {

            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("'")
                    .append(nameIdDTO.getName())
                    .append("'");

        }

        return stringBuilder.toString();

    }



}
