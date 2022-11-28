package docSharing.Entities;

import javax.persistence.*;

@Entity
public class Contender {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    Long docId;
    String email;
    String token;
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    public Contender() {
    }

    public Contender(Long docId, String email, String token, UserRole userRole) {
        this.docId = docId;
        this.email = email;
        this.token = token;
        this.userRole = userRole;
    }

    public Long getDocId() {
        return docId;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getToken() {
        return token;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Contender{" +
                "id=" + id +
                ", docId=" + docId +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
