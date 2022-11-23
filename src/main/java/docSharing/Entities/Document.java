package docSharing.Entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    String fileName;
    String fileContent;


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

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Document{" +
                "ownerEmail='" + email + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
