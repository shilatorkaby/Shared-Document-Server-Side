package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Directory;
import docSharing.Entities.Document;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.User;
import docSharing.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Autowired
    DocPermissionRepository docPermissionRepository;

    @Autowired
    DocumentLinkRepository documentLinkRepository;

    @Autowired
    DocRepository docRepository;

    private static final Gson gson = new Gson();


    /*=================================== get Sub Directories ====================================================================*/

    @Test
    void getSubDirs_nullDir_statusBadRequest() {
        assertEquals(userController.getSubDirs(null).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getSubDirs_existDirWithSubDirs_ResponseOK() {

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

    /*=================================== create new directory ====================================================================*/

    @Test
    void createNewDir_nullDirectory_NotFoundResponse() {

        userRepository.save(new User("testUser@gmail.com", "2222222"));
        User user = userRepository.findByEmail("testUser@gmail.com");

        String token = authController.login(user).getBody().get("token");
        userRepository.delete(user);

        assertEquals(userController.createNewDir(token, null).getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void createNewDir_newDirectory_ResponseOK() {

        userRepository.save(new User("testUser@gmail.com", "2222222"));
        User user = userRepository.findByEmail("testUser@gmail.com");

        String token = authController.login(user).getBody().get("token");
        userRepository.delete(user);


        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        ResponseEntity<String> response = userController.createNewDir(token, new Directory(directory.getId(), "newDir"));
        Directory newDirectory = gson.fromJson(response.getBody(), Directory.class);
        directoryRepository.delete(directory);
        directoryRepository.delete(newDirectory);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void createNewDir_existDirectory_NotFoundResponse() {

        userRepository.save(new User("testUser@gmail.com", "2222222"));
        User user = userRepository.findByEmail("testUser@gmail.com");

        String token = authController.login(user).getBody().get("token");
        userRepository.delete(user);

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        ResponseEntity<String> response = userController.createNewDir(token, directory);
        directoryRepository.delete(directory);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void createNewDir_nullToken_NotFoundResponse() {

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        ResponseEntity<String> response = userController.createNewDir(null, directory);
        directoryRepository.delete(directory);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void createNewDir_notExistToken_NotFoundResponse() {

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        ResponseEntity<String> response = userController.createNewDir("abcd", directory);
        directoryRepository.delete(directory);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }


    /*=================================== create new document ====================================================================*/
    @Test
    void createDocument_nullDocument_NotFoundResponse() {

        userRepository.save(new User("testUser@gmail.com", "2222222"));
        User user = userRepository.findByEmail("testUser@gmail.com");

        String token = authController.login(user).getBody().get("token");
        userRepository.delete(user);

        assertEquals(userController.createDocument(token, null).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void createDocument_newDocument_ResponseOK() {

        userRepository.save(new User("testUser@gmail.com", "2222222"));
        User user = userRepository.findByEmail("testUser@gmail.com");

        String token = authController.login(user).getBody().get("token");
        userRepository.delete(user);

        ResponseEntity<String> response = userController.createDocument(token, new DocumentBody(123648L, "newDoc", user.getEmail()));
        Document newDocument = gson.fromJson(response.getBody(), Document.class);


        docRepository.delete(docRepository.findByNameAndEmail("newDoc", user.getEmail()));
        directoryRepository.delete(directoryRepository.findByFatherIdAndName(123648L, "newDoc"));
        documentLinkRepository.delete(documentLinkRepository.findByDocId(newDocument.getId()));
        docPermissionRepository.delete(docPermissionRepository.findByDocIdAndEmail(newDocument.getId(), user.getEmail()));
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void createDocument_existDocument_NotFoundResponse() {

        userRepository.save(new User("testUser@gmail.com", "2222222"));
        User user = userRepository.findByEmail("testUser@gmail.com");

        String token = authController.login(user).getBody().get("token");
        userRepository.delete(user);

        docRepository.save(new Document(user.getEmail(), "testDoc"));
        Document document = docRepository.findByNameAndEmail("testDoc", user.getEmail());

        ResponseEntity<String> response = userController.createDocument(token, new DocumentBody(85115L, document.getFileName(), document.getEmail()));
        docRepository.delete(document);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void createDocument_nullToken_NotFoundResponse() {

        assertEquals(userController.createDocument(null, new DocumentBody()).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void createDocument_notExistToken_NotFoundResponse() {


        ResponseEntity<String> response = userController.createDocument("abcd", new DocumentBody(287878L,"sh@gmail.com","testDoc"));

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

    }



}