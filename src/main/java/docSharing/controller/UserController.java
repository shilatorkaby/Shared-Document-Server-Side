package docSharing.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    private DirectoryService directoryService;
    private static final Gson gson = new Gson();

    @RequestMapping(value = "/get/sub-files", method = RequestMethod.POST)
    public ResponseEntity<String> getSubFiles(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        User user = authService.getCachedUser(token);

        Directory directory = new Directory(null, map.get("name"));
        directory.setId(Long.parseLong(map.get("id")));

        if (user != null) {
            List<Directory> subFolders = directoryService.getSubDirs(directory);
            if (subFolders != null) {
                return ResponseEntity.ok(gson.toJson(subFolders));
            } else
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();

    }

    @RequestMapping(value = "/get/root/sub-files", method = RequestMethod.POST)
    public ResponseEntity<String> getSubFiles(@RequestHeader("token") String token) {

        User user = authService.getCachedUser(token);


        if (user != null) {

            List<Directory> subFolders = directoryService.getSubDirs(user);
            if (subFolders != null) {
                return ResponseEntity.ok(gson.toJson(subFolders));
            } else
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();

    }

    @RequestMapping(value = "/create-directory", method = RequestMethod.POST)
    public ResponseEntity<String> createNewDir(@RequestHeader("token") String token, @RequestBody HashMap<String, String> map) {
        User user = authService.getCachedUser(token);

        Directory directory = new Directory(null, map.get("name"));

        if (user != null) {
            Directory newDir = directoryService.addNewDir(user,directory);
            if (newDir != null) {
                return ResponseEntity.ok(gson.toJson(newDir));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/create-document", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestHeader("token") String token, @RequestBody DocumentBody document) {

        User user = authService.getCachedUser(token);

        if (user != null ) {

            Document temp = userService.createDocument(user, document);
            if (temp != null)
                return ResponseEntity.ok(gson.toJson(temp));
        }
        return ResponseEntity.notFound().build();
    }

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

    @RequestMapping(value = "delete/dir", method = RequestMethod.POST)
    public ResponseEntity<String> removeDir(@RequestHeader("token") String token, @RequestBody Directory directory) {
        if (authService.getCachedUser(token) != null && directory != null) {
            if (!directoryService.removeDir(directory.getId())) {
                return ResponseEntity.ok("Directory was deleted successfully");
            }
        }
        return ResponseEntity.notFound().build();
    }

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


    @RequestMapping(value = "/get/docs", method = RequestMethod.POST)
    public ResponseEntity<String> getAllDocs(@RequestHeader("token") String token) {

        User user = authService.getCachedUser(token);

        if (user != null) {
            String docs = gson.toJson(userService.getAllDocs(user));
            if (docs != null)
                return ResponseEntity.ok(docs);
        }
        return ResponseEntity.notFound().build();
    }
}
