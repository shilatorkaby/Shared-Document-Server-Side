package docSharing.controller;

import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.UserRepository;
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
    void createUser_addIllegalUser_statusBadRequest() {
        User user = new User("arielosh8@gmail.com", "123");
        assertEquals(HttpStatus.BAD_REQUEST, authController.createUser(user).getStatusCode());
    }

    @Test
    void createUser_addExistUser_BadRequest() {
        User user = new User("arielosh98@gmail.com", "123123");
        userRepository.save(user);
        assertEquals(HttpStatus.BAD_REQUEST, authController.createUser(user).getStatusCode());
        userRepository.delete(user);
    }

    @Test
    void emailVerification_unVerifiedToken_statusOK() {
        Unconfirmed unconfirmed = new Unconfirmed();
        assertEquals(HttpStatus.NOT_FOUND, authController.emailVerification(unconfirmed.getToken() + "xyz").getStatusCode());
    }

    @Test
    void emailVerification_nullToken_statusNotFound() {
        String token = null;
        assertEquals(authController.emailVerification(null).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void login_loginWithRegisteredUser_statusOK() {
        User user = new User("98@gmail.com", "123123");
        userRepository.save(user);
        assertEquals(authController.login(user).getStatusCode(), HttpStatus.OK);
        userRepository.delete(user);
    }

    @Test
    void login_loginWithSavedUser_statusOK() {
        User user = new User("test@gmail.com", "123123");
        userRepository.save(user);
        assertEquals(HttpStatus.OK, authController.login(user).getStatusCode());
        userRepository.delete(user);
    }
}