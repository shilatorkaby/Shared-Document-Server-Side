package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.DocPermission;
import docSharing.Entities.Document;
import docSharing.Entities.UserBody;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {

    @Autowired
    DocService docService;

    @Autowired
    AuthService authService;

    private static final Gson gson = new Gson();

    private static Logger logger = LogManager.getLogger(DocController.class.getName());


    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<String> save(@RequestHeader("token") String token, @RequestBody Document document) {
        logger.info("Save the document: " + document.getId() + " with this token: " + token);

        UserBody user = authService.getCachedUser(token);
        if (user != null && document.getId() != null) {
            logger.info("User email is: " + user.getEmail());
            Document updatedDoc = docService.save(document);
            System.out.println(updatedDoc);
            if (updatedDoc != null) {
                logger.info("file's content was updated successfully: " + updatedDoc);
                return ResponseEntity.ok(gson.toJson(updatedDoc));

            }
        }
        logger.error("File doesn't exists");
        return ResponseEntity.notFound().build();
    }

    /**
     * fetch data about a specific document using his unique document Id
     *
     * @param token (Unique key for each logged user)
     * @param id    (stores the id of the document)
     * @return json Document, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public ResponseEntity<String> getDocumentById(@RequestHeader("token") String token, @RequestBody Long id) {
        UserBody user = authService.getCachedUser(token);
        if (user != null && id != null) {
            Document temp = docService.getDocumentById(user, id);
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
        logger.info("Get the user role with token: " + token);
        UserBody user = authService.getCachedUser(token);
        if (user != null) {
            logger.info("User is not null");
            List<DocPermission> temp = docService.getRolesByEmail(user.getEmail());
            return ResponseEntity.ok(gson.toJson(temp));
        }
        logger.warn("getRolesByToken is failed");
        return ResponseEntity.notFound().build();
    }
}