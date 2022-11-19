package docSharing.service;

import docSharing.Entities.User;
import docSharing.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) throws SQLDataException {
        if(userRepository.findByEmail(user.getEmail())!=null){
            throw new SQLDataException(String.format("Email %s exists in users table", user.getEmail()));
        }
        return userRepository.save(user);
    }

    public User findUser(User user) throws SQLDataException {
        User u = userRepository.findByEmail(user.getEmail());
        if(u == null){
            throw new SQLDataException(String.format("Email %s not exists in users table", user.getEmail()));
        }
        return u;
    }
}
