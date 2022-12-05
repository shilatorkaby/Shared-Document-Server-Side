package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.*;
import docSharing.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class DocControllerTest {

    @Autowired
    DocController docController;

    @Autowired
    AuthController authController;
    @Autowired
    DocRepository docRepository;
    @Autowired
    DirectoryRepository directoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DocPermissionRepository docPermissionRepository;
    private static final Gson gson = new Gson();

    String token;

    @BeforeEach
    void getToken() {
        userRepository.save(new User("test@gmail.com", "2222222"));
        String jsonBody = authController.login(userRepository.findByEmail("test@gmail.com")).getBody();

        Map temp = gson.fromJson(jsonBody, Map.class);
        token = (String) temp.get("token");

        directoryRepository.deleteByName("testDir");
    }
    @AfterEach
    void deleteUser()
    {
        userRepository.delete(userRepository.findByEmail("test@gmail.com"));
    }


    /*=============================================== save ====================================================================*/


    @Test
    void save_nullDocId_NotFoundRequest() {
        assertEquals(docController.save(token,new Document("test@gmail.com","fileTest")).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void save_notExistDocId_NotFoundRequest() {
        Document document = docRepository.save(new Document("test@gmail.com", "testDir"));
        document.setFileContent("new content for test");
        docRepository.delete(document);

        assertEquals(docController.save(token,document).getStatusCode(), HttpStatus.NOT_FOUND);
    }
    @Test
    void save_ExistDocId_OKResponse() {
        Document document = docRepository.save(new Document("test@gmail.com", "testDir"));
        document.setFileContent("new content for test");

        ResponseEntity<String> response = docController.save(token,document);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        docRepository.delete(document);

    }

    /*=============================================== get Roles By Token ====================================================================*/

    @Test
    void getRolesByToken_ExistTokenAndDocuments_OKResponse() {
        Document document = docRepository.save(new Document("test@gmail.com", "testDir"));
        DocPermission documentPermission = docPermissionRepository.save(new DocPermission(document.getId(),document.getEmail(),UserRole.OWNER));

        ResponseEntity<String> response = docController.getRolesByToken(token);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        docRepository.delete(document);
        docPermissionRepository.delete(documentPermission);

    }
    @Test
    void getRolesByToken_ExistTokenWithoutDocuments_OKResponse() {

        assertEquals(docController.getRolesByToken(token).getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getRolesByToken_notExistToken_NotFoundRequest() {
        Document document = docRepository.save(new Document("test@gmail.com", "testDir"));
        DocPermission documentPermission = docPermissionRepository.save(new DocPermission(document.getId(),document.getEmail(),UserRole.OWNER));

        assertEquals(docController.getRolesByToken("yyy").getStatusCode(), HttpStatus.NOT_FOUND);
        docRepository.delete(document);
        docPermissionRepository.delete(documentPermission);

    }

    /*=============================================== Get Document By Id ====================================================================*/

    @Test
    void getDocumentById_ExistDocId_OKResponse() {
        Document document = docRepository.save(new Document("test@gmail.com", "testDir"));
        DocPermission documentPermission = docPermissionRepository.save(new DocPermission(document.getId(),document.getEmail(),UserRole.OWNER));

        ResponseEntity<String> response = docController.getDocumentById(token,document);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        docRepository.delete(document);
        docPermissionRepository.delete(documentPermission);

    }
    @Test
    void getDocumentById_NotExistDocId_NotFoundRequest() {
        Document document = docRepository.save(new Document("test@gmail.com", "testDir"));
        DocPermission documentPermission = docPermissionRepository.save(new DocPermission(document.getId(),document.getEmail(),UserRole.OWNER));
        docRepository.delete(document);
        docPermissionRepository.delete(documentPermission);

        ResponseEntity<String> response = docController.getDocumentById(token,document);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }


}
