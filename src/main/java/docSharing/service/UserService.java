package docSharing.service;

import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.Entities.UserRole;
import docSharing.repository.DocRepository;
import docSharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;

@Service
public class UserService {
    private final String PATH = "src/main/java/docSharing/repository/files/";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocRepository docRepository;

    public UserService() {
    }

    public Document createDocument(User user, Document document) {

        if (!findDoc(user, document.getFileName())) {
            Document newDocument = new Document(user.getEmail(), document.getFileName());
            newDocument.getActiveUsers().put(user, UserRole.OWNER);
            user.getDocuments().add(newDocument);
            docRepository.save(newDocument);
            return newDocument;
        }
        return null;
    }

    boolean docInDatabase(User user, String documentName) {
        Document document = docRepository.findByNameAndEmail(documentName, user.getEmail());
        return document != null;
    }

    boolean findDoc(User user, String documentName) {
        Document doc = user.getDocuments().stream().filter(d -> d.getFileName().equals(documentName)).findAny().orElse(null);
        if (doc != null || docInDatabase(user, documentName))
            return true;
        return false;
    }

    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if (u == null) {
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }

    public User addUser(User user) throws SQLDataException {
        findUser(user);
        return userRepository.save(user);


    }
}
