package docSharing;

import docSharing.Entities.Directory;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.User;
import docSharing.controller.AuthController;
import docSharing.controller.UserController;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.UserRepository;
import docSharing.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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


     public static void main(String[] args) {

        SpringApplication.run(SpringApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDoc()
    {
        authController.emailVerification(null);
//        User user = new User("shillat14@gmail.com","12345");
//        authController.createUser(user);
//        ResponseEntity<Map<String, String>> responseEntity = authController.login(user);

//        Long rootId = directoryRepository.getRootDir(-1*userRepository.findByEmail(user.getEmail()).getId()).getId();
//        userController.createDocument(responseEntity.getBody().get("token"), new DocumentBody(rootId, "first document",user.getEmail()));
//        userController.createNewDir(responseEntity.getBody().get("token"), new Directory(rootId, "first directory"));
//        userController.createNewDir(responseEntity.getBody().get("token"), new Directory(8L, "second directory"));

//        Directory directory = new Directory();
//        directory.setId(9L);
//        directory.setFatherId(8L);
//        ResponseEntity<String> response = userController.getOptionToMove(directory);
//        System.out.println(response.getBody());
//        //directory.setFatherId(4L);
//        //directory.setFatherId(8L);
//        System.out.println(userController.changeDir(directory).getBody());
    }
}