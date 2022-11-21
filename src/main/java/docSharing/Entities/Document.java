package docSharing.Entities;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        //this.activeUsers = new HashMap<>();
    }

    public Document(String name, String PATH) {
        this.fileName = name;
        this.activeUsers = new HashMap<>();

//        try {
//            this.fileContent = new FileWriter(PATH + name+".txt"); // fileContent.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    public String getName() {
        return fileName;
    }

    public void setName(String name) {
        this.fileName = name;
    }

    @Override
    public String toString() {
        return "Document{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
