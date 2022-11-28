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
    String permission;

    public DocPermission() {
    }

    public DocPermission(Long docId, String email, String permission) {
        this.docId = docId;
        this.email = email;
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

}
