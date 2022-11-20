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

    HashMap<String,User> tokens = new HashMap<>();

    HashMap <String,String> registeredUsers = new HashMap<>();
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;
    private Logger logger;

    public AuthService() {
        this.logger = LogManager.getLogger(AuthService.class.getName());
    }

    public User register(User user)
    {
        VerificationToken verificationUser = new VerificationToken(user);
        sendmail(verificationUser,user);
        tokens.put(verificationUser.getToken(),user);
        return user;
    }

    public void sendmail(VerificationToken verificationToken,User user)
    {
        SimpleMailMessage message =new SimpleMailMessage();
        message.setFrom("startgooglproject@gmail.com");
        message.setTo(user.getEmail());


        String content =  "Dear "+user.getName()+",\n"
                + "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/auth/verify/" + verificationToken.getToken()
                + "\nThank you.";

        message.setText(content);
        message.setSubject("Please verify your registration");

        mailSender.send(message);
        logger.info("mail sent successfully");
    }

    public String verifyToken(String token) {
        User user = tokens.get(token);
        if (user!= null) {
            logger.info("user by token: " + user.getEmail());
            if (!userInDB(user)) {
                userRepository.save(user);
                registeredUsers.put(token,user.getEmail());
                logger.info("user saved successfully");
                return user.getEmail();
            }
        }
        return null;
    }

    public String login(User user) {
        return(registeredUsers.containsValue(user.getEmail())? user.getEmail():null);
    }

        boolean userInDB(User user)
        {
            return (userRepository.findByEmail(user.getEmail()) != null);
        }


}
