package docSharing.service;

import docSharing.Entities.Contender;
import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.ContenderRepository;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.UserRepository;
import docSharing.utils.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

@Service
public class SharingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocPermissionRepository docPermissionRepository;

    @Autowired
    private ContenderRepository contenderRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Contender shareViaEmail(Contender contender){

        if(!isEmailInDatabase(contender.getEmail()) || isEmailInDocument(contender.getDocId(), contender.getEmail())){
            return null;
        }

        contender.setToken(generateNewToken());

        sendmail(contender);

        contenderRepository.save(contender);

        return contender;
    }

    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    boolean isEmailInDocument(Long id, String email) {
        return (docPermissionRepository.findByDocIdAndEmail(id, email) != null);
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    public void sendmail(Contender contender)
    {
        String destination = contender.getEmail();
        String title = "Please accept the invitation";
        String txt = "Please click the link below to join a shared document:\n"
                    + "http://localhost:8080/accept/email-invite/" + contender.getToken()
                    + "\nThank you.";

        Email email = new Email.Builder().subject(title).to(destination).content(txt).build();
        mailSender.send(email.convertIntoMessage());
    }

    public String verifyToken(String token) {

        Unconfirmed unconfirmed = unconfirmedRepository.findByToken(token);

        if (unconfirmed != null) {
            unconfirmedRepository.delete(unconfirmed);
            userRepository.save(new User(unconfirmed.getEmail(), unconfirmed.getPassword()));
            return "<h1>Email verification was done successfully</h1>";
        }
        return "You need to sign up first";
    }
}
