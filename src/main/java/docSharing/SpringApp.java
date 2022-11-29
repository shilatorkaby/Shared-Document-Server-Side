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


    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

//    @EventListener (ApplicationReadyEvent.class)
//    void method()
//    {
//
//    }
}