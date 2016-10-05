package org.gobiiproject.gobiiprocess.web;

import com.esotericsoftware.yamlbeans.YamlWriter;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;

/**
 * Created by Phil on 9/30/2016.
 */
public class YamlDump {

    private static Logger LOGGER = LoggerFactory.getLogger(YamlDump.class);


    public static void main(String[] args) {


        try {

            ContactDTO contact = new ContactDTO();
            contact.setFirstName("Nathan Sweet");
            YamlWriter writer = new YamlWriter(new FileWriter("output.yml"));
            writer.write(contact);
            System.out.print(writer.toString());

            writer.close();
        } catch(Exception e) {
            LOGGER.error("Error serializing Java objects",e);
        }
    }
}
