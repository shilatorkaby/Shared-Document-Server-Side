package docSharing.controller;

import docSharing.Entities.User;
import docSharing.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;
    public final Validation validation; // should be static later on

    private static Logger logger = LogManager.getLogger(AuthController.class.getName());


    public AuthController() {
        this.authService = new AuthService();
        this.validation = new Validation();
    }


    /**
     * register endpoint, which at the end sends email verification using generated unique token
     *
     * @param user (email and password)
     * @return email verification
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody User user) {
        logger.info("Creating user: " + user.getEmail());
        User verifiedUser = authService.register(user);
        if (verifiedUser != null) {
            logger.info("verified user successfully completed!");
            return ResponseEntity.ok().build();
        }
        else{
            logger.warn("verified user failed.");
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * email verification, at the end the guest becomes a user in database
     *
     * @param token (taken from route)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/verify/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable("token") String token) {
        logger.info("Email verification with this token:  " + token);
        if (token != null) {
            logger.info("token is not null" + token);
            return ResponseEntity.ok(authService.verifyToken(token));
        }
        logger.warn("token is null");
        return ResponseEntity.notFound().build();
    }


    /**
     * login endpoint, at the end receives unique token and stored at cache
     *
     * @param user (email and password)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        logger.info("login with user:  " + user.getEmail());
        if (user != null) {
            logger.info("User is not null");
            validation.isValidEmail(user.getEmail());
            validation.isValidPassword(user.getPassword());
            String token = authService.login(user);
            logger.info("User token is:  " + token);
            if (token != null) {
                logger.info("Token is found");
                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                return ResponseEntity.ok(map);
            }
        }
        logger.info("login failed.");
        return ResponseEntity.notFound().build();
    }
}


