package docSharing.controller;
import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import docSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthService authService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestParam User user) throws SQLDataException {
        return new ResponseEntity<>(userService.findUser(user), HttpStatus.OK);
    }

    @RequestMapping(value="/modify/password",method = RequestMethod.PATCH)
    public String modifyPassword(@RequestParam String email,@RequestParam String newPassword,@RequestParam String token)
    {
        return email;
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestHeader("token") String token, @RequestBody Document document){

        User user = authService.getCachedUser(token);

        if(user != null){
            Document temp = userService.createDocument(user, document);
            if(temp != null)
                return ResponseEntity.ok(temp.toString());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") int id){
        return ResponseEntity.noContent().build();
    }
}
