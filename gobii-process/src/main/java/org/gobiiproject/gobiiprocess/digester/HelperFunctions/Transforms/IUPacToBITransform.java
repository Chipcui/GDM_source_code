package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.IOException;

import static org.gobiiproject.gobiiprocess.digester.utils.IUPACmatrixToBi.convertIUPACtoBi;

class IUPacToBITransform extends MobileTransform {

    public void transform(TransformArguments args,String fromFile, String toFile, String errorPath) {
        try {
            convertIUPACtoBi("tab", fromFile, toFile);
        } catch (IOException e) {
            ErrorLogger.logError("IUPACToBI", "Exception opening files for IUPAC to BI conversion", e);
        }
    }
}
