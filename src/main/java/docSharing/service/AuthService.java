package docSharing.service;

import docSharing.Entities.Directory;
import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.Entities.UserBody;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.UnconfirmedRepository;
import docSharing.repository.UserRepository;
import docSharing.utils.Email;
import docSharing.utils.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    // users that did a valid login
    public HashMap<String, UserBody> cachedUsers = new HashMap<>();

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

    /**
     * authenticates if the given user doesn't exist in the database, if so sends email for verification
     *
     * @param user (email and password type UserBody)
     * @return user, if something went wrong it returns null
     */
    public UserBody register(UserBody user) {

        // email does exist
        if (isEmailInDatabase(user.getEmail()))
            return null;

        Unconfirmed verificationUser = new Unconfirmed(user.getEmail(), user.getPassword());

        sendEmail(verificationUser);

        unconfirmedRepository.save(verificationUser);

        return user;
    }

    /**
     * sends email verification for optional user
     *
     * @param unconfirmed (email, password and unique token type Unconfirmed)
     */
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

    /**
     * verify the given token if it is not forged
     *
     * @param token (unique String)
     * @return token, if something went wrong returns null
     */
    public String verifyToken(String token) {

        Unconfirmed unconfirmed = unconfirmedRepository.findByToken(token);

        if (unconfirmed != null) {

            unconfirmedRepository.delete(unconfirmed);

            User user = new User(unconfirmed.getEmail(), unconfirmed.getPassword());
            userRepository.save(user);

            // each user creates by default a "root" directory
            directoryRepository.save(new Directory(user.getId() * -1, "root"));
            return token;
            // return "<h1>Email verification was done successfully</h1>";
        }
        return null;
        // return "You need to sign up first";
    }

    /**
     * authenticates if the given user is in the database
     *
     * @param temp (email and password type UserBody)
     * @return token (unique String that allows further actions)
     */
    public String login(UserBody temp) {

        User user = userRepository.findByEmail(temp.getEmail());

        if (user.getPassword().equals(temp.getPassword())) {
            String token = Token.generate();
            cachedUsers.put(token, user);
            return token;
        }
        return null;
    }

    /**
     * checks if the given email is in the database
     *
     * @param email
     * @return boolean
     */
    boolean isEmailInDatabase(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    /**
     * main function to check if the user is logged, and able to perform actions
     *
     * @param token (Unique key for each logged user)
     * @return User
     */
    public UserBody getCachedUser(String token) {
        return (token == null) ? null : cachedUsers.get(token);
    }
}

