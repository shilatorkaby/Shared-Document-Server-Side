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

    private static final Logger logger = LogManager.getLogger(AuthController.class);


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
        User verifiedUser = authService.register(user);
        if (verifiedUser != null)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    /**
     * email verification, at the end the guest becomes a user in database
     *
     * @param token (taken from route)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/verify/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable("token") String token) {
        if (token != null) {
            return ResponseEntity.ok(authService.verifyToken(token));
        }
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
        if (user != null) {
            validation.isValidEmail(user.getEmail());
            validation.isValidPassword(user.getPassword());
            String token = authService.login(user);

            if (token != null) {
                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                return ResponseEntity.ok(map);
            }
        }
        return ResponseEntity.notFound().build();
    }
}


