package docSharing;

import docSharing.Entities.User;
import docSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringApp {
    @Autowired
    private UserService userService;
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendMail()
    {
        User user = new User(1234,"shilat","shilatprojects@gmail.com","shilat1");
        userService.sendmail(user);
    }
}