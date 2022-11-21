package docSharing.service;
import docSharing.Entities.Document;
import docSharing.Entities.User;
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

    public UserService() {}

    public String createNewDoc(User user,String documentName) {
        try {
            findUser(user);
            if(user.getDocuments().size() == 0)
            {
                new File(PATH+user.getEmail()).mkdirs();
            }
            Document doc = findDoc(user,documentName);
            if (doc == null) {
                user.getDocuments().add(new Document(documentName, PATH + user.getEmail() + "/"));
                return user.getDocuments().toString();

            }
            else
                return "This document already exists";
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
    }

    Document findDoc(User user,String documentName)
    {
        return user.getDocuments().stream().filter(d -> {
            return d.getName().equals(documentName);
        }).findAny().orElse(null);
    }

    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if(u == null){
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }

    public User addUser(User user) throws SQLDataException {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new SQLDataException(String.format("Email %s exists in users table", user.getEmail()));
        } else
        {
            return userRepository.save(user);
        }

    }






}
