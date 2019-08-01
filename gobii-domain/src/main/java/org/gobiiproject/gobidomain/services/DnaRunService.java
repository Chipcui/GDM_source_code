package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;

import java.util.List;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public interface DnaRunService {

    DnaRunDTO getDnaRunById(Integer dnaRunId) throws GobiiDomainException;
    List<DnaRunDTO> getDnaRuns(Integer pageToken, Integer pageSize, DnaRunDTO dnaRunDTOFilter) throws GobiiDomainException;
    String createSearchQuery(String searchQuery) throws GobiiDomainException;
    List<DnaRunDTO> getDnaRunsFromSearch(String searchResultsDbId, Integer pageToken, Integer pageSize) throws GobiiDomainException;
}
