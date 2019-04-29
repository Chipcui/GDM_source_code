package org.gobiiproject.gobiidao.entity.pojos;
// Generated Mar 31, 2016 1:44:38 PM by Hibernate Tools 3.2.2.GA


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Germplasm generated by hbm2java
 */
@Entity
@Table(name="germplasm"
    ,schema="public"
)
public class Germplasm  implements java.io.Serializable {


     private int germplasmId;
     private String germplasmName;
     private String germplasmCode;
     private int speciesId;
     private Integer germplasmTypeId;
     private int createdBy;
     private Date createdDate;
     private int modifiedBy;
     private Date modifiedDate;
     private int status;

    public Germplasm() {
    }

	
    public Germplasm(int germplasmId, String germplasmName, int speciesId, int createdBy, Date createdDate, int modifiedBy, Date modifiedDate, int status) {
        this.germplasmId = germplasmId;
        this.germplasmName = germplasmName;
        this.speciesId = speciesId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.status = status;
    }
    public Germplasm(int germplasmId, String germplasmName, String germplasmCode, int speciesId, Integer germplasmTypeId, int createdBy, Date createdDate, int modifiedBy, Date modifiedDate, int status) {
       this.germplasmId = germplasmId;
       this.germplasmName = germplasmName;
       this.germplasmCode = germplasmCode;
       this.speciesId = speciesId;
       this.germplasmTypeId = germplasmTypeId;
       this.createdBy = createdBy;
       this.createdDate = createdDate;
       this.modifiedBy = modifiedBy;
       this.modifiedDate = modifiedDate;
       this.status = status;
    }
   
     @Id 
    
    @Column(name="germplasm_id", unique=true, nullable=false)
    public int getGermplasmId() {
        return this.germplasmId;
    }
    
    public void setGermplasmId(int germplasmId) {
        this.germplasmId = germplasmId;
    }
    
    @Column(name="germplasm_name", nullable=false)
    public String getGermplasmName() {
        return this.germplasmName;
    }
    
    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }
    
    @Column(name="germplasm_code")
    public String getGermplasmCode() {
        return this.germplasmCode;
    }
    
    public void setGermplasmCode(String germplasmCode) {
        this.germplasmCode = germplasmCode;
    }
    
    @Column(name="species_id", nullable=false)
    public int getSpeciesId() {
        return this.speciesId;
    }
    
    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }
    
    @Column(name="germplasm_type_id")
    public Integer getGermplasmTypeId() {
        return this.germplasmTypeId;
    }
    
    public void setGermplasmTypeId(Integer germplasmTypeId) {
        this.germplasmTypeId = germplasmTypeId;
    }
    
    @Column(name="created_by", nullable=false)
    public int getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="created_date", nullable=false, length=13)
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    @Column(name="modified_by", nullable=false)
    public int getModifiedBy() {
        return this.modifiedBy;
    }
    
    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="modified_date", nullable=false, length=13)
    public Date getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    @Column(name="status", nullable=false)
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }




}

