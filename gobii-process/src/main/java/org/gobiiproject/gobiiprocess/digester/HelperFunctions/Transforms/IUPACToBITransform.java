package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.utils.IUPACmatrixToBi;

import java.io.IOException;

class IUPACToBITransform extends MobileTransform {

    public void transform(String fromFile, String toFile, String errorPath) {
        try {
            IUPACmatrixToBi.convertIUPACtoBi("tab", fromFile, toFile);
        } catch (IOException e) {
            ErrorLogger.logError("IUPACToBI", "Exception opening files for IUPAC to BI conversion", e);
        }
    }
}
