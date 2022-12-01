package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    public UserService() {
    }

    public Document createDocument(User user, DocumentBody documentBody) {
        logger.info("Create doc to: " + user.getEmail() + "doc body: " + documentBody.getFileName());
        user.setId(userRepository.findByEmail(user.getEmail()).getId());
        Long fatherId = null;

        if (documentBody != null && user.getId() != null && !findDoc(user, documentBody.getFileName())) {
            if (documentBody.getFatherId() != null && directoryRepository.existsById(documentBody.getFatherId()))
            {
                fatherId = documentBody.getFatherId();
            } else if (documentBody.getFatherId() == null)
            {
                fatherId = directoryRepository.findByFatherId(user.getId() * -1).get(0).getId();
            }
            if (fatherId != null) {
                Document newDocument = docRepository.save(new Document(user.getEmail(), documentBody.getFileName()));
                documentLinkRepository.save(new DocumentLink(newDocument.getId()));
                docPermissionRepository.save(new DocPermission(newDocument.getId(), user.getEmail(), UserRole.OWNER));

                directoryRepository.save(new Directory(fatherId, documentBody.getFileName(), newDocument.getId()));
                return newDocument;
            }        }
        logger.warn("Create Document failed");
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
            logger.error("CEmail %s not exists in users table", user.getEmail());
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        logger.info("Find user completed");
        return u;
    }

}
