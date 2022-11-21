package docSharing.service;
import docSharing.Entities.User;
import docSharing.Entities.VerificationToken;
import docSharing.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {

    HashMap<String,User> tokensByUsers = new HashMap<>();

    HashMap <String,String> tempRegistered = new HashMap<>(); //token,mail address
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;
    private final Logger logger;

    public AuthService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    public User register(User user)
    {
        VerificationToken verificationUser = new VerificationToken(user);
        if(!tempRegistered.containsValue(user.getEmail()) && userInTokens(user.getEmail()) == null) {
            sendmail(verificationUser, user);
            tokensByUsers.put(verificationUser.getToken(),user);
            tempRegistered.put(verificationUser.getToken(), user.getEmail());
        }
        return user;
    }

    public void sendmail(VerificationToken verificationToken,User user)
    {
        SimpleMailMessage message =new SimpleMailMessage();
        message.setFrom("startgooglproject@gmail.com");
        message.setTo(user.getEmail());


        String content = "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/auth/verify/" + verificationToken.getToken()
                + "\nThank you.";

        message.setText(content);
        message.setSubject("Please verify your registration");

        mailSender.send(message);
        logger.info("mail sent successfully");
    }

    public String verifyToken(String token) {
        if (registeredUser(token)) {
            logger.info("user with token: " + token +"has registered as required");

            tempRegistered.remove(token);
            addUserToDatabase(tokensByUsers.get(token));
            return "Email verification was done successfully";
        }
        else
            return "You need to sign up first";
    }

    public String login(User user) {
        String existToken = userInTokens(user.getEmail());
        if (existToken != null && Objects.equals(tokensByUsers.get(existToken).getPassword(), user.getPassword()))
            return ("Login succeed");
        else
            return ("Login failed - you need to sign up first");

    }

    boolean registeredUser(String token)
    {
        User user = tokensByUsers.get(token);
        return (user != null && tempRegistered.get(token) != null);
    }

    void addUserToDatabase(User user) {
        if (userRepository.findByEmail(user.getEmail()) == null)
            userRepository.save(user);
    }

    String userInTokens(String email)
    {
        for(Map.Entry<String, User> entry : tokensByUsers.entrySet())
        {
            if (Objects.equals(entry.getValue().getEmail(), email))
                return entry.getKey();
        }
        return null;
    }
}
