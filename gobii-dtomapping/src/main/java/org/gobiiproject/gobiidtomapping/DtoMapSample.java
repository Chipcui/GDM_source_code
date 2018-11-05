package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.DnaSampleDTO;

import java.util.List;

/**
 * Created by Phil on 3/29/2016.
 */
public interface DtoMapSample {

    DnaSampleDTO getSampleDetailsByExternalCode(String externalCode) throws GobiiDtoMappingException;

    List<DataSetDTO> getDatasetForLoadedSamplesOfDataType(String externalCode, String datasetType) throws GobiiDtoMappingException;

    JobDTO submitDnaSamplesByJobName(String jobName, List<DnaSampleDTO> dnaSampleDTOList) throws GobiiDtoMappingException;
}
