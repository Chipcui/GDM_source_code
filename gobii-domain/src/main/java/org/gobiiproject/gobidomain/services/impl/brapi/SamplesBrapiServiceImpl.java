package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.CvIdCvTermMapper;
import org.gobiiproject.gobidomain.services.SamplesBrapiService;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SamplesBrapiServiceImpl implements SamplesBrapiService {


    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Autowired
    private CvDao cvDao;

    @Override
    public List<SamplesBrapiDTO> getSamples(Integer pageNum, Integer pageSize, Integer sampleDbId) {

        List<SamplesBrapiDTO> returnVal = new ArrayList<>();

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(pageNum, pageSize, sampleDbId);

        List<Cv> cvList = cvDao.getCvListByCvGroup(
                CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null);

        for(DnaSample dnaSample : dnaSamples) {

            if(dnaSample != null) {

                SamplesBrapiDTO samplesBrapiDTO = new SamplesBrapiDTO();

                ModelMapper.mapEntityToDto(dnaSample, samplesBrapiDTO);

                if (dnaSample.getProperties() != null && dnaSample.getProperties().size() > 0) {

                    samplesBrapiDTO.setAdditionalInfo(CvIdCvTermMapper.mapCvIdToCvTerms(
                            cvList, dnaSample.getProperties()));

                    if(samplesBrapiDTO.getAdditionalInfo().containsKey("sample_type")) {
                        samplesBrapiDTO.setTissueType(samplesBrapiDTO.getAdditionalInfo().get("sample_type"));
                        samplesBrapiDTO.getAdditionalInfo().remove("sample_type");
                    }
                }

                returnVal.add(samplesBrapiDTO);
            }
        }

        return returnVal;
    }


}
