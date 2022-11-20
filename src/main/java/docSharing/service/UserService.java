package docSharing.service;

import docSharing.Entities.OnRegistrationCompleteEvent;
import docSharing.Entities.User;
import docSharing.Entities.VerificationToken;
import docSharing.controller.AuthController;
import docSharing.repository.UserRepository;
import docSharing.repository.VerificationTokenRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.SQLDataException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthController authController;






    public UserService() {
    }





    private void sendVerificationEmail(User user, String siteURL)
    {

    }


    public User addUser(User user) throws SQLDataException {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new SQLDataException(String.format("Email %s exists in users table", user.getEmail()));
        } else
        {
            return userRepository.save(user);
        }

    }



    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if(u == null){
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }
}
