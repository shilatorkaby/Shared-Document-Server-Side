package docSharing.service;

import docSharing.Entities.*;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.DocPermissionRepository;
import docSharing.repository.DocRepository;
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
    private DocPermissionRepository docPermissionRepository;
    @Autowired
    private DirectoryRepository directoryRepository;


    public UserService() {
    }

    public Document createDocument(User user, DocBody docBody) {


        if (!findDoc(user, docBody.document.getFileName())) {
            Document newDocument = new Document(user.getEmail(), docBody.document.getFileName());

            docRepository.save(newDocument);
            docPermissionRepository.save(new DocPermission(newDocument.getId(), user.getEmail(), "owner"));
            if(docBody.fatherId!=null)
            {
                directoryRepository.save(new Directory(docBody.document.getFileName(), docBody.document.getId()));
            }
            else
                directoryRepository.save(new Directory(docBody.fatherId,docBody.document.getFileName(), docBody.document.getId()));

            return newDocument;
        }
        return null;
    }

    public List<Document> getAllDocs(User user)
    {
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
