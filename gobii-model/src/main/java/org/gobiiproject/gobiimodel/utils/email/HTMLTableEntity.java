package org.gobiiproject.gobiimodel.utils.email;

import java.util.ArrayList;
import java.util.Arrays;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class created in laziness. Accepts arbitrary length 'value' arguments and creates HTML tables out of them.
 * Note: Oddity in getHTMLTable - names of fields aren't stored in the HTMLTableEntity class, so you have to specify the
 * column names in getHTMLTable. Saves me some coding, but may be a pitfall since String... args may be empty. (Which throws a runtimeexception)
 */
public class HTMLTableEntity {
    List<String> fields=new ArrayList<>();

    /**
     * Creates an HTMLTableEntity, specifying a row in an HTML table. Pass parameters in order as table elements.
     * @param values Ordered List of Values for the entity to possess
     */
    public HTMLTableEntity(String... values){
        fields= Arrays.asList(values);
    }

    /**
     * Returns header row <including <b>th</b> columns.
     * @param fieldNames List of table header names
     * @return valid table starting row elements
     */
    private static String getHTMLTableStart(String width, String... fieldNames){
        if(fieldNames.length==0){
            throw new RuntimeException("Invalid Table Construction");
        }
        StringBuilder sb=new StringBuilder();
        sb.append("<table style=width:"+width+"%, border=\"1\"><tr>");
        for(String name:fieldNames) {
            sb.append("<th align=\"left\">").append(name).append("</th>");//Left align to better align tables
        }
        return sb.append("</tr>").toString();
    }
    /**
     * The end of the table. (Just slashTable)
     */
    private static String getHTMLTableEnd(){
        return "</table>";
    }
    /**
     * Magic. Returns a row containing every field, in order.
     */
    private String getHTMLTableRow(){
        StringBuilder sb=new StringBuilder();
        sb.append("<tr>");
        for(String value:fields) {
            sb.append("<td>").append(value).append("</td>");
        }
        return sb.append("</tr>").toString();
    }

    /**
     * Creates a valid HTML table given labels and 'HTMLTableEntity' objects.
     * @param contents HTMLTableEntity objects, one for each row.
     * @param labels String labels for each value, in order.
     * @return Valid HTML table based on the entities and labels
     */
    public static String getHTMLTable(List<HTMLTableEntity> contents,String width, String... labels){
        StringBuilder sb = new StringBuilder();
       sb.append(getHTMLTableStart(width, labels));
       for(HTMLTableEntity content:contents){
           sb.append(content.getHTMLTableRow());
       }
       sb.append(getHTMLTableEnd());
       return sb.toString();
   }

    /**
     * Creates a HTML table with links, for paths
     * @param contents
     * @param width
     * @param labels
     * @return
     */
   public static String getLiveLinkTable(List<HTMLTableEntity> contents, String width, String... labels){
        StringBuilder sb = new StringBuilder();
        sb.append(getHTMLTableStart(width, labels));
        for(HTMLTableEntity content:contents){
            sb.append(content.getHTMLLinkTableRow());
        }
        return sb.toString();
   }

    /**
     * Add a link under <a> with </ahref= </a>
     * @return
     */
    private String getHTMLLinkTableRow(){
        StringBuilder sb=new StringBuilder();
        sb.append("<tr>");
        int i=0;
        for(String value:fields) {
            i++;
            if(i==2) {
                sb.append("<td><a href=\"").append(getLiveLink(value)).append("\">").append(value).append("</a>").append("</td>");
            }
            else{
                sb.append("<td>").append(value).append("</td>");
            }
        }
        return sb.append("</tr>").toString();
    }

    /**
     * Converts String path to elfinder live link
     * @param path
     * @return
     */
    private String getLiveLink(String path) {
//        String configFilePath = "/home/sivasubramani/Projects/GOBII/Bitbucket/VM_Config/gobii_bundle/config/gobii-web.xml";
        String configFilePath;
        configFilePath = "/data/gobii_bundle/config/gobii-web.xml";
        String elFindURL;
        ConfigSettings config = new ConfigSettings(configFilePath);
        String domain = config.getElfinderUrl();
        if(path.endsWith("/")){
            elFindURL = domain + "elfinder.html#elf_";
        }
        else{
            elFindURL = domain + "php/connector.minimal.php?cmd=file&target=";
        }
        String rootPath = config.getFileSystemRoot();
        String encodedPath;
        encodedPath = Base64.getEncoder().encodeToString(path.replaceAll(rootPath,"gobii_bundle").getBytes());

        Pattern pattern = Pattern.compile("\\+");
        Matcher match = pattern.matcher(encodedPath);
        encodedPath = match.replaceAll("-");

        pattern = Pattern.compile("\\/");
        match = pattern.matcher(encodedPath);
        encodedPath = match.replaceAll("_");

        pattern = Pattern.compile("=");
        match = pattern.matcher(encodedPath);
        encodedPath = match.replaceAll(".");

        pattern = Pattern.compile("\\.+$");
        match = pattern.matcher(encodedPath);
        String replacedEncodedPath = match.replaceAll("");

        if(path.endsWith("/")){
            replacedEncodedPath = replacedEncodedPath.substring(0,replacedEncodedPath.length()-2);
        }

        return  elFindURL + "l1_" + replacedEncodedPath;

    }

}
