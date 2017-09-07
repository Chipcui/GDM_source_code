package org.gobiiproject.gobiimodel.config.datatypes;

import java.util.ArrayList;
import java.util.List;

/*
* Test class to generate the transformElement config XML files.
* */
public class MakeTransformElements {

    public static void main(String[] args) throws Exception {
        makeXML();
    }
    public static void makeXML() throws Exception {
        DigestDataType type = new DigestDataType();
        ReadDataType<DigestDataType> DigestDataType = new ReadDataType<>(DigestDataType.class);
        type.typeName="NUCLEOTIDE_2_LETTER";
        type.outTypeName="BI";
        type.size=2;
        type.transformList=new ArrayList<>();
        MatrixTransformElement a = new MatrixTransformElement();
        a.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.PythonCall");
        List<String> arguments = new ArrayList();
        arguments.add("etc/SNPSepRemoval.py");
        arguments.add("etc/missingIndicators.txt");
        a.setArgument(arguments);
        type.transformList.add(a);
        String fName = "/home/sivasubramani/NUCLEOTIDE_2_LETTER_2_BI.xml";
        DigestDataType.write(type,fName);
    }

}

