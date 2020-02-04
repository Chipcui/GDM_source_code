package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobidomain.services.SearchService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SearchResultDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.UUID;

public class SearchServiceImpl implements SearchService {

    @Autowired
    private ConfigSettingsService configSettingsService;

    @Override
    public SearchResultDTO createSearchQueryResource(String cropType,
                                                     String searchQueryString) {

        if (cropType == null) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error: Unable to read crop configuration");
        }

        SearchResultDTO searchResultDTO = new SearchResultDTO();

        try {
            String processingId = UUID.randomUUID().toString();

            String extractQueryPath = Paths.get(
                    configSettingsService.getConfigSettings().getServerConfigs().get(
                            cropType).getFileLocations().get(GobiiFileProcessDir.RAW_USER_FILES),
                    processingId,
                    "searchQuery.json"
            ).toString();


            File extractQueryFile = new File(extractQueryPath);

            extractQueryFile.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(extractQueryFile));

            bw.write(searchQueryString);

            bw.close();


            searchResultDTO.setSearchResultDbId(processingId);


            return searchResultDTO;
        } catch (Exception e) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal Server Error");

        }
    }
}
