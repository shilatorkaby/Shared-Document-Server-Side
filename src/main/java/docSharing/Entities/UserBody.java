package docSharing.Entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class UserBody {

    @Column(unique = true)
    private String email;
    private String password;

    public UserBody() {
    }

    public UserBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserBody{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
