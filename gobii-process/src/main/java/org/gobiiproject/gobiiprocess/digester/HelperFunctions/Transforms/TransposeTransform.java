package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.utils.TransposeMatrix;

import java.io.FileNotFoundException;

class TransposeTransform extends MobileTransform {
    private final String dest;

    public TransposeTransform(String dest) {
        this.dest = dest;
    }

    public void transform(String fromFile, String toFile, String errorPath) {
        try {
            TransposeMatrix.transposeMatrix("tab", fromFile, toFile, dest);
        } catch (FileNotFoundException e) {
            ErrorLogger.logError("Matrix Transpose", "Missing File", e);
        }
    }
}
