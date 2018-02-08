package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFTransformer;

import java.io.File;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;


class VCFTransform extends MobileTransform {

    public VCFTransform() {
    }

    public void transform(TransformArguments args, String fromFile, String toFile, String errorPath){
        File markerFile=args.markerFile;
        String markerFilename = markerFile.getAbsolutePath();
        String markerTmp = new File(markerFile.getParentFile(), "marker.mref").getAbsolutePath();
        try {
            VCFTransformer.generateMarkerReference(markerFilename, markerTmp, errorPath);
            new VCFTransformer(markerTmp, fromFile, toFile);
        } catch (Exception e) {
            ErrorLogger.logError("VCFTransformer", "Failure loading dataset", e);
        }
    }
}
