package org.gobiiproject.gobiiprocess.digester.utils.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

//Ignore if value is missed
@JsonInclude(JsonInclude.Include.NON_NULL)
// Ignore unknown values defined in JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationRules {

    private List<ValidationUnit> validations;

    public List<ValidationUnit> getValidations() {
        return validations;
    }

    public void setValidations(List<ValidationUnit> validations) {
        this.validations = validations;
    }
}
