package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
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
	public ExperimentDTO processExperiment(ExperimentDTO experimentDTO) {
		// TODO Auto-generated method stub

        ExperimentDTO returnVal = experimentDTO;

        try {
            switch (returnVal.getGobiiProcessType()) {
                case READ:
                    returnVal  = dtoMapExperiment.getExperimentDetails(returnVal.getExperimentId());
                    break;

                case CREATE:
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapExperiment.createExperiment(returnVal);
                    break;

                case UPDATE:
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapExperiment.replaceExperiment(returnVal.getExperimentId(),returnVal);
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported process type " + experimentDTO.getGobiiProcessType().toString());

            } // switch()

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return  returnVal;
	}

    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDomainException {

        List<ExperimentDTO> returnVal;

        try {
            returnVal = dtoMapExperiment.getExperiments();

//            for(ExperimentDTO currentExperimentDTO : returnVal ) {
//                currentExperimentDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
//                currentExperimentDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
//            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public ExperimentDTO getExperimentById(Integer experimentId) throws GobiiDomainException {

        ExperimentDTO returnVal;

        try {
            returnVal = dtoMapExperiment.getExperimentDetails(experimentId);

//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified experimentId ("
                                + experimentId
                                + ") does not match an existing experiment ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

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
//        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;
    }

    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDomainException {
        ExperimentDTO returnVal;

        try {

            if (null == experimentDTO.getExperimentId() ||
                    experimentDTO.getExperimentId().equals(experimentId)) {


                ExperimentDTO existingExperimentDTO = dtoMapExperiment.getExperimentDetails(experimentId);

                if (null != existingExperimentDTO.getExperimentId() && existingExperimentDTO.getExperimentId().equals(experimentId)) {


                    experimentDTO.setModifiedDate(new Date());
                    returnVal = dtoMapExperiment.replaceExperiment(experimentId, experimentDTO);
//                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
//                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

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


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;
    }
    

} // ExperimentServiceImpl
