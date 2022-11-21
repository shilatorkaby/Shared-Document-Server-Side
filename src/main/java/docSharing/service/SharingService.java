package docSharing.service;

import docSharing.Entities.Contender;
import docSharing.Entities.User;
import docSharing.Entities.VerificationToken;
import docSharing.utils.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SharingService {

    HashMap<String,User> invitationByUsers = new HashMap<>();
    @Autowired
    private JavaMailSender mailSender;


    public void sendmail(Contender contender)
    {
        String destination = contender.getEmail();
        String title = "Please verify your registration";
        String txt = "Please click the link below to join a shared document:\n"
                    + "http://localhost:8080/auth/verify/"
                    + "\nThank you.";

        Email email = new Email.Builder().subject(title).to(destination).content(txt).build();
        mailSender.send(email.convertIntoMessage());
    }
}
