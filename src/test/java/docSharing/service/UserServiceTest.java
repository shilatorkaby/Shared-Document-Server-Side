package docSharing.service;

import docSharing.Entities.Document;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.User;
import docSharing.repository.DocRepository;
import docSharing.repository.UnconfirmedRepository;
import docSharing.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    DocRepository docRepository;

    @Autowired
    UnconfirmedRepository unconfirmedRepository;

    @Test
    void createDocument_succeed() {
        User user = new User("ejjloil@gmail.com","1234567");
        DocumentBody doc_body = new DocumentBody(19L,"name",user.getEmail());
        Document doc = userService.createDocument(user,doc_body);
        assertTrue(doc != null);
    }

    @Test
    void createDocument_not_succeed() {
        docRepository.deleteAll();
        User user = new User("email8@gmail.com","1234567");
        DocumentBody doc_body = new DocumentBody(19L,"name",user.getEmail());
        Document doc1 = userService.createDocument(user,doc_body);
        docRepository.save(doc1);
        Document doc2 = userService.createDocument(user,doc_body);
        assertEquals(doc2 ,null);
        docRepository.delete(doc1);
    }
}