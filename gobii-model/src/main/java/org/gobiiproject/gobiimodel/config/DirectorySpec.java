package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.simpleframework.xml.Element;

public class DirectorySpec {

    public DirectorySpec() {}

    public DirectorySpec(GobiiFileProcessDir gobiiFileProcessDir, String relativePath, boolean isCropRelative) {
        this.gobiiFileProcessDir = gobiiFileProcessDir;
        this.relativePath = relativePath;
        this.isCropRelative = isCropRelative;
    }

    @Element(required = false)
    GobiiFileProcessDir gobiiFileProcessDir;

    @Element(required = false)
    String relativePath;

    @Element(required = false)
    boolean isCropRelative;


    public GobiiFileProcessDir getGobiiFileProcessDir() {
        return gobiiFileProcessDir;
    }

    public void setGobiiFileProcessDir(GobiiFileProcessDir gobiiFileProcessDir) {
        this.gobiiFileProcessDir = gobiiFileProcessDir;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public boolean isCropRelative() {
        return isCropRelative;
    }

    public void setCropRelative(boolean cropRelative) {
        isCropRelative = cropRelative;
    }
}
