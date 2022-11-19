package docSharing.service;

import docSharing.Entities.User;
import docSharing.controller.AuthController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    private final Map<String, String> Tokens;

    private final TokenService tokenService;
    private Logger logger;

    public AuthService() {
        this.Tokens = new HashMap<>();
        this.logger = LogManager.getLogger(AuthController.class.getName());
        this.tokenService = new TokenService();
    }


    public String token(){
        String token = tokenService.generateNewToken();
        logger.debug("Token generated: {}" ,token);
        return token;
    }

    public String register(String email) {
        String token = token();
        Tokens.put(email,token);
        return token;
    }

    public String login(String email) {
        String token = token();
        Tokens.put(email,token);
        return token;
    }
}
