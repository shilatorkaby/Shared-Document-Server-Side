package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Directory;
import docSharing.Entities.Document;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.User;
import docSharing.repository.*;
import org.junit.jupiter.api.*;
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
    String token;

    @BeforeEach
    void getToken() {
        userRepository.save(new User("testUser@gmail.com", "2222222"));
        token = authController.login(userRepository.findByEmail("testUser@gmail.com")).getBody().get("token");

        directoryRepository.deleteByName("testDir");
    }

    @AfterEach
    void deleteUser()
    {
        userRepository.delete(userRepository.findByEmail("testUser@gmail.com"));
    }


    /*=================================== get Sub Directories ====================================================================*/

    @Test
    void getSubFiles_nullDir_statusBadRequest() {
        assertEquals(userController.getSubFiles(token,null).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getSubFiles_existDirWithSubDirs_ResponseOK() {

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        directoryRepository.save(new Directory(directory.getId(), "subTestDir"));
        Directory subDirectory = directoryRepository.findByFatherIdAndName(directory.getId(), "subTestDir");


        assertEquals(userController.getSubFiles(token, directory).getStatusCode(), HttpStatus.OK);

        directoryRepository.delete(directory);
        directoryRepository.delete(subDirectory);
    }

    @Test
    void getSubFiles_existDirWithoutSubDirs_statusNotFound() {

        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");

        assertEquals(userController.getSubFiles(token, directory).getStatusCode(), HttpStatus.OK);

        directoryRepository.delete(directory);
    }

    @Test
    void getSubFiles_notExistDir_statusNotFound() {
        directoryRepository.save(new Directory(287878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(287878L, "testDir");
        directoryRepository.delete(directory);


        assertEquals(userController.getSubFiles(token, directory).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /*=================================== create new directory ====================================================================*/

    @Test
    void createNewDir_nullDirectory_NotFoundResponse() {

        assertEquals(userController.createNewDir(token, null).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void createNewDir_newDirectory_ResponseOK() {

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

        assertEquals(userController.createDocument(token, null).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void createDocument_newDocument_ResponseOK() {

        User user = userRepository.findByEmail("testUser@gmail.com");

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

        User user = userRepository.findByEmail("testUser@gmail.com");

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

    /*=================================== change directory ====================================================================*/

    @Test
    void changeDir_nullDirectory_NotFoundResponse() {
    assertEquals(userController.changeDir(token, null).getStatusCode(), HttpStatus.NOT_FOUND);
    }
    @Test
    void changeDir_notExistDirectory_NotFoundResponse() {

        directoryRepository.save(new Directory(28878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(28878L, "testDir");
        directoryRepository.delete(directory);

        ResponseEntity<String> response = userController.changeDir(token, directory);

        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
    }
    @Test
    void changeDir_existDirectory_OKResponse() {
        directoryRepository.save(new Directory(4521L,"tempRootDir"));
        Directory tempRootDir = directoryRepository.findByFatherIdAndName(4521L, "tempRootDir");

        directoryRepository.save(new Directory(tempRootDir.getId(), "futureFatherDir"));
        Directory futureFatherDir = directoryRepository.findByFatherIdAndName(tempRootDir.getId(), "futureFatherDir");

        directoryRepository.save(new Directory(tempRootDir.getId(), "currentDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(tempRootDir.getId(), "currentDir");

        directory.setFatherId(futureFatherDir.getId());
        ResponseEntity<String> response = userController.changeDir(token, directory);

        directoryRepository.delete(tempRootDir);
        directoryRepository.delete(futureFatherDir);
        directoryRepository.delete(directory);

        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }


    /*=================================== remove directory ====================================================================*/

    @Test
    void removeDir_nullDir_NotFoundResponse()
    {
        assertEquals(userController.removeDir(token,null).getStatusCode(),HttpStatus.NOT_FOUND);
    }
    @Test
    void removeDir_notExistDir_NotFoundResponse()
    {
        directoryRepository.save(new Directory(28878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(28878L, "testDir");
        directoryRepository.delete(directory);

        assertEquals(userController.removeDir(token,directory).getStatusCode(),HttpStatus.NOT_FOUND);
    }
    @Test
    void removeDir_existDir_OKResponse()
    {
        directoryRepository.save(new Directory(28878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(28878L, "testDir");
        assertEquals(userController.removeDir(token,directory).getStatusCode(),HttpStatus.OK);
    }

    /*=================================== get options to move ====================================================================*/

    @Test
    void getOptionToMove_nullDirectory_NotFoundResponse()
    {
        assertEquals(userController.getOptionToMove(token,null).getStatusCode(),HttpStatus.NOT_FOUND);
    }
    @Test
    void getOptionToMove_notExistDirectory_NotFoundResponse()
    {
        directoryRepository.save(new Directory(28878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(28878L, "testDir");
        directoryRepository.delete(directory);

        ResponseEntity<String> response = userController.getOptionToMove(token, directory);

        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);

    }
    @Test
    void getOptionToMove_existDirectoryWithoutOptions_notFoundResponse()
    {
        directoryRepository.save(new Directory(28878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(28878L, "testDir");

        ResponseEntity<String> response = userController.getOptionToMove(token, directory);
        directoryRepository.delete(directory);

        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
    }

    @Test
    void getOptionToMove_ExistDirectoryWithOption_OKResponse()
    {
        directoryRepository.save(new Directory(28878L, "testDir"));
        Directory directory = directoryRepository.findByFatherIdAndName(28878L, "testDir");

        directoryRepository.save(new Directory(directory.getId(), "testSubDir"));
        Directory subDirectory = directoryRepository.findByFatherIdAndName(directory.getId(), "testSubDir");

        ResponseEntity<String> response = userController.getOptionToMove(token, subDirectory);
        directoryRepository.delete(directory);
        directoryRepository.delete(subDirectory);

        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }



    /*=================================== remove directory ====================================================================*/


}