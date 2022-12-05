package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.*;
import docSharing.utils.Email;
import docSharing.utils.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SharingService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DirectoryRepository directoryRepository;

    @Autowired
    DocPermissionRepository docPermissionRepository;

    @Autowired
    ContenderRepository contenderRepository;

    @Autowired
    DocRepository docRepository;

    @Autowired
    JavaMailSender mailSender;

    private static final Logger logger = LogManager.getLogger(SharingService.class.getName());


    /**
     * shares the document via email to the desired contender,
     * checks if the given email is registered user, and if the invitation is not forged
     *
     * @param contender (docId, email and token type Contender)
     * @return contender, if something went wrong it returns null
     */
    public Contender shareViaEmail(Contender contender) {
        logger.info("Share via email to contender: " + contender.getEmail());
        if (!isEmailInDatabase(contender.getEmail()) || isEmailInDocument(contender.getDocId(), contender.getEmail())) {
            logger.warn("Share via email failed.");
            return null;
        }

        contender.setToken(Token.generate());
        sendmail(contender);
        contenderRepository.save(contender);
        logger.info("Share via email completed");
        return contender;
    }

    /**
     * checks if email is in database
     *
     * @param email
     * @return boolean
     */
    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    boolean isEmailInDocument(Long id, String email) {
        return (docPermissionRepository.findByDocIdAndEmail(id, email) != null);
    }

    /**
     * sends email to the contender
     *
     * @param contender (docId, email and password)
     */
    public void sendmail(Contender contender) {
        logger.info("Send mail to contender: " + contender.getEmail());
        String destination = contender.getEmail();
        String title = "Please accept the invitation";
        String txt = "Please click the link below to join a shared document:\n"
                + "http://localhost:8080/accept/email-invite/" + contender.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().subject(title).to(destination).content(txt).build();
        logger.info("Send mail completed");
        mailSender.send(email.convertIntoMessage());
    }

    /**
     * verify the given token if it is not forged
     *
     * @param token (unique String)
     * @return token, if something went wrong returns null
     */
    public String verifyEmailToken(String token) {
        logger.info("Verify email token :" + token);
        Contender contender = contenderRepository.findByToken(token);

        // we check if the user has a role already in the document
        if (docPermissionRepository.findByDocIdAndEmail(contender.getDocId(), contender.getEmail()) != null) {
            contenderRepository.delete(contender);
            docPermissionRepository.updatePermission(contender.getDocId(), contender.getEmail(), contender.getUserRole());
            logger.info("Verify email token completed");
            return token;
        }

        User user = userRepository.findByEmail(contender.getEmail());

        if (user != null) {
            Long fId = directoryRepository.findByFatherId(user.getId() * -1).get(0).getId();
            String name = docRepository.findByDocId(contender.getDocId()).getFileName();
            directoryRepository.save(new Directory(fId, name, contender.getDocId()));

            contenderRepository.delete(contender);
            docPermissionRepository.save(new DocPermission(contender.getDocId(), contender.getEmail(), contender.getUserRole()));
            logger.info("User successfully joined document");
            return token;
        }
        logger.warn("verify Email Token failed.");
        return null;
    }
}
