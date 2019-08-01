package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class DnaRunServiceImpl implements DnaRunService {

    Logger LOGGER = LoggerFactory.getLogger(DnaRunServiceImpl.class);

    @Autowired
    private DtoMapDnaRun dtoMapDnaRun = null;

    @Override
    public DnaRunDTO getDnaRunById(Integer dnaRunId) throws GobiiDomainException {

        DnaRunDTO returnVal;


        try {

            returnVal = dtoMapDnaRun.get(dnaRunId);

            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dnarun not found for given id.");
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

    @Override
    public List<DnaRunDTO> getDnaRuns(Integer pageToken, Integer pageSize,
                                      DnaRunDTO dnaRunDTOFilter) throws GobiiDomainException {

        List<DnaRunDTO> returnVal;

        try {

            returnVal = dtoMapDnaRun.getList(pageToken, pageSize, dnaRunDTOFilter);

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

        return returnVal;
    }

    @Override
    public String createSearchQuery(String searchQuery) throws GobiiDomainException {

        String searchResultDbId;

        try {

            searchResultDbId = dtoMapDnaRun.createSearchQuery(searchQuery);

        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return searchResultDbId;
    }

    @Override
    public List<DnaRunDTO> getDnaRunsFromSearch(String searchResultsDbId, Integer pageToken, Integer pageSize) throws GobiiDomainException {

        List<DnaRunDTO> returnVal = new ArrayList<>();

        try {

            returnVal = dtoMapDnaRun.getListFromSearch(searchResultsDbId, pageToken, pageSize);


        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }
}
