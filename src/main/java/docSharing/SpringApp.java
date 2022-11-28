package docSharing;

import docSharing.Entities.*;
import docSharing.controller.AuthController;
import docSharing.controller.UserController;
import docSharing.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;

import java.sql.SQLDataException;
import java.util.Map;

@SpringBootApplication
public class SpringApp {
    @Autowired
    UserController userController;
    @Autowired
    AuthController authController;

    @Autowired
    SharingService sharingService;

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void shareDoc()
    {
//        User user = new User("yudin.david@gmail.com","12345");
//        authController.createUser(user);
//
//        user = new User("davidyu@edu.hac.ac.il","12345");
//        authController.createUser(user);

//        ResponseEntity<Map<String, String>> responseEntity = authController.login(user);
//        userController.createDocument(responseEntity.getBody().get("token"), new DocumentBody(4L, "first document", user.getEmail()));
//        userController.createDocument(responseEntity.getBody().get("token"), new DocumentBody(4L, "second document", user.getEmail()));
//        userController.createDocument(responseEntity.getBody().get("token"), new DocumentBody(4L, "third document", user.getEmail()));
//
        Contender contender = new Contender(11L, "davidyu@edu.hac.ac.il", null, UserRole.EDITOR);
        sharingService.shareViaEmail(contender);
    }
}