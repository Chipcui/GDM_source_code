package org.gobiiproject.gobiiprocess.digester.utils.validation;


import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

class GermplasmPropValidator extends BaseValidator {
    @Override
    void validate(ValidationUnit validationUnit, String dir) {
        ErrorLogger.logDebug("Germplasm-prop validation ", " started.");
        List<String> digestGermplasmProp = new ArrayList<>();
        if (checkForSingleFileExistence(dir, validationUnit.getDigestFileName(), digestGermplasmProp)) {
            String filePath = dir + "/" + validationUnit.getDigestFileName();
            validateRequiredColumns(filePath, validationUnit.getConditions());
            validateRequiredUniqueColumns(filePath, validationUnit.getConditions());
            for (ConditionUnit condition : validationUnit.getConditions()) {
                if (condition.type != null && condition.type.equalsIgnoreCase(ValidationConstants.FILE)) {
                    validateColumnBetweenFiles(filePath, condition);
                }
            }
        }
        ErrorLogger.logDebug("Germplasm-prop validation ", " done.");
    }


}
