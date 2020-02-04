package org.gobiiproject.gobidomain;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Module to map CV Ids to CV terms and vice versa.
 */
public class CvIdCvTermMapper {

    /**
     * Function maps the properties object with CV term as a key to Json Node
     * with cvIds as Key.
     *
     * @param cvList List of Cv Entity
     * @param properties Map object with cv term as key which needs to be mapped to their cvId as key
     * @return Json Node with cvId as Key. Returning Json Node as Entity Column is a Json Column.
     */
    public static JsonNode mapCvTermsToCvId(List<Cv> cvList, Map<String, String> properties) {

        JsonNode returnVal;

        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, String> cvIdMappedProperties = new HashMap<>();

            for (Cv cv : cvList) {
                if (properties.containsKey(cv.getTerm())) {

                    cvIdMappedProperties.put(cv.getCvId().toString(),
                            properties.get(cv.getTerm()));

                }
            }

            returnVal = mapper.reader().readTree(
                    mapper.writeValueAsString(cvIdMappedProperties));

        }
        catch(Exception e) {

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return returnVal;
    }


    public static Map<String, String> mapCvIdToCvTerms(List<Cv> cvList, JsonNode propertiesJson) {

        Map<String, String> returnVal = new HashMap();

        ObjectMapper mapper = new ObjectMapper();

        try {
            for(Cv cv : cvList) {
                if(propertiesJson.has(cv.getCvId().toString())) {
                    returnVal.put(cv.getTerm(), propertiesJson.get(cv.getCvId().toString()).asText());
                }
            }
        }
        catch(Exception e) {

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return returnVal;

    }
}
