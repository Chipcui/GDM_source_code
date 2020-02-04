package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SampleMetadataDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;

import java.util.List;

public interface DnaSampleService {


    ProjectSamplesDTO createSamples(ProjectSamplesDTO sampleListDTO) throws GobiiDomainException;

    JobDTO uploadSamples(
            byte[] inputFileBytes,
            SampleMetadataDTO sampleMetadataDTO,
            String cropType) throws GobiiDomainException;

}
