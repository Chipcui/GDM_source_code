package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.OrganizationService;
import org.gobiiproject.gobiidtomapping.DtoMapOrganization;
import org.gobiiproject.gobiimodel.dto.container.OrganizationDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Angel on 5/4/2016.
 */
public class OrganizationServiceImpl implements OrganizationService {

    Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    DtoMapOrganization dtoMapOrganization = null;

    @Override
    public OrganizationDTO process(OrganizationDTO organizationDTO) {

        OrganizationDTO returnVal = new OrganizationDTO();

        try {
            switch (organizationDTO.getGobiiProcessType()) {
                case READ:
                    returnVal = dtoMapOrganization.getOrganizationDetails(organizationDTO);
                    break;

                case CREATE:
                    returnVal = dtoMapOrganization.createOrganization(organizationDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapOrganization.updateOrganization(organizationDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported proces Organization type " + organizationDTO.getGobiiProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}