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

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestHeader("token") String token, @RequestBody Document document) {
        User user = authService.getCachedUser(token);

        return docService.save(document);
    }

    /**
     * fetch data about a specific document using his unique document Id
     *
     * @param token (Unique key for each logged user)
     * @param map   (stores the id of the document)
     * @return json Document, wrapped with ResponseEntity
     */
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

    /**
     * receive list of roles of a specific user
     *
     * @param token (Unique key for each logged user)
     * @return json list of DocPermission, wrapped with ResponseEntity
     */
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