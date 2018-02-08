package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;

/* DEPRECATED by ScriptTransform*/
class SNPTransform extends MobileTransform {
    private final String exec;
    private final String missingFile;

    public SNPTransform(String exec, String missingFile) {
        this.exec = exec;
        this.missingFile = missingFile;
    }

    public void transform(TransformArguments args, String fromFile, String toFile, String errorPath) {
        HelperFunctions.tryExec(exec + " " + fromFile + " " + missingFile + " " + toFile, errorPath + ".tfmlog", errorPath);
        rmIfExist(errorPath + ".tfmlog");
    }
}
