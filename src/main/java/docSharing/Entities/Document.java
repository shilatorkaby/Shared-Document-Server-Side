package docSharing.Entities;

import javax.persistence.*;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    private String fileName;
    private String fileContent;


    public Document() {}

    public Document(String email, String fileName) {
        this.email = email;
        this.fileName = fileName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileContent='" + fileContent + '\'' +
                '}';
    }
}
