package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.DocPermission;
import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @Autowired
    private AuthService authService;

    private static final Gson gson = new Gson();

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody Document document) {
        return docService.save(document);
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public ResponseEntity<String> getDocumentById(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        User user = authService.getCachedUser(token);

        if (user != null) {
            Document temp = docService.getDocumentById(user, Long.parseLong(map.get("id")));
            if (temp != null) {
                return ResponseEntity.ok(gson.toJson(temp));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public ResponseEntity<String> getRolesByToken(@RequestHeader("token") String token) {
        User user = authService.getCachedUser(token);

        if (user != null) {
            List<DocPermission> temp = docService.getRolesByEmail(user.getEmail());
            return ResponseEntity.ok(gson.toJson(temp));
        }
        return ResponseEntity.notFound().build();
    }
}