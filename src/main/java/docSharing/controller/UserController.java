package docSharing.controller;

import docSharing.Entities.User;
import docSharing.repository.UserRepository;
import docSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLDataException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody User user){
        try {
            return new ResponseEntity<>(userService.addUser(user).toString(), HttpStatus.OK);
        } catch (SQLDataException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already exists", e);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestParam User user) throws SQLDataException {
        return new ResponseEntity<>(userService.findUser(user), HttpStatus.OK);
    }

    @RequestMapping(value="/modify/password",method = RequestMethod.PATCH)
    public String modifyPassword(@RequestParam String email,@RequestParam String newPassword,@RequestParam String token)
    {
        return email;
    }
    @RequestMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") int id){
        return ResponseEntity.noContent().build();
    }
}
