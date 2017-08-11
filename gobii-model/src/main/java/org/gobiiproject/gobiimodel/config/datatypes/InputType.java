package org.gobiiproject.gobiimodel.config.datatypes;

import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Everything a Digest needs to understand types. Input Matrix Transformations, if applicable, size to store in HDF5,
 * and matrix/properties
 */
@XmlRootElement(name = "InputType")
@XmlType(propOrder={"typeName","outTypeName","size","transformList"})
public class InputType {
    //Size, in bytes, of storage
    private String typeName;
    private int size;
    private List<MatrixTransformElement> transformList=new ArrayList<MatrixTransformElement>();
    //Nullable, output type name
    private String outTypeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    @XmlElement(name="transform")
    public List<MatrixTransformElement> getTransformList() {
        return transformList;
    }

    public void setTransformList(List<MatrixTransformElement> transformList) {
        this.transformList = transformList;
    }

    public String getOutTypeName() {
        return outTypeName;
    }

    public void setOutTypeName(String outTypeName) {
        this.outTypeName = outTypeName;
    }

    @Override
    public String toString() {
        return "InputType{" +
                "typeName='" + typeName + '\'' +
                ", size=" + size +
                ", transformList=" + transformList +
                ", outTypeName='" + outTypeName + '\'' +
                '}';
    }

    /**
     * returns a map of all input types found in an XML file given a base directory
     * @param baseDir starting directory for datatypes, does not traverse folders
     * @return map containing all valid types, sorted by their InputType parameter.
     */
    public static Map<String,InputType> getAllInputTypes(File baseDir){
        TypeReaderXml<InputType> outputTypeReader= new TypeReaderXml<>(InputType.class);
        List<File> list = FileSystemInterface.getAllFiles(baseDir,"xml");
        Map<String,InputType> map=new HashMap<String,InputType>();
        for(File f:list){
            InputType t = null;
            try {
                t = outputTypeReader.read(f.getAbsolutePath());
            } catch (Exception e) {
                ErrorLogger.logWarning("InputTypeReader", "Invalid type found at " + f.getAbsolutePath() + "\n"+e.getMessage());
            }
            if(t!=null)map.put(t.getTypeName(),t);
        }
        return map;
    }
    public static void testMarshallInputType() throws JAXBException {
        InputType type = new InputType();
        type.outTypeName="Orange";
        type.typeName="Banana";
        type.size=2;
        type.transformList=new ArrayList<>();
        MatrixTransformElement a = new MatrixTransformElement();
        a.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.IUPACToBI");
        type.transformList.add(a);
        MatrixTransformElement b = new MatrixTransformElement();
        b.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.PythonCall");
        List<String> arguments = new ArrayList();
        arguments.add("etc/IUPACtoBI.pl");
        arguments.add("missingIndicators.txt");
        b.setArgument(arguments);
        type.transformList.add(b);
        Marshaller marshaller = JAXBContext.newInstance(InputType.class).createMarshaller();
       // JAXBElement<InputType> element
        //        = new JAXBElement<InputType>("InputType", InputType.class, null, type);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(type, System.out);
    }
    public static void main(String[] args) throws JAXBException {
        testMarshallInputType();
    }
}
/*Example output-
 *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 *<InputType>
 *<typeName>Banana</typeName>
 *<outTypeName>Orange</outTypeName>
 *<size>2</size>
 *<transform>
 *<class>org.gobiiproject.gobiiprocess.digest.Matrix.IUPACToBI</class>
 *</transform>
 *<transform>
 *<class>org.gobiiproject.gobiiprocess.digest.Matrix.PythonCall</class>
 *<argument>etc/IUPACtoBI.pl</argument>
 *<argument>missingIndicators.txt</argument>
 *</transform>
 *</InputType>
 */