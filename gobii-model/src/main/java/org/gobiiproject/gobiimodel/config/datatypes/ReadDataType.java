package org.gobiiproject.gobiimodel.config.datatypes;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * XML read and write for DigestDataType and ExtractDataType
 */
public class ReadDataType<T>{
    public Class<T> class_;
    public ReadDataType(Class<T> class_){
        this.class_=class_;
    }

    /**
     * marshall DigestType/ to xml
     * @param value DigestType
     * @param fileName
     * @throws Exception
     */
    public void write(T value, String fileName) throws Exception {

        File file = new File(fileName);
        if( ! file.exists()) {
            file.createNewFile();
        }
        Marshaller marshaller = JAXBContext.newInstance(class_).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(value, file);
    }

    /**
     * DigestType XML reader
     * @param fileName config.xml
     * @return
     * @throws Exception
     */
    public T read(String fileName) throws Exception {
        File file = new File(fileName);
        Unmarshaller marshaller = JAXBContext.newInstance(class_).createUnmarshaller();
        return (T)marshaller.unmarshal(file);
    }
}
