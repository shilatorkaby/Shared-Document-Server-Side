package docSharing.Entities;

import docSharing.utils.Token;

import javax.persistence.*;

@Entity
public class Unconfirmed extends UserBody{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String token;
    
    
    public Unconfirmed() {
        this.token = Token.generate();
    }

    public Unconfirmed(String email, String password) {
        super(email, password);
        this.token = Token.generate();
    }

    public Long getId() {
        return id;
    }
    
    public String getToken() {
        return token;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Unconfirmed{" +
                "id=" + id +
                ", token='" + token + '\'' +
                '}';
    }
}