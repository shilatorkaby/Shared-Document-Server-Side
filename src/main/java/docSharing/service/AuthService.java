package docSharing.service;

import docSharing.Entities.Directory;
import docSharing.Entities.User;
import docSharing.Entities.Unconfirmed;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.UserRepository;
import docSharing.repository.UnconfirmedRepository;
import docSharing.utils.Email;
import docSharing.utils.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class AuthService {

    // users that did a valid login
    public HashMap<String, User> cachedUsers = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnconfirmedRepository unconfirmedRepository;
    @Autowired
    private DirectoryRepository directoryRepository;
    private final Logger logger;

    public AuthService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    public User register(User user) {

        // email does exist
        if (isEmailInDatabase(user.getEmail()))
            return null;

        Unconfirmed verificationUser = new Unconfirmed(user.getEmail(), user.getPassword());

        sendEmail(verificationUser);

        unconfirmedRepository.save(verificationUser);

        return user;
    }

    private void sendEmail(Unconfirmed unconfirmed) {

        String destination = unconfirmed.getEmail();
        String title = "Please verify your registration";
        String txt = "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/auth/verify/" + unconfirmed.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().to(destination).subject(title).content(txt).build();
        mailSender.send(email.convertIntoMessage());

        logger.info("mail sent successfully");
    }

    public String verifyToken(String token) {

        Unconfirmed unconfirmed = unconfirmedRepository.findByToken(token);

        if (unconfirmed != null) {
            unconfirmedRepository.delete(unconfirmed);
            User user = new User(unconfirmed.getEmail(), unconfirmed.getPassword());
            userRepository.save(user);
            directoryRepository.save(new Directory(user.getId()*-1,"root"));
            return "<h1>Email verification was done successfully</h1>";
        }
        return "You need to sign up first";
    }

    public String login(User user) {

        if (authenticateLogin(user)) {
            String token = Token.generate();
            cachedUsers.put(token, user);
            return token;
        }
        return null;
    }

    boolean authenticateLogin(User user) {
        User temp = userRepository.findByEmail(user.getEmail());

        return temp != null && temp.getPassword().equals(user.getPassword());
    }

    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    public User getCachedUser(String token){
        return cachedUsers.get(token);
    }
}
