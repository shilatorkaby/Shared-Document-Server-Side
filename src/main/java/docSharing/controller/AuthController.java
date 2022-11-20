package docSharing.controller;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    public final Validation validation;
    private Logger logger;


    public AuthController() {
        this.authService = new AuthService();
        this.validation = new Validation();
        this.logger = LogManager.getLogger(AuthController.class.getName());
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody User user){
        User verifiedUser = authService.register(user);
        if (user != null)
            return ResponseEntity.ok(verifiedUser.toString());
        else
            return ResponseEntity.notFound().build();
    }
    @RequestMapping(value = "/verify/{token}")
    public String emailVerification(@PathVariable("token") String token)
    {
        return authService.verifyToken(token);
    }


//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(@RequestParam String email, @RequestParam String password) {
//        try {
//            validation.isValidEmail(email);
//            validation.isValidPassword(password);
//            String token = authService.login(email);
//            System.out.println("Login succeeded.");
//            return token;
//        } catch (IllegalArgumentException exp) {
//            System.out.println("Login failed." + exp.getMessage());
//            return null;
//        }
//    }
}


