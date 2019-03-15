package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

//Ignore if value is missed
@JsonInclude(JsonInclude.Include.NON_NULL)
// Ignore unknown values defined in JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationUnit {
    private String digestFileName;
    private List<ConditionUnit> conditions;

    public String getDigestFileName() {
        return digestFileName;
    }

    public void setDigestFileName(String digestFileName) {
        this.digestFileName = digestFileName;
    }

    public List<ConditionUnit> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionUnit> conditions) {
        this.conditions = conditions;
    }
}
