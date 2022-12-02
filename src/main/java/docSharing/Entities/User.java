package docSharing.Entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends UserBody {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public User(){}

    public User(String email, String password) {

        super(email, password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
