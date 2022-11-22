package docSharing.Entities;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Entity
public class VerificationToken {
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

    public VerificationToken() {
        this.token = generateNewToken();
    }

    public VerificationToken(String email, String password) {
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