package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Phil on 4/6/2016.
 */
public class NameIdListServiceImpl implements NameIdListService {


    private Logger LOGGER = LoggerFactory.getLogger(NameIdListServiceImpl.class);

    @Autowired
    DtoMapNameIdList dtoMapNameIdList;

    @Override
    public List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        /** NOTE
         * Added this validation to prevent the issue for GP1-1882.
         * If validation is REMOVED, the service that gets the ID's for given name list should be fixed so that it will work even if the list contains duplicate names
         * **/

        if (dtoMapNameIdParams.getGobiiFilterType().equals(GobiiFilterType.NAMES_BY_NAME_LIST)) {

            Set<String> duplicateNames = getDuplicateNames(dtoMapNameIdParams.getNameIdDTOList());

            if (duplicateNames.size() != 0) {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                        "There were duplicate values in the list: " +
                                duplicateNames);
            }

        }

        List<NameIdDTO> returnVal = dtoMapNameIdList.getNameIdList(dtoMapNameIdParams);
        returnVal.forEach(nameIdDTO -> nameIdDTO.getAllowedProcessTypes().add(GobiiProcessType.READ));



        return returnVal;
    }

    private Set<String> getDuplicateNames(List<NameIdDTO> nameIdDTOListInput) {

        Set<String> returnSet = new HashSet<>();
        Set<String> namesSet = new HashSet<>();

        for (NameIdDTO nameIdDTO : nameIdDTOListInput) {

            if (!namesSet.add(nameIdDTO.getName())) {

                returnSet.add(nameIdDTO.getName());

            }

        }

        return returnSet;

    }
}
