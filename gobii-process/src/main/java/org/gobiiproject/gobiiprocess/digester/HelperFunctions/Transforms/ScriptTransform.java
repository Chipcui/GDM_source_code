package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;


public class ScriptTransform extends MobileTransform {
    private final String exec;
    private final String missingFile;

    /**
     *
     * @param exec String to execute with parameters from file and to file
     * @param missingFile if not null, added between from and to file - to account for legacy SNPSEPREM file
     */
    public ScriptTransform(String exec, String missingFile) {
        this.exec = exec;
        this.missingFile = missingFile;
    }
    public ScriptTransform(String exec) {
        this(exec,null);
    }

    public void transform(TransformArguments args, String fromFile, String toFile, String errorPath) {
        String baseDir=args.scriptDir;
        String missingFileParam="";
        if(missingFile!=null){
            missingFileParam=baseDir+"/"+missingFile+" ";
        }
        String execParam = baseDir+"/"+exec;
        if(exec.startsWith("python")){
            execParam = "python " + baseDir+"/"+exec.substring(7); /*that's 'python ' that we're removing*/
        }
        HelperFunctions.tryExec(execParam + " " + fromFile +  " " + missingFileParam  + toFile, errorPath + ".tfmlog", errorPath);
        rmIfExist(errorPath + ".tfmlog");
    }
}
