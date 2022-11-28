package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.DocRepository;
import docSharing.repository.DocumentLinkRepository;
import docSharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.util.List;

@Service
public class UserService {
    private final String PATH = "src/main/java/docSharing/repository/files/";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocRepository docRepository;

    @Autowired
    private DocumentLinkRepository documentLinkRepository;

    @Autowired
    private DocPermissionRepository docPermissionRepository;

    public UserService() {
    }

    public Document createDocument(User user, Document document) {


        if (!findDoc(user, document.getFileName())) {
            Document newDocument = new Document(user.getEmail(), document.getFileName());

            docRepository.save(newDocument);
            documentLinkRepository.save(new DocumentLink(newDocument.getId()));
            docPermissionRepository.save(new DocPermission(newDocument.getId(), user.getEmail(), UserRole.OWNER));
            return newDocument;
        }
        return null;
    }

    public List<Document> getAllDocs(User user) {
        return docRepository.findByEmail(user.getEmail());
    }


    boolean findDoc(User user, String documentName) {

        return docRepository.findByNameAndEmail(documentName, user.getEmail()) != null;
    }

    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if (u == null) {
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }

}
