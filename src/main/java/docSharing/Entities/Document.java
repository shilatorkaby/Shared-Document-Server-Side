package docSharing.Entities;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;

    String fileName;
    //@Column(name="UserRole", columnDefinition="enum(Owner,Editor,Viewer)")

    Map<String,String> activeUsers;

    String fileContent;

    public Document() {}

    public Document(String email, String fileName) {
        this.email = email;
        this.fileName = fileName;
        this.activeUsers = new HashMap<>();
    }

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    public Map<String, String> getActiveUsers() {
        return activeUsers;
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
