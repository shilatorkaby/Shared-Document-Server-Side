package docSharing.service;
import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.repository.DocRepository;
import docSharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.SQLDataException;

@Service
public class UserService {
    private final String PATH = "src/main/java/docSharing/repository/files/";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocRepository docRepository;

    public UserService() {}

//    public String createNewDoc(User user,String documentName) {
//        try {
//            findUser(user);
//            if(user.getDocuments().size() == 0)
//            {
//                new File(PATH+user.getEmail()).mkdirs();
//            }
//            if (!findDoc(user,documentName)) {
//                Document document = new Document(documentName);
//                user.getDocuments().add(document);
//                docRepository.save(document);
//                return user.getDocuments().toString();
//            }
//            else
//                return "This document already exists";
//        } catch (SQLDataException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    boolean docInDatabase(User user,String documentName)
//    {
//        Document document = docRepository.findByNameAndEmail(documentName,user.getEmail());
//        return document!=null;
//    }
//    boolean findDoc(User user,String documentName)
//    {
//        Document doc = user.getDocuments().stream().filter(d -> d.getFileName().equals(documentName)).findAny().orElse(null);
//        if(docInDatabase(user,documentName) || doc != null)
//            return true;
//        return false;
//    }

    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if(u == null){
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }

    public User addUser(User user) throws SQLDataException {
        findUser(user);
        return userRepository.save(user);


    }
}
