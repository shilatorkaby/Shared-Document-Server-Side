package docSharing.service;

import docSharing.Entities.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocServiceTest {

    @Test
    void save() {
        Document document = new Document();
        String res = "file's content was updated";

    }

    @Test
    void getDocFromDatabase() {
    }
}