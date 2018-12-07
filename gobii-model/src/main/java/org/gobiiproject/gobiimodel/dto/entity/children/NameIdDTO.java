package org.gobiiproject.gobiimodel.dto.entity.children;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.headerlesscontainer.DnaSampleDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Phil on 4/8/2016.
 */
public class NameIdDTO<T> extends DTOBase implements Comparable<NameIdDTO>{

    private T queryObject;

    // entityLastModified is necessary because this class doe snot correspond to a
    // specific entity, and so it should not derive from DTOBaseAuditable
//    @JsonFormat
//            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date entityLasetModified;

    private GobiiEntityNameType gobiiEntityNameType;
    private GobiiEntityNameType gobiiFkEntityNameType;
    private Integer id = 0;
    private Integer fkId = 0;
    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEntityLasetModified() {
        return entityLasetModified;
    }

    public void setEntityLasetModified(Date entityLasetModified) {
        this.entityLasetModified = entityLasetModified;
    }

    public GobiiEntityNameType getGobiiEntityNameType() {
        return gobiiEntityNameType;
    }

    public void setGobiiEntityNameType(GobiiEntityNameType gobiiEntityNameType) {
        this.gobiiEntityNameType = gobiiEntityNameType;
    }

    public GobiiEntityNameType getGobiiFkEntityNameType() {
        return gobiiFkEntityNameType;
    }

    public void setGobiiFkEntityNameType(GobiiEntityNameType gobiiFkEntityNameType) {
        this.gobiiFkEntityNameType = gobiiFkEntityNameType;
    }

    public Integer getFkId() {
        return fkId;
    }

    public void setFkId(Integer fkId) {
        this.fkId = fkId;
    }

    public T getQueryObject() {
        return queryObject;
    }

    public void setQueryObject(T queryObject) {
        this.queryObject = queryObject;
    }

    public int compareTo(NameIdDTO compareNameIdDTO) {

        return name.compareTo(compareNameIdDTO.getName());

    }

}
