package org.gobiiproject.gobiimodel.config.datatypes;

import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
@XmlRootElement(name = "OutputType")
@XmlType(propOrder={"typeName","transformList"})
public class OutputType {
    //Size, in bytes, of storage
    private String typeName;
    private List<MatrixTransformElement> transformList=new ArrayList<MatrixTransformElement>();

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @XmlElement(name="transform")
    public List<MatrixTransformElement> getTransformList() {
        return transformList;
    }

    public void setTransformList(List<MatrixTransformElement> transformList) {
        this.transformList = transformList;
    }


    /**
     * returns a map of all output types found in an XML file given a base directory
     * @param baseDir starting directory for datatypes, does not traverse folders
     * @return map containing all valid types, sorted by their OutputType parameter.
     */
    public static Map<String,OutputType> getAllOutputTypes(File baseDir){
        TypeReaderXml<OutputType> outputTypeReader= new TypeReaderXml<>(OutputType.class);
        List<File> list = FileSystemInterface.getAllFiles(baseDir,"xml");
        Map<String,OutputType> map=new HashMap<String,OutputType>();
        for(File f:list){
            OutputType t = null;
            try {
                t = outputTypeReader.read(f.getAbsolutePath());
            } catch (Exception e) {
                ErrorLogger.logWarning("OutputTypeReader", "Invalid type found at " + f.getAbsolutePath() + "\n"+e.getMessage());
            }
            if(t!=null)map.put(t.getTypeName(),t);
        }
        return map;
    }


    public static void testMarshallOutputType() throws JAXBException {
        OutputType type = new OutputType();
        type.typeName="Orange";
        type.transformList=new ArrayList<>();
        MatrixTransformElement a = new MatrixTransformElement();
        a.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.AddSlashes");
        type.transformList.add(a);
        MatrixTransformElement b = new MatrixTransformElement();
        b.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.PythonCall");
        List<String> arguments = new ArrayList();
        arguments.add("etc/IUPACtoBI.pl");
        arguments.add("missingIndicators.txt");
        b.setArgument(arguments);
        type.transformList.add(b);
        Marshaller marshaller = JAXBContext.newInstance(OutputType.class).createMarshaller();
       // JAXBElement<InputType> element
        //        = new JAXBElement<InputType>("InputType", InputType.class, null, type);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(type, System.out);
    }
    public static void main(String[] args) throws JAXBException {
        testMarshallOutputType();
    }
}
/*Example output-
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<OutputType>
    <typeName>Orange</typeName>
    <transform>
        <class>org.gobiiproject.gobiiprocess.digest.Matrix.AddSlashes</class>
    </transform>
    <transform>
        <class>org.gobiiproject.gobiiprocess.digest.Matrix.PythonCall</class>
        <argument>etc/IUPACtoBI.pl</argument>
        <argument>missingIndicators.txt</argument>
    </transform>
</OutputType>
 */