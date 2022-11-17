package docSharing.controller;
import docSharing.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    public final Validation validation;

    public AuthController(AuthService authService) {
        authService = new AuthService();
        validation = new Validation();
    }

    @RequestMapping(value ="/login",method = RequestMethod.GET)
    public String login(@RequestParam String email, @RequestParam String password) {
        try {
            validation.isValidEmail(email);
            //validation.isValidPassword(password);
            String token = authService.login(email, password);
            System.out.println("Login succeeded.");
            return token;
        }
        catch (IllegalArgumentException exp)
        {
            System.out.println("Login failed."+exp.getMessage());
            return null;
        }
    }

//    public void checkToken(String email, String Token) {
//        if(Token==null || Token.isEmpty())
//        {
//            printErrorToCmd("Login failed.","The token is null or empty.\n You must login first to get a valid token.");
//            return;
//        }
//        authService.checkToken(email, Token);
//    }

//    public static void printErrorToCmd(String mainMessage,String defiles)
//    {
//        System.out.println(mainMessage);
//        System.out.println(defiles);
//    }
}
