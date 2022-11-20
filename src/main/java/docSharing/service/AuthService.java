package docSharing.service;

import docSharing.Entities.User;
import docSharing.Entities.VerificationToken;
import docSharing.controller.AuthController;
import docSharing.repository.UserRepository;
import docSharing.repository.VerificationTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private Logger logger;
    public AuthService() {
        this.logger = LogManager.getLogger(AuthController.class.getName());
    }

    public User register(User user)
    {
        VerificationToken verificationUser = new VerificationToken(user);
        System.out.println(user.toString());
        sendmail(verificationUser);
        User u = verificationTokenRepository.save(verificationUser).getUser();
        System.out.println(u.toString());
        return u;
    }



    public void sendmail(VerificationToken verificationUser)
    {

        SimpleMailMessage message =new SimpleMailMessage();
        message.setFrom("startgooglproject@gmail.com");
        message.setTo( verificationUser.getUser().getEmail());


        String content =  "Dear "+ verificationUser.getUser().getName() +",\n"
                + "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/auth/verify/" + verificationUser.getToken()
                + "\nThank you.";

        message.setText(content);
        message.setSubject("Please verify your registration");

        mailSender.send(message);

        System.out.println("mail sent successfully");
    }

    public String verifyToken(String token) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(token);
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        userRepository.save(verificationToken.getUser());
        return "Saved user";
    }

//    public String login(String email) {
//        String token = token();
//        Tokens.put(email,token);
//        return token;
//    }


}
