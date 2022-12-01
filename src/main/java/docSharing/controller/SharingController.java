package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Contender;
import docSharing.Entities.DocumentLink;
import docSharing.Entities.User;
import docSharing.Entities.UserRole;
import docSharing.service.AuthService;
import docSharing.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * share via email endpoint, receives data about the desired user and send email invitation to the document
     *
     * @param token (Unique key for each logged user)
     * @param map   has inside docId, email & userRole
     * @return email verification
     */
    @RequestMapping(value = "/share/via/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {

        User user = authService.getCachedUser(token);

        if (user != null) {

            Long docId = Long.parseLong(map.get("docId"));
            String email = map.get("email");
            UserRole userRole = (map.get("userRole").equals("viewer")) ? UserRole.VIEWER : UserRole.EDITOR;

            // we should check if this email valid
            Contender contender = sharingService.shareViaEmail(new Contender(docId, email, null, userRole));

            if (contender != null) {
                return ResponseEntity.ok(email + " was invited to the document");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "/share/via/link", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaLink(@RequestHeader("token") String token, @RequestBody String json) {

        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(json, HashMap.class);

        User user = authService.getCachedUser(token);

        if (user != null) {
            DocumentLink documentLink = sharingService.shareViaLink(user.getEmail(), (HashMap<String, String>) map);

            map.clear();
            map.put("viewerLink", "http://localhost:8080/accept/link-invite/" + documentLink.getViewerToken());
            map.put("editorLink", "http://localhost:8080/accept/link-invite/" + documentLink.getEditorToken());

            return ResponseEntity.ok(gson.toJson(map));
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * email verification, at the end the invited user becomes a viewer or editor of the document
     *
     * @param token (Unique key for each logged user)
     * @return response status - 200 or 404
     */
    @RequestMapping(value = "/accept/email-invite/{token}")
    public String emailVerification(@PathVariable("token") String token) {
        return sharingService.verifyEmailToken(token);
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "/accept/link-invite/{token}")
    public String linkVerification(@RequestHeader("token") String tokenUser, @PathVariable("token") String tokenLink) {
        return sharingService.verifyEmailToken(tokenLink);
    }
}
