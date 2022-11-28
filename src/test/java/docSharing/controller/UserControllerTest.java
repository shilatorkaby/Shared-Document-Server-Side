package docSharing.controller;

import docSharing.Entities.Directory;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.UserRepository;
import docSharing.service.AuthService;
import docSharing.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;

    @Autowired
    AuthController authController;
    @Autowired
    DirectoryRepository directoryRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void getSonsByDirId() {
        Directory directory1 = new Directory(287878L, "fgf");
        directoryRepository.save(directory1);
        Directory directory2 = directoryRepository.findByFatherIdAndName(287878L, "fgf");
        assertEquals(userController.getSonsByDirId(directory2).getStatusCode(), HttpStatus.OK);
    }

    @Test
    void createNewDir() {
        Directory directory = new Directory(233L, "ggg");
        directoryRepository.save(directory);
        User user = new User("ari@gmail.com" , "2222222");
        userRepository.save(user);
        userRepository.findByEmail("ari@gmail.com");
        String token = authController.login(user).getBody().get(0);
        assertEquals(userController.createNewDir(token, directory).getStatusCode(), HttpStatus.OK);

    }

    @Test
    void createDocument() {
        Unconfirmed unconfirmed = new Unconfirmed();
        DocumentBody documentBody = new DocumentBody();
        assertThat(userController.createDocument(unconfirmed.getToken(), documentBody).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void changeDir() {
    }

    @Test
    void removeDir() {
    }

    @Test
    void getOptionToMove() {
    }

    @Test
    void getAllDocs() {
    }
}