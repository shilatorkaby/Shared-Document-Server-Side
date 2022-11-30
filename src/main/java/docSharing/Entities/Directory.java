package docSharing.Entities;

import javax.persistence.*;

@Entity
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long fatherId;
    private String name;
    private Long docId;


    public Directory() {
    }

    public Directory(Long fatherId, String dirName) {
        this.fatherId = fatherId;
        this.name = dirName;
    }

    public Directory(Long fatherId, String name, Long fileId) {
        this.fatherId = fatherId;
        this.name = name;
        this.docId = fileId;
    }
  public Directory(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String subDirs) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "id=" + id +
                ", fatherId=" + fatherId +
                ", name='" + name + '\'' +
                ", docId=" + docId +
                '}';
    }
}
