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
import org.springframework.http.ResponseEntity;

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

//    @EventListener(ApplicationReadyEvent.class)
//    public void createDoc()
//    {
//        User user = new User("yudin.david@gmail.com","12345");
//        //authController.createUser(user);
//        ResponseEntity<String> responseEntity = authController.login(user);
//        userController.createDocument(responseEntity.getBody(), new Document(user.getEmail(), "first document"));
//        String allDocs = String.valueOf(userController.getAllDocs(responseEntity.getBody()));
//    }
}