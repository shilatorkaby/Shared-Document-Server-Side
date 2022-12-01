package docSharing.service;

import docSharing.Entities.DocPermission;
import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.DocRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    @Autowired
    private DocPermissionRepository docPermissionRepository;

    private static Logger logger = LogManager.getLogger(DocService.class.getName());



    public String save(Document document) {
        if (getDocFromDatabase(document) != null) {
            docRepository.updateFileContent(document.getId(), document.getFileContent());
            logger.info("file's content was updated");
            return "file's content was updated";
        }
        logger.warn("Save failed.");
        return "File doesn't exists";
    }

    Document getDocFromDatabase(Document document) {
        return docRepository.findByDocId(document.getId());
    }


    public Document getDocumentById(User user, Long id) {

        DocPermission docPermission = docPermissionRepository.findByDocIdAndEmail(id, user.getEmail());
        if(docPermission == null){
            logger.warn("Doc permission is null");
        }
        else{
            logger.info("Get doc by id completed");
        }
        return (docPermission != null) ? docRepository.findByDocId(id) : null;
    }

    public List<DocPermission> getRolesByEmail(String email) {

        return docPermissionRepository.findAllPermissionsByEmail(email);
    }
}
