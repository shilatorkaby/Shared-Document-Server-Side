package docSharing.Entities;

import docSharing.utils.Token;

import javax.persistence.*;

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
        this.token = Token.generate();
    }

    public Unconfirmed(String email, String password) {
        this.token = Token.generate();
        this.email = email;
        this.password = password;
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