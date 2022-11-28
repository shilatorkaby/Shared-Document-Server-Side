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

}
