package docSharing;

import docSharing.Entities.User;
import docSharing.controller.AuthController;
import docSharing.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import java.sql.SQLDataException;

@SpringBootApplication
public class SpringApp {
    @Autowired
    UserController userController;
    @Autowired
    AuthController authController;
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDoc()
    {
        User user = new User("shilatprojects@gmail.com","shilat1");
        authController.createUser(user);
        authController.login(user);

        try {
            userController.createNewDoc(user,"firstFile");
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
    }
}