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
    void getSubDirs_nullDir_statusBadRequest() {
        assertEquals(userController.getSubDirs(null).getStatusCode(), HttpStatus.BAD_REQUEST);
    } 
    
    @Test
    void getSubDirs_existDirWithSubDirs_statusOK() {

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        directoryRepository.save(new Directory(directory.getId(), "subTestDir"));
        Directory subDirectory = directoryRepository.findByFatherIdAndName(directory.getId(), "subTestDir");


        assertEquals(userController.getSubDirs(directory).getStatusCode(), HttpStatus.OK);

        directoryRepository.delete(directory);
        directoryRepository.delete(subDirectory);
    }
    
    @Test
    void getSubDirs_existDirWithoutSubDirs_statusNotFound() {

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        assertEquals(userController.getSubDirs(directory).getStatusCode(), HttpStatus.OK);

        directoryRepository.delete(directory);
    }

    @Test
    void getSubDirs_notExistDir_statusNotFound() {
        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");
        directoryRepository.delete(directory);


        assertEquals(userController.getSubDirs(directory).getStatusCode(), HttpStatus.NOT_FOUND);
    }
//
//    @Test
//    void createNewDir() {
//        Directory directory = new Directory(233L, "ggg");
//        directoryRepository.save(directory);
//        User user = new User("ari@gmail.com" , "2222222");
//        userRepository.save(user);
//        userRepository.findByEmail("ari@gmail.com");
//        String token = authController.login(user).getBody().get(0);
//        assertEquals(userController.createNewDir(token, directory).getStatusCode(), HttpStatus.OK);
//
//    }
//
//    @Test
//    void createDocument() {
//        Unconfirmed unconfirmed = new Unconfirmed();
//        DocumentBody documentBody = new DocumentBody();
//        assertThat(userController.createDocument(unconfirmed.getToken(), documentBody).getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    void changeDir() {
//    }
//
//    @Test
//    void removeDir() {
//    }
//
//    @Test
//    void getOptionToMove() {
//    }
//
//    @Test
//    void getAllDocs() {
//    }
}