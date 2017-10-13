package org.gobiiproject.gobiiprocess.digester.HelperFunctions.Transforms;

import org.gobiiproject.gobiimodel.config.datatypes.MatrixTransformElement;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFTransformer;
import org.springframework.security.access.method.P;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * A class representing a transformation that is not 'in place'. This transformation may take several parameters, but the
 * transformative step requires a file to operate on and a place for it to place the output file.
 */

public abstract class MobileTransform {
    /**
     * Perform transformation from 'from' location to 'to' location optionally making use of the error path provided
     * @param fromFileLocation String representation of the from location on the filesystem
     * @param toFileLocation String representation of the to location on the filesystem
     * @param errorPath place to use for temporary error files
     */
    public abstract void transform(TransformArguments args, String fromFileLocation, String toFileLocation, String errorPath);


    public static final MobileTransform stripHeader= new StripHeaderTransform();
    public static final MobileTransform IUPACToBI= new IUPacToBITransform();
    public static final MobileTransform PGArray= new PGArrayTransform();
    public static final MobileTransform VCFTransform= new VCFTransform();
    public static final MobileTransform TransposeMatrixTransform = new TransposeTransform();

    public static MobileTransform getTransformFromExecString(String exec){
        return new ExecStringTransform(exec);
    }
    public static MobileTransform getSNPTransform(String exec, String missingFile){
        return new SNPTransform(exec, missingFile);
    }

    /**
     * Given a model MatrixTransformElement, create a MobileTransform object from it with the appropriate argument list
     * @param element
     * @return
     */
    public static MobileTransform getFromMatrixElement(MatrixTransformElement element){
        Class c=null;
        try {
             c = element.getMatrixClass();
        }
        catch(Exception e){
            ErrorLogger.logError("MatrixTransformElement","Failure to get transformation from element",e);
            return null;
        }
        List<String> arguments = element.getArgument();
        if(!MobileTransform.class.isAssignableFrom(c)){
            Constructor[] constructors=c.getConstructors();
            Constructor ctor=null;
            //Find first constructor with the correct NUMBER of arguments. This is a simplified list.
            //All transforms are stringly-typed
            for(Constructor item:constructors){
                if(item.getParameterCount() == arguments.size()){
                    ctor=item;
                    break;
                }
            }
            if(ctor==null){
                ErrorLogger.logError("MatrixTransformElement","Unable to find length " + arguments.size() + " constructor for " + c.getName());
                return null;
            }
            //At this stage, we have the constructor
            MobileTransform mt=null;
            try {
                mt = (MobileTransform) ctor.newInstance(arguments.toArray());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                ErrorLogger.logError("MatrixTransformElement","Unable to instantiate instance for " + c.getName(),e);
            }
            return mt;
        }
        else {
            ErrorLogger.logError("MatrixTransformElement","Unable to convert " + c.getName() + " into a transformation");
            return null;
        }
    }

}

