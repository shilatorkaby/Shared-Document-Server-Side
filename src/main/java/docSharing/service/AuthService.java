package docSharing.service;

import docSharing.Entities.User;
import docSharing.Entities.VerificationToken;
import docSharing.repository.UserRepository;
import docSharing.repository.VerificationTokenRepository;
import docSharing.utils.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    // users that did a valid login
    HashMap<String, User> cachedUsers = new HashMap<>();

    //HashMap<String, String> tempRegistered = new HashMap<>(); //token,mail address

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    private final Logger logger;

    public AuthService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    public User register(User user) {

        // email does exist
        if (isEmailInDatabase(user.getEmail()))
            return null;

        VerificationToken verificationUser = new VerificationToken(user.getEmail(), user.getPassword());

        sendEmail(verificationUser);

        verificationTokenRepository.save(verificationUser);

        return user;
    }

    public void sendEmail(VerificationToken verificationToken) {

        String destination = verificationToken.getEmail();
        String title = "Please verify your registration";
        String txt = "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/auth/verify/" + verificationToken.getToken()
                + "\nThank you.";

        Email email = new Email.Builder().to(destination).subject(title).content(txt).build();
        mailSender.send(email.convertIntoMessage());

        logger.info("mail sent successfully");
    }

    public String verifyToken(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken != null) {
            verificationTokenRepository.delete(verificationToken);
            userRepository.save(new User(verificationToken.getEmail(), verificationToken.getPassword()));
            return "Email verification was done successfully";
        }
        return "You need to sign up first";
    }

    public String login(User user) {

        if (authenticateLogin(user)) {
            cachedUsers.put(VerificationToken.generateNewToken(), user);
            return "Login succeed";
        }
        return "Login failed";
    }

    boolean authenticateLogin(User user) {
        User temp = userRepository.findByEmail(user.getEmail());

        return temp != null && temp.getPassword().equals(user.getPassword());
    }

    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }
}
