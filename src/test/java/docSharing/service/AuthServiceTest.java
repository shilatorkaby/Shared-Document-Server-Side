package docSharing.service;

import docSharing.Entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
@Autowired
AuthService authService;

    @Test
    void register() {
        User user = new User("arielosh98@gmail.com","123123");
        assertEquals(authService.register(user),user);
    }

    @Test
    void verifyToken() {

    }

    @Test
    void login() {
        User user = new User("arielosh98@gmail.com","123123");
       assertFalse(authService.login(user) == null);
    }

    @Test
    void authenticateLogin() {
    }

    @Test
    void isEmailInDatabase() {
    }

    @Test
    void getCachedUser() {
    }
}