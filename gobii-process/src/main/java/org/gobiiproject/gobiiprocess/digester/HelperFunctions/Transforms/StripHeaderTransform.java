package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

class StripHeaderTransform extends MobileTransform {
    public void transform(String fromFile, String toFile, String errorPath) {
        HelperFunctions.tryExec("tail -n +2 ", toFile, errorPath, fromFile);
    }
}
