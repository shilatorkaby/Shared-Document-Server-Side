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


    public Document save(Document document) {
        if (getDocFromDatabase(document) != null) {
            docRepository.updateFileContent(document.getId(), document.getFileContent());
            return docRepository.findById(document.getId()).orElse(null);
        }
        return null;
    }

    Document getDocFromDatabase(Document document) {
        return docRepository.findByDocId(document.getId());
    }


    public Document getDocumentById(UserBody user, Long id) {

        DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(id, user.getEmail());

        return (docPermission != null) ? docRepository.findByDocId(id) : null;
    }

    public List<DocPermission> getRolesByEmail(String email) {

        return docPermissionRepository.findAllPermissionsByEmail(email);
    }
}
