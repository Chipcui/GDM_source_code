package org.gobiiproject.gobiimodel.entity;


import javax.persistence.*;

/**
 * Model for Reference Entity.
 * Represents database table reference.
 *
 */
@Entity
@Table(name = "reference")
public class Reference extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name="name")
    private String referenceName;

    @Column(name="version")
    private String version;

    @Column(name="link")
    private String link;

    @Column(name="file_path")
    private String filePath;

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
