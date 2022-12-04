package docSharing.service;

import docSharing.Entities.DocPermission;
import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.Entities.UserBody;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    @Autowired
    private DocPermissionRepository docPermissionRepository;


    /**
     * saves the document's content in the database
     *
     * @param document
     * @return document, if something went wrong returns null
     */
    public Document save(Document document) {
        if (getDocFromDatabase(document) != null) {
            docRepository.updateFileContent(document.getId(), document.getFileContent());
            return docRepository.findById(document.getId()).orElse(null);
        }
        return null;
    }

    /**
     * gets document from database by id
     *
     * @param document
     * @return document, if something went wrong returns null
     */
    Document getDocFromDatabase(Document document) {
        return docRepository.findByDocId(document.getId());
    }


    /**
     * get document from database by email and id (together they are unique)
     * we first check if the given user has the right permission to do so
     *
     * @param user
     * @param id
     * @return document, if something went wrong returns null
     */
    public Document getDocumentById(UserBody user, Long id) {

        DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(id, user.getEmail());

        return (docPermission != null) ? docRepository.findByDocId(id) : null;
    }

    /**
     * gets lists of roles of a specific user, using their email
     *
     * @param email
     * @return list of roles
     */
    public List<DocPermission> getRolesByEmail(String email) {

        return docPermissionRepository.findAllPermissionsByEmail(email);
    }
}
