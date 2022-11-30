package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLDataException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocRepository docRepository;
    @Autowired
    private DocumentLinkRepository documentLinkRepository;
    @Autowired
    private DocPermissionRepository docPermissionRepository;
    @Autowired
    private DirectoryRepository directoryRepository;

    public UserService() {
    }

    public Document createDocument(User user, DocumentBody documentBody) {


        if (documentBody != null && !findDoc(user, documentBody.getFileName())) {

            user.setId(userRepository.findByEmail(user.getEmail()).getId());

            Document newDocument = docRepository.save(new Document(user.getEmail(), documentBody.getFileName()));
            documentLinkRepository.save(new DocumentLink(newDocument.getId()));
            docPermissionRepository.save(new DocPermission(newDocument.getId(), user.getEmail(), UserRole.OWNER));

            System.out.println(user.getId());
            System.out.println(directoryRepository.findByFatherId(user.getId() * -1).get(0).toString());

            Long fId = directoryRepository.findByFatherId(user.getId() * -1).get(0).getId();
            directoryRepository.save(new Directory(fId, documentBody.getFileName(), newDocument.getId()));
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
