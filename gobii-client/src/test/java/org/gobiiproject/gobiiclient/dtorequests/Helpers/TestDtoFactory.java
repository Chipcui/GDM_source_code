package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/27/2016.
 */
public class TestDtoFactory {

    public static EntityParamValues makeArbitraryEntityParams() {

        EntityParamValues returnVal = new EntityParamValues();


        returnVal.add("fooparam", "fooval");
        returnVal.add("barparam", "barval");
        returnVal.add("foobarparam", "foobarval");

        return returnVal;
    }

    public static AnalysisDTO makePopulatedAnalysisDTO(DtoMetaData.ProcessType processType,
                                                       Integer uniqueStem,
                                                       EntityParamValues entityParamValues) {

        AnalysisDTO returnVal = new AnalysisDTO(processType);

        returnVal.setAnalysisName(uniqueStem + ": analysis");
        returnVal.setTimeExecuted(new Date());
        returnVal.setSourceUri(uniqueStem + ":  foo URL");
        returnVal.setAlgorithm(uniqueStem + ":  foo algorithm");
        returnVal.setSourceName(uniqueStem + ":  foo source");
        returnVal.setAnalysisDescription(uniqueStem + ":  my analysis description");
        returnVal.setProgram(uniqueStem + ":  foo program");
        returnVal.setProgramVersion(uniqueStem + ":  foo version");
        returnVal.setAnlaysisTypeId(1);
        returnVal.setStatus(1);

        returnVal.setParameters(entityParamValues.getProperties());

        return returnVal;

    }

    public static DataSetDTO makePopulatedDataSetDTO(DtoMetaData.ProcessType processType,
                                                     Integer uniqueStem,
                                                     Integer callingAnalysisId,
                                                     List<Integer> analysisIds) {

        DataSetDTO returnVal = new DataSetDTO(processType);


        // set the big-ticket items

        returnVal.getScores().add(1);
        returnVal.getScores().add(2);
        returnVal.getScores().add(3);

        // set the plain properties
        returnVal.setStatus(1);
        returnVal.setCreatedBy(1);
        returnVal.setCreatedDate(new Date());
        returnVal.setDataFile(uniqueStem + ": foo file");
        returnVal.setQualityFile(uniqueStem + ": foo quality file");
        returnVal.setExperimentId(2);
        returnVal.setDataTable(uniqueStem + ": foo table");
        returnVal.setModifiedBy(1);
        returnVal.setModifiedDate(new Date());
        returnVal.setCallingAnalysisId(callingAnalysisId);
        returnVal.setAnalysesIds(analysisIds);

        return returnVal;

    }
}
