package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.*;

import java.util.List;
import java.util.Map;

public interface DtoMapGenotypeCalls {


    List<GenotypeCallsDTO> getGenotypeCallsList(Integer datasetId, String pageToken,
                                                Integer pageSize);

    Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken);

    List<GenotypeCallsDTO> getGenotypeCallsListByExtractQuery(
            String extractQueryFilePath, String pageToken, Integer pageSize);

    String getNextPageOffset();

    String getNextColumnOffset();
}
