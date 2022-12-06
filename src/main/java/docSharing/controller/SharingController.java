package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.*;
import docSharing.service.AuthService;
import docSharing.service.SharingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class SharingController {

    @Autowired
    AuthService authService;

    @Autowired
    SharingService sharingService;

    private static Logger logger = LogManager.getLogger(SharingController.class.getName());
    private static final Gson gson = new Gson();

    /**
     * share via email endpoint, receives data about the desired user and send email invitation to the document
     *
     * @param token (Unique key for each logged user)
     * @param contender   has inside docId, email & userRole
     * @return email verification
     */
    @RequestMapping(value = "/share/via/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestHeader("token") String token, @RequestBody Contender contender) {
        logger.info("Share via email , token is: " + token);
        UserBody user = authService.getCachedUser(token);

        if (user != null && contender.getDocId() != null && contender.getEmail() != null) {
//            contender.setUserRole((contender.getUserRole() == "viewer") ? UserRole.VIEWER : UserRole.EDITOR);
            logger.info("Doc id: " + contender.getDocId() + "email: " + contender.getEmail() + "user role: " + contender.getUserRole());
            // we should check if this email valid
            Contender newContender = sharingService.shareViaEmail(contender);
            if (newContender != null) {
                logger.info("Contender is not null , email was sent");
                return ResponseEntity.ok(gson.toJson(contender));
            }
        }
        logger.warn("Share via email failed.");
        return ResponseEntity.badRequest().build();
    }


    /**
     * email verification, at the end the invited user becomes a viewer or editor of the document
     *
     * @param token (Unique key for each logged user)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/accept/email-invite/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable("token") String token) {
        logger.info("Email verification with token: " + token);
        String temp = sharingService.verifyEmailToken(token);

        if(temp != null){
            logger.info(token + " is legit");

            return ResponseEntity.ok().body("<h1>Email verification was done successfully</h1>");  // 200
        }
        logger.warn("token is forged");
        return ResponseEntity.notFound().build(); // 404
    }
}
