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

    /**
     * creates document if the user passes authentication, and there are no duplicate names
     *
     * @param temp
     * @param documentBody
     * @return document
     */
    public Document createDocument(UserBody temp, DocumentBody documentBody) {

        User user = userRepository.findByEmail(temp.getEmail());
        Long fatherId = null;

        if (documentBody != null && user.getId() != null && !findDoc(user, documentBody.getFileName())) {
            if (documentBody.getFatherId() != null && directoryRepository.existsById(documentBody.getFatherId())) {
                fatherId = documentBody.getFatherId();
            } else if (documentBody.getFatherId() == null) {
                fatherId = directoryRepository.findByFatherId(user.getId() * -1).get(0).getId();
            }
            if (fatherId != null) {
                Document newDocument = docRepository.save(new Document(user.getEmail(), documentBody.getFileName()));
                documentLinkRepository.save(new DocumentLink(newDocument.getId()));
                docPermissionRepository.save(new DocPermission(newDocument.getId(), user.getEmail(), UserRole.OWNER));

                directoryRepository.save(new Directory(fatherId, documentBody.getFileName(), newDocument.getId()));
                return newDocument;
            }
        }
        return null;
    }

    /**
     * returns all documents of a specific user, using email as an identifier
     *
     * @param user
     * @return list of Documents
     */
    public List<Document> getAllDocs(User user) {
        return docRepository.findByEmail(user.getEmail());
    }


    /**
     * returns a specific document, using document name and email of a user as an identifier
     *
     * @param user
     * @param documentName
     * @return Document
     */
    boolean findDoc(User user, String documentName) {
        return docRepository.findByNameAndEmail(documentName, user.getEmail()) != null;
    }
}
