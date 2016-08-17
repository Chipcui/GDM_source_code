package org.gobiiproject.gobiimodel.config;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.jndi.JndiTemplate;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


/**
 * Created by Phil on 5/5/2016.
 */
class ConfigValuesFactory {

    private static void renamePropsFile(String fqpn) {
        File propsFile = new File(fqpn);
        if(propsFile.exists()) {
            File newPropsFile = new File(fqpn + ".unused");
            propsFile.renameTo(newPropsFile);
        }
    }

    public static ConfigValues make(String configFileWebPath) throws Exception {

        ConfigValues returnVal = null;

        String fqpn = configFileWebPath;

        if (LineUtils.isNullOrEmpty(fqpn)) {
            JndiTemplate jndi = new JndiTemplate();
            fqpn = (String) jndi.lookup("java:comp/env/gobiipropsloc");
            if (LineUtils.isNullOrEmpty(fqpn)) {
                throw new Exception("JNDI configuration does specify configuration file; see user manual for configuration instructions");
            }
        }

        ConfigFileReaderXml configFileReaderXml = new ConfigFileReaderXml();
        String extension = FilenameUtils.getExtension(fqpn);
        if (extension.equals("properties")) {

            String fileNameStem = FilenameUtils.getFullPath(fqpn)
                    + FilenameUtils.getBaseName(fqpn);

            String xmlFileEquivalent = fileNameStem + ".xml";
            File xmlFile = new File(xmlFileEquivalent);
            if (xmlFile.exists()) {

                //Since we've got  an XML file, that will now be used going forward
                //mark the properties file unused

                renamePropsFile(fqpn);
                // now read our xml
                returnVal = configFileReaderXml.read(xmlFileEquivalent);

            } else {

                File propsFile = new File(fqpn);
                if (propsFile.exists()) {

                    ConfigFileReaderProps configFileReaderProps = new ConfigFileReaderProps(fqpn);
                    ConfigValues configValues = configFileReaderProps.makeConfigValues();
                    configFileReaderXml.write(configValues, xmlFileEquivalent);
                    returnVal = configFileReaderXml.read(xmlFileEquivalent);

                } else {
                    throw (new Exception("File does not exist: " + fqpn));
                }

            }
        } else {

            renamePropsFile(fqpn);
            returnVal = configFileReaderXml.read(fqpn);

        } // if else the file we got has ".properties" as extension


        return returnVal;

    } // make()

}
