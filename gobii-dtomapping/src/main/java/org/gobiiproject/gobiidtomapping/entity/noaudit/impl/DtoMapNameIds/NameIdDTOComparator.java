package org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds;

import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.Comparator;

/**
 * Created by VCalaminos on 10/11/2018.
 */
public class NameIdDTOComparator implements Comparator<NameIdDTO> {

    public int compare(NameIdDTO nameIdDTO1, NameIdDTO nameIdDTO2) {

        return nameIdDTO1.getName().compareTo(nameIdDTO2.getName());

    }

}
