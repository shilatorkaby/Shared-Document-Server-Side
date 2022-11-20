package docSharing.Entities;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;


public class VerificationToken {
    private String token;
    private Date expiryDate;

    public VerificationToken() {this.token = generateNewToken();}

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationToken(User user) {
       this.token = generateNewToken();
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }


    public String getToken() {
        return token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}