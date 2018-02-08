package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;

class ExecStringTransform extends MobileTransform {
    private final String exec;

    public ExecStringTransform(String exec) {
        this.exec = exec;
    }

    public void transform(TransformArguments args, String fromFile, String toFile, String errorPath) {
        HelperFunctions.tryExec(exec + " " + fromFile + " " + toFile, errorPath + ".tfmlog", errorPath);
        rmIfExist(errorPath + ".tfmlog");
    }
}
