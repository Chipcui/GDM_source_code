package org.gobiiproject.gobiimodel.dto.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.utils.customserializers.UtcDateSerializer;

/**
 * All Dto classes for tables that have the created_date, modified_date, created_by,
 * and modified_by columns, only those classes, must derive from DTOBaseAuditable.
 * This inheritance strategy makes it so that such DTOs will have the methods and column
 * name annotations will be standardized. Moreover, this interface is relied upon by
 * the DtoMapAspect class such that for the DtoMap* classes that derive from DtoMap<> and
 * live in the entity.auditable namespace, the aspect will automatically call these methods
 * to maintain the date and user columns systematically.
 */
public abstract class DTOBaseAuditable extends DTOBase {


    private GobiiEntityNameType entityNameType = null;
    public DTOBaseAuditable(GobiiEntityNameType entityNameType) {
        this.entityNameType = entityNameType;
    }

    @GobiiEntityMap(paramName="createdBy", base=true)
    private Integer createdBy = null;

    @JsonSerialize(using=UtcDateSerializer.class)
    @GobiiEntityMap(paramName="createdDate", base=true)
    private Date createdDate = null;

    @GobiiEntityMap(paramName="modifiedBy", base=true)
    private Integer modifiedBy = null;

    @JsonSerialize(using=UtcDateSerializer.class)
    @GobiiEntityMap(paramName="modifiedDate", base=true)
    private Date modifiedDate = null;

    public GobiiEntityNameType getEntityNameType() {
        return this.entityNameType;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    @GobiiEntityColumn(columnName = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @GobiiEntityParam(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    @GobiiEntityColumn(columnName = "modified_by")
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
