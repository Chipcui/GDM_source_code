package org.gobiiproject.gobiimodel.utils.email;


import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.apache.commons.lang.StringEscapeUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiimodel.utils.links.GetLinks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.sizeToReadable;

/*
 *  GOBII - Process mail message format.  (Hopefully to replace DigesterMessage.java)
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class ProcessMessage extends MailMessage {
    private String statusLine;
    private String errorLine;
    private String tableLine;
    private String extractCriteriaLine;
    private String entityLine;
    private String identifierLine;
    private String pathsLine;
    private String confidentialyMessage;
    File fPath;
    private String color;
    final String redColor = "#E74C3C";
    final String greenColor = "#2ECC71";
    final String tableLineWidth = "40";
    final String entityLineWidth = "40";
    final String identifierLineWidth = "40";
    final String extractCriteriaLineWidth = "40";
    final String pathsLineWidth = "65";
    List<HTMLTableEntity> entries=new ArrayList<>();
    List<HTMLTableEntity> identifiers=new ArrayList<>();
    List<HTMLTableEntity> extractCriteria=new ArrayList<>();
    List<HTMLTableEntity> entities=new ArrayList<>();
    List<HTMLTableEntity> paths=new ArrayList<>();
//    ConfigSettings config = new ConfigSettings();

    /**
     * Sets the BODY of the mail message with TABLEs
     * @param jobName Name of the JOB ([GOBII - Extractor]: crop - extraction of "xxxx") 
     * @param shortError error message, 100 or less charectors
     * @param success If the job is success/failed (true/false)
     * @param longError Long error message
     * @return handle to this object
     */
    public ProcessMessage setBody(String jobName, String type, long time, String shortError,boolean success, String longError){
        this.setStatus(success);
        this.setSubject(jobName+(success?" Success":" Failed"));
        this.errorLine=shortError;
        this.color = (success ? greenColor:redColor);
        if(!entries.isEmpty()) {
            tableLine = HTMLTableEntity.getHTMLTable(entries, tableLineWidth,"Table","Total in File", "Total Loaded","Total Existing","Total Invalid");
            entries.clear();
        }
        if(!identifiers.isEmpty()) {
            identifierLine = HTMLTableEntity.getHTMLTable(identifiers, identifierLineWidth,"Identifier Type","Name","ID");
            identifiers.clear();
        }
        if(!extractCriteria.isEmpty()) {
            extractCriteriaLine = HTMLTableEntity.getHTMLTable(extractCriteria, extractCriteriaLineWidth, "Extraction Criteria");
        }
        if(!entities.isEmpty()) {
            entityLine = HTMLTableEntity.getHTMLTable(entities, entityLineWidth,"Type","Count");
            entities.clear();
        }
        if(!paths.isEmpty()) {
            pathsLine = HTMLTableEntity.getHTMLTable(paths, pathsLineWidth,"File Type","Path","Size");
            paths.clear();
        }

        String line="<br/>";
        StringBuilder body=new StringBuilder();
        body.append("<html><head><style>table{font-family:arial,sans-serif;border-collapse:collapse;width:60%;}th{background-color:" + color + ";border:1px solid #dddddd;text-align:left;padding:8px;}td{border:1px solid #dddddd;text-align:left;padding:8px;}tr:nth-child(even){background-color:lightblue;}</style></head><body>");

        if(type!=null){
            body.append("<font size = 4><b>"+type+"</b></font> (Duration: "+(time>=1000?time/1000+"secs":time+"ms")+")<br/><br/>");
        }
        else{
            body.append("<br/><br/>");
        }

        body.append(statusLine+line);
        if(errorLine!=null)body.append(errorLine+line);
        body.append(line);
        if(identifierLine!=null)body.append(identifierLine+line);
        if(extractCriteriaLine!=null)body.append(extractCriteriaLine+line);
        if(entityLine!=null)body.append(entityLine+line);
        if(tableLine!=null)body.append(tableLine+line);
        if(pathsLine!=null)body.append(pathsLine+line);
        if(longError!=null)body.append(longError);
        body.append("</html>");
        this.setBody(body.toString());
        return this;
    }
    
    
    /**
     * Add an entry to the intermediate File table
     * @param tableName Name of table
     * @param fileCount Unique entry count in file
     * @param loadCount Count of loaded entries
     * @param existCount Count of duplicate entries
     * @return this object
     */
    public ProcessMessage addEntry(String tableName,String fileCount, String loadCount, String existCount, String invalidCount){
        entries.add(new HTMLTableEntity(tableName,fileCount,loadCount,existCount, invalidCount));
        return this;
    }
    
    /**
     * Add an entry to the Identifiers table
     * @param type Type of entry (Platform, Dataset, etc.)
     * @param name Name of entry
     * @param id Identifier of entry
     * @return this object
     */
    public ProcessMessage addIdentifier(String type,String name, String id){
        if((name==null) && ((id==null || id.equals("null"))))return this;
        if(id==null){
            identifiers.add(new HTMLTableEntity(type,escapeHTML(name),""));
        }
        else {
            identifiers.add(new HTMLTableEntity(type, escapeHTML(name), id));
        }
         return this;
    }
    
    public ProcessMessage addIdentifier(String type, PropNameId identifier){
        if(identifier==null)return this;//Don't add a null ID to the table
        return addIdentifier(type,escapeHTML(identifier.getName()),identifier.getId()+"");
    }
    
     /**
     * Add an entry to the Entity table (just with two strings)
     * @param type Type of entry (Platform, Dataset, etc.)
     * @param name Name of entry
     * @return this object
     */
    public ProcessMessage addEntity(String type,String name){
        if(name==null)return this;
        entities.add(new HTMLTableEntity(type,escapeHTML(name)));
        return this;
    }

    public ProcessMessage addCriteria(String type, String name){
        if(name == null)return this;
        extractCriteria.add(new HTMLTableEntity(type, name));
        return this;
    }

    public ProcessMessage addCriteria(String type, PropNameId criteria){
        if(criteria == null)return this;
        if(criteria.getName() == null) return this; // Don't add null values to the table
        extractCriteria.add(new HTMLTableEntity(type, criteria.getName()));
        return this;
    }

    public ProcessMessage addFolderPath(String type,String path) {
        paths.add(new HTMLTableEntity(type,path,""));
        return this;
    }
        /**
         * Add item to the filepaths entry
         * @param type type of file
         * @param path filepath
         * #param alwaysShow to always show the path, even if no object is there
         * @return this object
         */
    public ProcessMessage addPath(String type,String path, boolean alwaysShow, ConfigSettings config) throws Exception {

        String pathLine = path;
        if(config.getGlobalServer(ServerType.OWN_CLOUD).isActive()){
            if(path.endsWith("/")){
                pathLine = path + "\n" + GetLinks.getOwncloudURL(path, config);
            }
            else{
                pathLine = path + "\n" + GetLinks.getLink(path, config);
            }
        }
    	if(new File(path).length() > 1){
    		paths.add(new HTMLTableEntity(type, escapeHTML(pathLine), HelperFunctions.sizeToReadable(new File(path).length())));
    	}
    	else if(alwaysShow){
    	    paths.add(new HTMLTableEntity(type,escapeHTML(pathLine),""));
        }
        return this;
    }

    /**
     * As ProcessMessage(type,path,False)
     * @param type type of file
     * @param path filepath
     * @return
     */
    public ProcessMessage addPath(String type, String path, ConfigSettings config) throws Exception {
        return addPath(type,path,false,config);
    }

    
    /**
     * Set status line in HTML format. format includes font size and color
     * @param status
     * @return
     */
    private ProcessMessage setStatus(boolean status) {
        statusLine = "Status: " + (status ?
                "<font color="+greenColor+" size=4><b>SUCCESS</b></font>" :
                "<font color="+redColor+" size=4><b>Failed</b></font>");
        return this;
    }

    public ProcessMessage addConfidentialityMessage(String confidentialyMessage){
        this.setConfidentialityMessage("<font color="+redColor+"><b></br>"+escapeHTML(confidentialyMessage)+"</br></b></font>");
        return this;
    }

    public ProcessMessage addBody(String lastBody, String jobName, String type, long time, String shortError,boolean success, String longError){
        this.errorLine=shortError;
        this.color = (success ? greenColor:redColor);
        if(!entries.isEmpty()) {
            tableLine = HTMLTableEntity.getHTMLTable(entries, tableLineWidth,"Table","Total in File", "Total Loaded","Total Existing","Total Invalid");
        }
        if(!identifiers.isEmpty()) {
            identifierLine = HTMLTableEntity.getHTMLTable(identifiers, identifierLineWidth,"Identifier Type","Name","ID");
        }
        if(!entities.isEmpty()) {
            entityLine = HTMLTableEntity.getHTMLTable(entities, entityLineWidth,"Type","Count");
        }
        if(!paths.isEmpty()) {
            pathsLine = HTMLTableEntity.getHTMLTable(paths, pathsLineWidth,"File Type","Path","Size");
        }

        String line="<br>";
        StringBuilder body=new StringBuilder();
        body.append("<html><head><style>table{font-family:arial,sans-serif;border-collapse:collapse;width:60%;}th{background-color:" + color + ";border:1px solid #dddddd;text-align:left;padding:8px;}td{border:1px solid #dddddd;text-align:left;padding:8px;}tr:nth-child(even){background-color:lightblue;}</style></head><body>");

        if(type!=null){
            body.append("<font size = 4><b>"+type+"</b></font> (Duration: "+(time>=1000?time/1000+"secs":time+"ms")+")<br><br>");
        }
        else{
            body.append("<br><br>");
        }

        body.append(statusLine+line);
        if(errorLine!=null)body.append(errorLine+line);
        body.append(line);
        if(identifierLine!=null)body.append(identifierLine+line);
        if(entityLine!=null)body.append(entityLine+line);
        if(tableLine!=null)body.append(tableLine+line);
        if(pathsLine!=null)body.append(pathsLine+line);

        if(longError!=null)body.append(longError);
        body.append("</html>");
        this.setBody(lastBody + body.toString());
        return this;
    }

    public ProcessMessage addProcessPath(String lastBody, boolean active){
        if(!paths.isEmpty()) {
            pathsLine = HTMLTableEntity.getHTMLTable(paths, pathsLineWidth,"File Type","Path","Size");
        }
        String line="<br>";
        StringBuilder body=new StringBuilder();
        body.append("<html><head><style>table{font-family:arial,sans-serif;border-collapse:collapse;width:60%;}th{background-color:" + color + ";border:1px solid #dddddd;text-align:left;padding:8px;}td{border:1px solid #dddddd;text-align:left;padding:8px;}tr:nth-child(even){background-color:lightblue;}</style></head><body>");
        if(pathsLine!=null)body.append(pathsLine +line);
        if(active){
            body.append("<font size=6 color=red><b>WARNING: Clicking links might force you to download bigger files.</b></font>" + line);
        }
        this.setBody(lastBody + body.toString());
        return this;
    }

    public String escapeHTML(String strToHTML){
        if(strToHTML.contains("http")){
            strToHTML.replace("http","URL");
        }
        return StringEscapeUtils.escapeHtml(strToHTML);
    }
}

