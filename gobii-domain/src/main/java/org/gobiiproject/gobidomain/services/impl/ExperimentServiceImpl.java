package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiimodel.headerlesscontainer.gobii.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Angel on 4/19/2016.
 */
public class ExperimentServiceImpl implements ExperimentService {


    Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private DtoMapExperiment dtoMapExperiment = null;


    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDomainException {

        List<ExperimentDTO> returnVal;

        returnVal = dtoMapExperiment.getExperiments();

        for (ExperimentDTO currentExperimentDTO : returnVal) {
            currentExperimentDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentExperimentDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }


        if (null == returnVal) {
            returnVal = new ArrayList<>();
        }


        return returnVal;
    }

    @Override
    public ExperimentDTO getExperimentById(Integer experimentId) throws GobiiDomainException {

        ExperimentDTO returnVal;

        returnVal = dtoMapExperiment.getExperimentDetails(experimentId);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified experimentId ("
                            + experimentId
                            + ") does not match an existing experiment ");
        }

        return returnVal;
    }

    @Override
    public ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDomainException {
        ExperimentDTO returnVal;

        experimentDTO.setCreatedDate(new Date());
        experimentDTO.setModifiedDate(new Date());
        returnVal = dtoMapExperiment.createExperiment(experimentDTO);

        // When we have roles and permissions, this will be set programmatically
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;
    }

    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDomainException {
        ExperimentDTO returnVal;


        if (null == experimentDTO.getExperimentId() ||
                experimentDTO.getExperimentId().equals(experimentId)) {


            ExperimentDTO existingExperimentDTO = dtoMapExperiment.getExperimentDetails(experimentId);

            if (null != existingExperimentDTO.getExperimentId() && existingExperimentDTO.getExperimentId().equals(experimentId)) {


                experimentDTO.setModifiedDate(new Date());
                returnVal = dtoMapExperiment.replaceExperiment(experimentId, experimentDTO);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified experimentId ("
                                + experimentId
                                + ") does not match an existing experiment ");
            }

        } else {

            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "The experimentId specified in the dto ("
                            + experimentDTO.getExperimentId()
                            + ") does not match the experimentId passed as a parameter "
                            + "("
                            + experimentId
                            + ")");

        }


        return returnVal;
    }


} // ExperimentServiceImpl
