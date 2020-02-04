package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.util.List;
import java.util.Map;

public interface CvDao {

    List<Cv> getCvListByCvGroup(String cvGroupName, GobiiCvGroupType cvGroupType);
    List<Cv> getCvsByCvTermAndCvGroup(String cvTerm, String cvGroupName, GobiiCvGroupType cvType);
    Cv getCvByCvId(Integer cvId);

}
