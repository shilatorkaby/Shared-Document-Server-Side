package docSharing.controller;

import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.UserRepository;
import docSharing.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthControllerTest {
    @Autowired
    AuthController authController;
    @Autowired
    UserRepository userRepository;


    @Test
    void createUser_addLegalUser_statusOK() {
        User a = new User("arielosh8@gmail.com","123123");
        assertEquals(authController.createUser(a).getStatusCode() , HttpStatus.OK);
        userRepository.delete(a);
    }

    @Test
    void createUser_addExistUser_BadRequest() {
        User user = new User("arielosh98@gmail.com","123123");
        userRepository.save(user);
        assertEquals(authController.createUser(user).getStatusCode() , HttpStatus.BAD_REQUEST);
        userRepository.delete(user);
    }

    @Test
    void emailVerification_verifiedToken_statusOK() {
        Unconfirmed unconfirmed = new Unconfirmed();
        assertEquals(authController.emailVerification(unconfirmed.getToken()).getStatusCode() , HttpStatus.OK);
    }

    @Test
    void emailVerification_nullToken_statusNotFound() {
        String token = null;
        assertEquals(authController.emailVerification(null).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void login_loginWithRegisteredUser_statusOK() {
        User user = new User("98@gmail.com","123123");
        userRepository.save(user);
        assertEquals(authController.login(user).getStatusCode(), HttpStatus.OK);
        userRepository.delete(user);
    }

    @Test
    void login_loginWithUnRegisteredUser_statusOK() {
        User user = new User("98@gmail.com","123123");
        assertEquals(authController.login(user).getStatusCode(), HttpStatus.NOT_FOUND);

    }
}