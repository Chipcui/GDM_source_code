package org.gobiiproject.gobiimodel.utils.email;

import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;

import java.util.ArrayList;
import java.util.List;


public class ProcessMessage extends MailMessage {
    String statusLine;
    String errorLine;
    String tableLine;
    String entityLine;
    String identifierLine;
    String pathsLine;
    String color;
    final String redColor = "#E74C3C";
    final String greenColor = "#2ECC71";
    List<HTMLTableEntity> entries=new ArrayList<>();
    List<HTMLTableEntity> identifiers=new ArrayList<>();
    List<HTMLTableEntity> entities=new ArrayList<>();
    List<HTMLTableEntity> paths=new ArrayList<>();
    
    
    public ProcessMessage setBody(String jobName, String shortError,boolean success, String longError){
        this.setStatus(success);
        this.setSubject(jobName+(success?" Success":" Error"));
        this.errorLine=shortError;
        this.color = (success ? greenColor:redColor);
        if(!entries.isEmpty()) {
            tableLine = HTMLTableEntity.getHTMLTable(entries,"Table","Total in File", "Total Loaded","Total Existing","Total Invalid");
        }
        if(!identifiers.isEmpty()) {
            identifierLine = HTMLTableEntity.getHTMLTable(identifiers,"Identifier Type","Name","ID");
        }
        if(!entities.isEmpty()) {
            entityLine = HTMLTableEntity.getHTMLTable(entities,"Identifier Type","Name");
        }
        if(!paths.isEmpty()) {
            pathsLine = HTMLTableEntity.getHTMLTable(paths,"File Type","Path");
        }

        String line="<br/>";
        StringBuilder body=new StringBuilder();
        body.append("<html><head><style>table{font-family:arial,sans-serif;border-collapse:collapse;width:60%;}th{background-color:" + color + ";border:1px solid #dddddd;text-align:left;padding:8px;}td{border:1px solid #dddddd;text-align:left;padding:8px;}tr:nth-child(even){background-color:lightblue;}</style></head><body>");
        body.append(statusLine+line);
        if(errorLine!=null)body.append(errorLine+line);
        body.append(line);
        if(identifierLine!=null)body.append(identifierLine+line);
        if(entityLine!=null)body.append(entityLine+line);
        if(tableLine!=null)body.append(tableLine+line);
        if(pathsLine!=null)body.append(pathsLine+line);
        if(longError!=null)body.append(longError);
        body.append("</html>");
        this.setBody(body.toString());
        return this;
    }
    
    public ProcessMessage addEntry(String tableName,String fileCount, String loadCount, String existCount, String invalidCount){
        entries.add(new HTMLTableEntity(tableName,fileCount,loadCount,existCount, invalidCount));
        return this;
    }
    
    public ProcessMessage addIdentifier(String type,String name, String id){
        if((name==null) && ((id==null || id.equals("null"))))return this;
        identifiers.add(new HTMLTableEntity(type,name,id));
        return this;
    }
    
    public ProcessMessage addIdentifier(String type, GobiiFilePropNameId identifier){
        if(identifier==null)return this;//Don't add a null ID to the table
        return addIdentifier(type,identifier.getName(),identifier.getId()+"");
    }
    
    public ProcessMessage addEntity(String type,String name){
        if(name==null)return this;
        entities.add(new HTMLTableEntity(type,name));
        return this;
    }
    
    public ProcessMessage addEntity(String type, GobiiFilePropNameId entity){
        if(entity==null)return this;//Don't add a null ID to the table
        return addEntity(type,entity.getName()+"");
    }
      
    public ProcessMessage addPath(String type,String path){
        paths.add(new HTMLTableEntity(type,path));
        return this;
    }
    private ProcessMessage setStatus(boolean status) {
        statusLine = "Status: " + (status ?
                "<font color="+greenColor+" size=4><b>SUCCESS</b></font>" :
                "<font color="+redColor+" size=4><b>ERROR</b></font>");
        return this;
    }
    
}
