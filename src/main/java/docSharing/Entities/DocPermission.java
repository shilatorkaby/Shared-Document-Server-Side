package docSharing.Entities;

import javax.persistence.*;

@Entity
public class DocPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    Long docId;
    String email;
    @Enumerated(EnumType.STRING)
    UserRole role;

    public DocPermission() {
    }

    public DocPermission(Long docId, String email, UserRole role) {
        this.docId = docId;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "DocPermission{" +
                "id=" + id +
                ", docId=" + docId +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
