package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedListByCursor;

import java.util.List;
import java.util.Map;

public interface GenotypeCallsService {

    PagedListByCursor<GenotypeCallsDTO> getGenotypeCallsByCallSetId(
            Integer callSetDbId, Integer pageSize,
            String pageToken) throws GobiiDomainException;

    PagedListByCursor<GenotypeCallsDTO> getGenotypeCallsByVariantDbId(
            Integer markerId, Integer pageSize,
            String pageToken) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByDatasetId(
            Integer datasetId, Integer pageSize,
            String pageToken) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            String extractQueryFilePath, Integer pageSize,
            String pageToken) throws GobiiDomainException;

    Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken);

    String getNextPageToken();

}
