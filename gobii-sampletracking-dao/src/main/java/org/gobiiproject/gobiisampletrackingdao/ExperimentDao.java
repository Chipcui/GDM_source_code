package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Experiment;

public interface ExperimentDao {

    Integer createExperiment(Experiment experiment);
    Experiment getExperimentById(Integer experimentId);
    Integer updateExperimentDataFile(Integer experimentId, String dataFilePath);
}
