package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.io.FileNotFoundException;

import static org.gobiiproject.gobiiprocess.digester.utils.TransposeMatrix.transposeMatrix;

class TransposeTransform extends MobileTransform {

    public TransposeTransform() {
    }

    public void transform(TransformArguments args, String fromFile, String toFile, String errorPath) {
        try {
            transposeMatrix("tab", fromFile, toFile, new File(new File(fromFile).getParentFile(),"transpose").toString());
        } catch (FileNotFoundException e) {
            ErrorLogger.logError("Matrix Transpose", "Missing File", e);
        }
    }
}
