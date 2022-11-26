package docSharing.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long fatherId;
    private String dirName;

    private Long fileId;

    @OneToMany(targetEntity=Directory.class, fetch=FetchType.EAGER)
    private List<Long> subDirs;

    public Directory() {
    }

    public Directory(Long fatherId, String dirName) {
        this.fatherId = fatherId;
        this.dirName = dirName;
    }

    public Directory(String dirName) {
        this.dirName = dirName;
    }

    public Directory(String dirName, Long fileId) {
        this.dirName = dirName;
        this.fileId = fileId;
    }

    public Directory(Long fatherId, String dirName, Long fileId) {
        this.fatherId = fatherId;
        this.dirName = dirName;
        this.fileId = fileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String subDirs) {
        this.dirName = dirName;
    }

    public List<Long> getSubDirs() {
        return subDirs;
    }

    public void setSubDirs(List<Long> subDirs) {
        this.subDirs = subDirs;
    }


}
