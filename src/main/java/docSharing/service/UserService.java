package docSharing.service;

import docSharing.Entities.User;
import docSharing.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.SQLDataException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendmail(User user)
    {
        SimpleMailMessage message =new SimpleMailMessage();
        message.setFrom("startgooglproject@gmail.com");
        message.setTo(user.getEmail());
        String content = "Dear "+user.getName()+",\n"
            + "Please click the link below to verify your registration:\n"
            + "Thank you.";


        message.setText(content);
        message.setSubject("Please verify your registration");

        mailSender.send(message);

        System.out.println("mail sent successfully");

    }

    public UserService() {
    }


    public void register(User user, String siteUrl)
    {
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);

        String randomCode = String.valueOf(new RandomString(64));
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        //userRepository.save(user);

        try {
            sendVerificationEmail(user,siteUrl);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }



    public User addUser(User user) throws SQLDataException {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new SQLDataException(String.format("Email %s exists in users table", user.getEmail()));
        }
        return userRepository.save(user);
    }

    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if(u == null){
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }
}
