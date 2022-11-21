package docSharing.Entities;
import javax.persistence.*;
import java.util.HashMap;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;

    String fileName;
    HashMap<User,UserRole> activeUsers;

    String fileContent;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Document() {
        this.activeUsers = new HashMap<>();
    }

    public Document(String name) {
        this.fileName = name;
        this.activeUsers = new HashMap<>();
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
                "fileName='" + fileName + '\'' +
                '}';
    }
}
