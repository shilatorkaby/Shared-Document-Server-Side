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

    /**
     * share via email endpoint, receives data about the desired user and send email invitation to the document
     *
     * @param token (Unique key for each logged user)
     * @param map   has inside docId, email & userRole
     * @return email verification
     */
    @RequestMapping(value = "/share/via/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        logger.info("Share via email , token is: " + token);
        UserBody user = authService.getCachedUser(token);

        if (user != null) {
            Long docId = Long.parseLong(map.get("docId"));
            String email = map.get("email");
            UserRole userRole = (map.get("userRole").equals("viewer")) ? UserRole.VIEWER : UserRole.EDITOR;
            logger.info("Doc id: " + docId + "email: " + email + "user role: " + userRole);
            // we should check if this email valid
            Contender contender = sharingService.shareViaEmail(new Contender(docId, email, null, userRole));
            if (contender != null) {
                logger.info("Contender is not null , email was sent");
                return ResponseEntity.ok(email + " was invited to the document");
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
