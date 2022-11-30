package docSharing;

import docSharing.Entities.Directory;
import docSharing.Entities.Document;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.User;
import docSharing.controller.AuthController;
import docSharing.controller.UserController;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.DocRepository;
import docSharing.repository.UserRepository;
import docSharing.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootApplication
public class SpringApp {
    @Autowired
    UserController userController;
    @Autowired
    AuthController authController;
    @Autowired
    DirectoryService directoryService;
    @Autowired
    DirectoryRepository directoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DocRepository docRepository;


    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @EventListener (ApplicationReadyEvent.class)
    void method()
    {
//        userRepository.save(new User("yudin.david@gmail.com", "12345"));
//        User user = userRepository.findByEmail("yudin.david@gmail.com");
//        String token = authController.login(user).getBody().get("token");
////        directoryRepository.save(new Directory(-1*user.getId(), "root"));
//        Directory root = directoryRepository.findByFatherIdAndName(-1*user.getId(), "root");
////        directoryRepository.save(new Directory(root.getId(), "first directory"));

//        docRepository.updateFileContent(4L, "Best article ever!!");
//
//        userController.createDocument(token, new DocumentBody(root.getId(), "first document", "yudin.david@gmail.com"));
    }
}