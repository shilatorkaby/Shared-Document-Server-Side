package docSharing.controller;

import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.UserRepository;
import docSharing.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthControllerTest {
    @Autowired
    AuthController authController;
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Test
    void createUser() {
        User a = new User("arielosh8@gmail.com","123123");
        assertEquals(authController.createUser(a).getStatusCode() , HttpStatus.OK);
        userRepository.delete(a);
    }

    @Test
    void createUser_Failed() {
        User a = new User("arielosh98@gmail.com","123123");
        assertEquals(authController.createUser(a).getStatusCode() , HttpStatus.BAD_REQUEST);
    }

    @Test
    void emailVerification() {
        Unconfirmed unconfirmed = new Unconfirmed();
        assertEquals(authController.emailVerification(unconfirmed.getToken()).getStatusCode() , HttpStatus.OK);
    }

    @Test
    void emailVerification_Failed() {
        String token = null;
        assertEquals(authController.emailVerification(token).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void login() {
        User user = new User("sh98@gmail.com","123123");
        userRepository.save(user);
        String token = authService.login(user);
        assertEquals(authController.login(user).getStatusCode(), HttpStatus.OK);
    }

    @Test
    void login_failed() {
        User user = new User("98@gmail.com","123123");
        String token = authService.login(user);
        assertEquals(authController.login(user).getStatusCode(), HttpStatus.NOT_FOUND);
    }
}