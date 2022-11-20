package docSharing;

import docSharing.Entities.User;
import docSharing.controller.AuthController;
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
    private AuthController authController;
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendMail()
    {
        User user = new User(1234L,"david","yudin.david@gmail.com","david");
        authController.createUser(user);
        authController.login(user);
    }
}