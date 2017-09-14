package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFTransformer;

import java.io.File;

public class VCFTransform extends MobileTransform {

    private final File markerFile;

    public VCFTransform(String markerFile) {
        this.markerFile = new File(markerFile);
    }

    public void transform(String fromFile, String toFile, String errorPath) {
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
