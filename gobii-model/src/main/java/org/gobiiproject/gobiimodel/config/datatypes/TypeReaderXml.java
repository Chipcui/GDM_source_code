package org.gobiiproject.gobiimodel.config.datatypes;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles serialization of the InputType class to/from XML.
 */
public class TypeReaderXml<T> {

    public Class<T> class_;

    public TypeReaderXml(Class<T> class_){
        this.class_=class_;
    }
    public void write(T value, String fileName) throws Exception {

        File file = new File(fileName);
        if( ! file.exists()) {
            file.createNewFile();
        }
        Marshaller marshaller = JAXBContext.newInstance(class_).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(value, file);
    }

    public T read(String fileName) throws Exception {
        File file = new File(fileName);

        Unmarshaller marshaller = JAXBContext.newInstance(class_).createUnmarshaller();
        return (T)marshaller.unmarshal(file);
    }

    /**
     * returns a map of all input types found in an XML file given a base directory
     * @param baseDir starting directory for datatypes, does not traverse filders
     * @return map containing all valud types, sorted by their InputType parameter.
     */
    public static List<File> getAllFiles(File baseDir,String extension) {
        File[] list = baseDir.listFiles();
        List<File> ret = new ArrayList<File>();
        if (list == null) return null;
        for (File f : list)
            if (f.isFile()) { //Improvised off Java's FileExtensionFilter.
                String name = f.getName();
                int i = name.lastIndexOf('.');
                if (i < 0) continue;
                if (i > name.length() - 1) continue;
                if (!name.substring(i + 1).equals("xml")) continue;
                ret.add(f);
            }
        return ret;
    }

    //TestFunction
    public static void main(String [] args){
        InputType type = new InputType();
        type.setOutTypeName("Orange");
        type.setTypeName("Banana");
        type.setSize(2);
        List transformList=new ArrayList<>();
        MatrixTransformElement a = new MatrixTransformElement();
        a.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.IUPACToBI");
        transformList.add(a);
        MatrixTransformElement b = new MatrixTransformElement();
        b.setClass_("org.gobiiproject.gobiiprocess.digest.Matrix.PythonCall");
        List<String> arguments = new ArrayList();
        arguments.add("etc/IUPACtoBI.pl");
        arguments.add("missingIndicators.txt");
        b.setArgument(arguments);
        transformList.add(b);
        type.setTransformList(transformList);

        String testLoc="C:\\Users\\Joshua\\testInputTypeFile.xml";
        TypeReaderXml<InputType> t = new TypeReaderXml<InputType>(InputType.class);
        try {
            t.write(type, testLoc);
            System.out.println(t.read(testLoc));
        }catch(Exception e){
            System.out.println("Whoops");
            System.out.println(e);
        }
    }
}