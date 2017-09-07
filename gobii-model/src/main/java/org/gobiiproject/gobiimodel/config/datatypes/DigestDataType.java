package org.gobiiproject.gobiimodel.config.datatypes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * XML elements type objects for Digester.
 */
@XmlRootElement(name = "DigestDataType")
@XmlType(propOrder={"typeName","outTypeName","size","transformList"})
public class DigestDataType {

    String typeName;
    int size;
    List<MatrixTransformElement> transformList=new ArrayList<MatrixTransformElement>();
    MatrixTransformElement transform;
    String outTypeName;

    @XmlElement(name="typeName")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @XmlElement(name="size")
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

    @XmlElement(name="outTypeName")
    public String getOutTypeName() {
        return outTypeName;
    }

    public void setOutTypeName(String outTypeName) {
        this.outTypeName = outTypeName;
    }
}
