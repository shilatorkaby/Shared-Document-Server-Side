package docSharing;

import docSharing.Entities.Document;
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
        User user = new User("shilatprojects@gmail.com","12356");
        authController.createUser(user);
        String token = authController.login(user);
        userController.createDocument(token, new Document(user.getEmail(), "first documnet"));
        String allDocs = String.valueOf(userController.getAllDocs(token));
    }
}