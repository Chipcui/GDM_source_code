package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.DnaSampleDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface SampleService {

    DnaSampleDTO getSampleDetailsByExternalCode(String externalCode) throws GobiiDomainException;

    List<DataSetDTO> getDatasetForLoadedSamplesOfDataType(String externalCode, String datasetType) throws GobiiDomainException;

}
