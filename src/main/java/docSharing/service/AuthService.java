package docSharing.service;

import docSharing.Entities.User;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    private final Map<Integer, String> Tokens;

    public AuthService() {
        this.Tokens = new HashMap<>();
    }

    private String createNewToken(User user)
    {
        String s= AuthService.generateRandomToken(8);
        Tokens.put(user.getId(),s);
        return s;
    }

    public String login(String email, String password) {

        return email;
    }
    public boolean checkToken(String email,String Token)
    {
        return false;
    }
    private static String generateRandomToken(int length)
    {
        assert length>0;
        StringBuilder result= new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append((char) ThreadLocalRandom.current().nextInt(33, 125));
        }
        return result.toString();
    }

}
