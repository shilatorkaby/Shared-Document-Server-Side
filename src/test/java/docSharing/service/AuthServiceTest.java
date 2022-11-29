package docSharing.service;

import docSharing.Entities.Unconfirmed;
import docSharing.Entities.User;
import docSharing.repository.UnconfirmedRepository;
import docSharing.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UnconfirmedRepository unconfirmedRepository;

    @Test
    void register_succeed() {
        User user = new User("gf@gmail.com", "123123");
        assertEquals(authService.register(user), user);
    }

    @Test
    void register_not_succeed() {
        User user = new User("ddd@gmail.com", "123123");
        userRepository.save(user);
        assertEquals(authService.register(user), null);
    }

    @Test
    void verifyToken_succeed() {
        String res = "<h1>Email verification was done successfully</h1>";
        Unconfirmed unconfirmed = new Unconfirmed("asd@gmsil.com", "1111111");
        unconfirmedRepository.save(unconfirmed);
        assertEquals(authService.verifyToken(unconfirmed.getToken()), res);
        unconfirmedRepository.delete(unconfirmed);
    }
    @Test
    void verifyToken_not_succeed() {
        String res = "You need to sign up first";
        Unconfirmed unconfirmed = new Unconfirmed("ggg@gmsil.com", "1111111");
        assertEquals(authService.verifyToken(unconfirmed.getToken()), res);
    }
    @Test
    void login_succeed() {
        User user = new User("asd@gmsil.com", "1111111");
        Unconfirmed unconfirmed = new Unconfirmed(user.getEmail(),user.getPassword());
        unconfirmedRepository.save(unconfirmed);
        assertFalse(authService.login(user) == null);
        unconfirmedRepository.delete(unconfirmed);
    }

    @Test
    void login_not_succeed() {
        User user = new User("asld@gmsil.com", "1111111");
        assertTrue(authService.login(user) == null);
    }

    @Test
    void authenticateLogin_succeed() {
        User user = new User("asllld@gmsil.com", "1111111");
        userRepository.save(user);
        assertTrue(authService.authenticateLogin(user));
        userRepository.delete(user);
    }

    @Test
    void authenticateLogin_not_succeed() {
        User user = new User("asllld@gmsil.com", "1111111");
        assertFalse(authService.authenticateLogin(user));
    }

    @Test
    void isEmailInDatabase_succeed() {
        User user1 = new User("email@gmail.com","111111111111");
        userRepository.save(user1);
       User user2 =  userRepository.findByEmail("email@gmail.com");
       assertEquals(user1,user2);
       userRepository.delete(user1);
    }

    @Test
    void isEmailInDatabase_not_succeed() {
        User user2 =  userRepository.findByEmail("email@gmail.com");
        assertTrue(user2 == null);
    }
}