package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;

import java.util.List;

public interface DnaRunDao {

    List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                            Integer dnaRunId, Integer datasetId);


    List<DnaRun> getDnaRunsByDnaRunIdCursor(Integer dnaRunId,
                                                Integer datasetId,
                                                Integer pageSize);

    DnaRun getDnaRunById(Integer dnaRunId);

    List<DnaRun> getDnaRunsByDatasetId(Integer datasetId,
                                       Integer pageSize,
                                       Integer rowOffset);


    List<DnaRun> getDnaRunsByDanRunIds(List<Integer> dnaRunIds);

    List<DnaRun> getDnaRunsByDanRunNames(List<String> dnaRunNames);

}

