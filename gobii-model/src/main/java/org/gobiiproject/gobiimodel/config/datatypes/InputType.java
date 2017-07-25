package org.gobiiproject.gobiimodel.config.datatypes;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

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