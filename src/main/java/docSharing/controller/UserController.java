package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Directory;
import docSharing.Entities.Document;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import docSharing.service.DirectoryService;
import docSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    DirectoryService directoryService;
    private static final Gson gson = new Gson();

    /**
     * gives all the sub-files, using map that includes directory name and its unique id
     *
     * @param token (Unique key for each logged user)
     * @param map   (stores id and name of the document)
     * @return json list of Directories, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/get/sub-files", method = RequestMethod.POST)
    public ResponseEntity<String> getSubFiles(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        User user = authService.getCachedUser(token);
        if (map != null) {
            Directory directory = new Directory(null, map.get("name"));
            directory.setId(Long.parseLong(map.get("id")));

            if (user != null) {
                List<Directory> subFolders = directoryService.getSubDirs(directory);
                if (subFolders != null) {
                    return ResponseEntity.ok(gson.toJson(subFolders));
                } else return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();

    }

    /**
     * gives all the sub-files from "root" directory
     *
     * @param token (Unique key for each logged user)
     * @return json list of Directories, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/get/root/sub-files", method = RequestMethod.POST)
    public ResponseEntity<String> getSubFiles(@RequestHeader("token") String token) {

        User user = authService.getCachedUser(token);

        if (user != null) {

            List<Directory> subFolders = directoryService.getSubDirs(user);
            if (subFolders != null) {
                return ResponseEntity.ok(gson.toJson(subFolders));
            } else return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();

    }

    /**
     * creates directory, and places it according the father's id directory
     *
     * @param token (Unique key for each logged user)
     * @param map   (stores father id and name of the directory - together they are unique combination to locate a specific directory)
     * @return json Directory, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/create-directory", method = RequestMethod.POST)
    public ResponseEntity<String> createNewDir(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        User user = authService.getCachedUser(token);

        if (map != null && user != null) {
            Directory newDir;
            if (map.get("fatherId") != null) {
                newDir = directoryService.addNewDir(user, new Directory(Long.parseLong(map.get("fatherId")), map.get("name")));
            } else {
                newDir = directoryService.addNewDir(user, new Directory(null, map.get("name")));
            }
            if (newDir != null) {
                return ResponseEntity.ok(gson.toJson(newDir));
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * creates directory, and places it according the father's id directory
     *
     * @param token (Unique key for each logged user)
     * @param document (DocumentBody class created for serialization)
     * @return json Document, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/create-document", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestHeader("token") String token, @RequestBody DocumentBody document) {

        User user = authService.getCachedUser(token);

        if (user != null) {

            Document temp = userService.createDocument(user, document);
            if (temp != null) return ResponseEntity.ok(gson.toJson(temp));
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "change/dir", method = RequestMethod.POST)
    public ResponseEntity<String> changeDir(@RequestHeader("token") String token, @RequestBody Directory directory) {
        if (authService.getCachedUser(token) != null && directory != null) {
            Directory changedDir = directoryService.changeDir(directory);
            if (changedDir != null) {
                return ResponseEntity.ok(gson.toJson(changedDir));
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "delete/dir", method = RequestMethod.POST)
    public ResponseEntity<String> removeDir(@RequestHeader("token") String token, @RequestBody Directory directory) {
        if (authService.getCachedUser(token) != null && directory != null) {
            if (directoryService.removeDir(directory.getId())) {
                return ResponseEntity.ok("Directory was deleted successfully");
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "get/optional/dir", method = RequestMethod.GET)
    public ResponseEntity<String> getOptionToMove(@RequestHeader("token") String token, @RequestBody Directory directory) {
        if (authService.getCachedUser(token) != null && directory != null) {
            List<Directory> optionalFolders = directoryService.getOptionToMove(directory);
            if (optionalFolders != null) {
                return ResponseEntity.ok(gson.toJson(optionalFolders));
            }
        }
        return ResponseEntity.notFound().build();
    }

}
