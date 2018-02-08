package org.gobiiproject.gobiimodel.config.datatypes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.List;

/**
 * A references a MatrixTransformElement(non-model) object and constructor arguments to be made to it.
 * @author jdl232
 */
@XmlRootElement(name = "Transform")
@XmlType(propOrder={"class_","argument"})
public class MatrixTransformElement {
    @XmlElement(name="class")
    public String getClass_() {
        return class_;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public List<String> getArgument() {
        return argument;
    }

    public void setArgument(List<String> argument) {
        this.argument = argument;
    }

    private String class_;
    private List<String> argument;

    public Class getMatrixClass() throws ClassNotFoundException {
        return Class.forName(class_);
    }
    public String toString(){
        if(argument==null){
            return "["+class_+"]";
        }
        return "["+class_+" : " + Arrays.toString(argument.toArray())+"]";
    }
}
