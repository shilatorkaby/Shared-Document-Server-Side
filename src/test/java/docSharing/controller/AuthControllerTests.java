package docSharing.controller;

import docSharing.Entities.Unconfirmed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;



public class AuthControllerTests {

    private static AuthController authController = new AuthController();




    @Test
    void verifyToken_nullToken_tokenExist()
    {
        assertThat(authController.emailVerification(null).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void verifyToken_verifiedToken_tokenExist()
    {
        Unconfirmed unconfirmed = new Unconfirmed();

    }
    @Test
    void verifyToken_verifiedToken_tokenIsNotExist()
    {
        Unconfirmed unconfirmed = new Unconfirmed();

    }






}
