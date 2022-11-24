package docSharing.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    public final Validation validation;

    public AuthController() {
        this.authService = new AuthService();
        this.validation = new Validation();
        Logger logger = LogManager.getLogger(AuthController.class.getName());
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody User user) {
        User verifiedUser = authService.register(user);
        if (verifiedUser != null)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/verify/{token}")
    public String emailVerification(@PathVariable("token") String token) {
        return authService.verifyToken(token);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        validation.isValidEmail(user.getEmail());
        validation.isValidPassword(user.getPassword());
        String token = authService.login(user);

        if (token != null) {
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            return ResponseEntity.ok(map);
        }
        return ResponseEntity.notFound().build();
    }
}


