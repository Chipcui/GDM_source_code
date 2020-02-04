package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;

import java.util.List;

public interface DnaSampleDao {

    List<DnaSample> getDnaSamples(Integer pageSize, Integer rowOffet,
                                  Integer projectId, Integer dnaSampleId,
                                  Integer germplasmId, String germplasmExternamCode);

    List<DnaSample> getDnaSamples(Integer pageSize, Integer rowOffset);

    List<DnaSample> getDnaSamplesByProjectId(Integer projectId, Integer pageSize, Integer rowOffset);

    DnaSample getDnaSampleByDnaSampleId(Integer dnaSampleId);

    List<DnaSample> getDnaSamplesByGermplasmId(Integer germplasmId, Integer pageSize, Integer rowOffset);

    List<DnaSample> getDnaSamplesByGermplasmExternalCode(String germplasmExternalCode, Integer pageSize, Integer rowOffset);

}
