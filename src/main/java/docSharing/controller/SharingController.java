package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Contender;
import docSharing.Entities.DocumentLink;
import docSharing.Entities.User;
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

    @RequestMapping(value = "/share/via/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestBody Contender contender) {
        // we should check if this email valid
        sharingService.shareViaEmail(contender);
        return ResponseEntity.ok("All good");
    }

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

    @RequestMapping(value = "/accept/email-invite/{token}")
    public String emailVerification(@PathVariable("token") String token) {
        return sharingService.verifyEmailToken(token);
    }

    @RequestMapping(value = "/accept/link-invite/{token}")
    public String linkVerification(@RequestHeader("token") String tokenUser, @PathVariable("token") String tokenLink) {
        return sharingService.verifyEmailToken(tokenLink);
    }
}
