package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiiprocess.digester.HelperFunctions.PGArray;

class PGArrayTransform extends MobileTransform {
    public void transform(TransformArguments args, String fromFile, String toFile, String errorPath) {
        new PGArray(fromFile, toFile, "alts").process();
    }
}
