package docSharing.service;

import docSharing.Entities.Contender;
import docSharing.Entities.DocPermission;
import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.ContenderRepository;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.UserRepository;
import docSharing.utils.Email;
import docSharing.utils.Token;
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

    public Contender shareViaEmail(Contender contender) {

        if (!isEmailInDatabase(contender.getEmail()) || isEmailInDocument(contender.getDocId(), contender.getEmail())) {
            return null;
        }

        contender.setToken(Token.generate());
        sendmail(contender);
        contenderRepository.save(contender);

        return contender;
    }

    public void shareViaLink(HashMap<String, String> map) {

    }

    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    boolean isEmailInDocument(Long id, String email) {
        return (docPermissionRepository.findByDocIdAndEmail(id, email) != null);
    }


    public void sendmail(Contender contender) {
        String destination = contender.getEmail();
        String title = "Please accept the invitation";
        String txt = "Please click the link below to join a shared document:\n"
                + "http://localhost:8080/accept/email-invite/" + contender.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().subject(title).to(destination).content(txt).build();
        mailSender.send(email.convertIntoMessage());
    }

    public String verifyToken(String token) {

        Contender contender = contenderRepository.findByToken(token);

        if (contender != null) {
            contenderRepository.delete(contender);
            docPermissionRepository.save(new DocPermission(contender.getDocId(), contender.getEmail(), contender.getUserRole()));

            return "<h1>User successfully joined document</h1>";
        }
        return "User couldn't join document";
    }
}
