package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DatasetBrapiService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapDatasetBrapi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by VCalaminos on 7/10/2019.
 */
public class DatasetBrapiServiceImpl implements DatasetBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(DatasetBrapiServiceImpl.class);

    @Autowired
    private DtoMapDatasetBrapi dtoMapDatasetBrapi = null;

    @Override
    public List<DataSetBrapiDTO> getDatasets(Integer pageToken, Integer pageSize,
                                             DataSetBrapiDTO dataSetBrapiDTOFilter) throws GobiiDomainException {

        List<DataSetBrapiDTO> returnVal;

        try {

            returnVal = dtoMapDatasetBrapi.getList(pageToken, pageSize, dataSetBrapiDTOFilter);

        } catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    @Override
    public DataSetBrapiDTO getDatasetById(Integer datasetId) throws GobiiDomainException {

        DataSetBrapiDTO returnVal;

        try {

            returnVal = dtoMapDatasetBrapi.get(datasetId);

            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dataset not found for given id.");
            }

            return returnVal;

        }
        catch (GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

    }
}
