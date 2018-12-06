package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DnaSampleDTO;

import java.util.List;

/**
 * Created by VCalaminos on 9/21/2018.
 */
public class ListStatementUtil {


    public static String generateParsedNameList(List<NameIdDTO> nameIdDTOList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (NameIdDTO nameIdDTO : nameIdDTOList) {

            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("'")
                    .append(nameIdDTO.getName())
                    .append("'");

        }

        return stringBuilder.toString();

    }


    public static String generateParsedNameListForDnaSamples(List<NameIdDTO> nameIdDTOList) {

        StringBuilder stringBuilder = new StringBuilder();

        for (NameIdDTO nameIdDTO : nameIdDTOList) {

            DnaSampleDTO dnaSampleDTO = (DnaSampleDTO) nameIdDTO.getQueryObject();

            if (stringBuilder.length() > 0) {
                stringBuilder.append(" or ");
            }

            stringBuilder.append("(name='")
                    .append(nameIdDTO.getName())
                    .append("' and num='")
                    .append(dnaSampleDTO.getDnaSampleNum())
                    .append("')");

        }

        return stringBuilder.toString();

    }


}
