package org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles;

import org.gobiiproject.gobidomain.services.SampleService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.DnaSampleDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapMarkerProfiles {

    @Autowired
    private SampleService sampleService;

    public BrapiResponseMarkerProfilesMaster getBrapiResponseMarkerProfilesByGermplasmId(String germplasmId) {

        BrapiResponseMarkerProfilesMaster returnVal = new BrapiResponseMarkerProfilesMaster();
        DnaSampleDTO dnaSampleDTO = sampleService.getSampleDetailsByExternalCode(germplasmId); //brapi germplasmDbId --> gobii externalCode

        if( dnaSampleDTO.getGermplasmId() > 0 )  {

            returnVal.setAnalysisMethod(dnaSampleDTO.getAnalysisMethodName());
            returnVal.setExtractDbId(dnaSampleDTO.getDnaSampleId().toString());
            returnVal.setGermplasmDbId(dnaSampleDTO.getGermplasmId().toString());
            returnVal.setUniqueDisplayName(dnaSampleDTO.getGermplasmName());
            returnVal.setMarkerprofileDbId(dnaSampleDTO.getExternalCode());
        } else {
            throw new GobiiException("There is no germplasm with this external code: " + germplasmId);
        }

        returnVal.setData(new ArrayList<>());
        return returnVal;

    }
}
