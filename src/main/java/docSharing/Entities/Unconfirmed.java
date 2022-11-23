package docSharing.Entities;
import javax.persistence.*;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
public class Unconfirmed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String token;

    private String email;

    private String password;

    public Long getId() {
        return id;
    }

    public Unconfirmed() {
        this.token = generateNewToken();
    }

    public Unconfirmed(String email, String password) {
        this.token = generateNewToken();
        this.email = email;
        this.password = password;
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}