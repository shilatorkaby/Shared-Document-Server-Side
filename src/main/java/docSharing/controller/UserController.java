package docSharing.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import docSharing.Entities.Directory;
import docSharing.Entities.DocumentBody;
import docSharing.Entities.Document;
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

    @RequestMapping(value = "get/subFolders", method = RequestMethod.GET)
    public ResponseEntity<String> getSonsByDirId(@RequestBody Directory directory) {
        if(directory != null) {
            List<Directory> subFolders = directoryService.getSonsByDirId(directory);
            if (subFolders != null) {
                return ResponseEntity.ok(gson.toJson(subFolders));
            }
        }
        return ResponseEntity.notFound().build();
    }
    @RequestMapping(value = "add/new/dir", method = RequestMethod.POST)
    public ResponseEntity<String> createNewDir(@RequestHeader ("token") String token,@RequestBody Directory directory) {
        User user = authService.getCachedUser(token);

        if(user != null && directory != null) {
            Directory newDir = directoryService.addNewDir(directory);
            if (newDir != null) {
                return ResponseEntity.ok(gson.toJson(newDir));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestHeader ("token") String token, @RequestBody DocumentBody document){

        User user = authService.getCachedUser(token);

        if(user != null){
            Document temp = userService.createDocument(user, document);
            if(temp != null)
                return ResponseEntity.ok(temp.toString());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "change/dir", method = RequestMethod.POST)
    public ResponseEntity<String> changeDir(@RequestBody Directory directory) {
        if(directory != null && directory.getFatherId() != null) {
            Directory changedDir = directoryService.changeDir(directory.getFatherId(),directory.getId());
            if (changedDir != null) {
                return ResponseEntity.ok(gson.toJson(changedDir));
            }
        }
        return ResponseEntity.notFound().build();
    }
    @RequestMapping(value = "delete/dir", method = RequestMethod.POST)
    public ResponseEntity<String> removeDir(@RequestBody Directory directory) {
        if(directory != null) {
            if (directoryService.removeDir(directory.getId())) {
                return ResponseEntity.ok("Directory was deleted successfully");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "get/optional/dir", method = RequestMethod.GET)
    public ResponseEntity<String> getOptionToMove(@RequestBody Directory directory) {
        if(directory != null) {
            List<Directory> optionalFolders = directoryService.getOptionToMove(directory);
            if (optionalFolders != null) {
                return ResponseEntity.ok(gson.toJson(optionalFolders));
            }
        }
        return ResponseEntity.notFound().build();

    }




    @RequestMapping(value="/get/docs", method = RequestMethod.POST)
    public ResponseEntity<String> getAllDocs(@RequestBody String json){

        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(json, HashMap.class);
        String token = map.get("token");
        System.out.println(token);

        gson = new GsonBuilder().setPrettyPrinting().create();
        User user = authService.getCachedUser(token);

        if(user != null){
            String docs = gson.toJson(userService.getAllDocs(user));
            if(docs != null)
                return ResponseEntity.ok(docs);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") int id){
        return ResponseEntity.noContent().build();
    }
}
