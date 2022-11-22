package docSharing.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(targetEntity=Document.class, mappedBy="email", fetch=FetchType.EAGER)
    private List<Document> documents;

    public User(){}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.documents = new ArrayList<>();
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public Long getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!Objects.equals(email, user.email)) return false;
        return Objects.equals(password, user.password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", documents=" + documents +
                '}';
    }
}
