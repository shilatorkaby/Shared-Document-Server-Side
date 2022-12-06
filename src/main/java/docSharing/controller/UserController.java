package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.*;
import docSharing.service.AuthService;
import docSharing.service.DirectoryService;
import docSharing.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(UserController.class.getName());

    private static final Gson gson = new Gson();

    /**
     * gives all the sub-files, using map that includes directory name and its unique id
     *
     * @param token     (Unique key for each logged user)
     * @param directory (stores id and name of the document)
     * @return json list of Directories, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/get/sub-files", method = RequestMethod.POST)
    public ResponseEntity<String> getSubFiles(@RequestHeader("token") String token, @RequestBody Directory directory) {

        logger.info("Get subs files with token: " + token);
        UserBody user = authService.getCachedUser(token);
        if (user != null && directory.getId() != null) {
            List<Directory> subFolders = directoryService.getSubDirs(directory);
            if (subFolders != null) {
                logger.info("Get subs files successfully complete");
                return ResponseEntity.ok(gson.toJson(subFolders));
            } else {
                logger.warn("Sub folder is null");
                return ResponseEntity.notFound().build();
            }
        }
        logger.warn("Get Sub files failed.");
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
        logger.info("Get subs files with token: " + token);
        UserBody user = authService.getCachedUser(token);

        if (user != null) {

            List<Directory> subFolders = directoryService.getSubDirs(user);
            if (subFolders != null) {
                logger.info("Get subs files successfully complete");
                return ResponseEntity.ok(gson.toJson(subFolders));
            } else {
                logger.warn("Sub folder is null");
                return ResponseEntity.notFound().build();
            }
        }
        logger.warn("Get Sub files failed.");
        return ResponseEntity.badRequest().build();
    }

    /**
     * creates directory, and places it according the father's id directory
     *
     * @param token     (Unique key for each logged user)
     * @param directory (stores father id and name of the directory - together they are unique combination to locate a specific directory)
     * @return json Directory, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/create-directory", method = RequestMethod.POST)
    public ResponseEntity<String> createNewDir(@RequestHeader("token") String token, @RequestBody Directory directory) {
        logger.info("Create new dir with token: " + token);
        UserBody user = authService.getCachedUser(token);

        if (directory.getName() != null && user != null) {

            Directory newDir = directoryService.addNewDir(user, directory);

            if (newDir != null) {
                logger.info(newDir.getName() + " was created successfully" + token);
                return ResponseEntity.ok(gson.toJson(newDir));
            }
            logger.warn("New dir is null");
        }
        logger.warn("create new dir failed.");
        return ResponseEntity.notFound().build();
    }

    /**
     * creates directory, and places it according to the father's id directory
     *
     * @param token    (Unique key for each logged user)
     * @param document (DocumentBody class created for serialization)
     * @return json Document, wrapped with ResponseEntity
     */
    @RequestMapping(value = "/create-document", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestHeader("token") String token, @RequestBody DocumentBody document) {
        UserBody user = authService.getCachedUser(token);

        if (user != null && document.getFileName() != null) {
            logger.info("Create document : " + document.getFileName());
            Document temp = userService.createDocument(user, document);
            if (temp != null) {
                logger.info("Create document successfully complete");
                return ResponseEntity.ok(gson.toJson(temp));
            }
            logger.warn("Document is null");
        }
        logger.warn("Create document failed.");
        return ResponseEntity.notFound().build();
    }

    /**
     * NOT USED RIGHT NOW, WILL BE FIXED SOON
     */
    @RequestMapping(value = "change/dir", method = RequestMethod.POST)
    public ResponseEntity<String> changeDir(@RequestHeader("token") String token, @RequestBody Directory directory) {
        if (authService.getCachedUser(token) != null && directory.getId() != null && directory.getName() != null) {
            logger.info("Change dir with token: " + token + "to directory : " + directory.getName());
            Directory changedDir = directoryService.changeDir(directory);
            if (changedDir != null) {
                logger.info("Change dir successfully complete");
                return ResponseEntity.ok(gson.toJson(changedDir));
            }
        }
        logger.warn("Change dir failed.");
        return ResponseEntity.notFound().build();
    }


    /**
     * deletes a directory
     *
     * @param token     (unique String)
     * @param directory
     * @return
     */
    @RequestMapping(value = "delete/dir", method = RequestMethod.POST)
    public ResponseEntity<String> removeDir(@RequestHeader("token") String token, @RequestBody Directory directory) {

        if (authService.getCachedUser(token) != null && directory.getName() != null) {
            logger.info("Remove directory : " + directory.getName());
            if (directoryService.removeDir(directory)) {
                logger.info("Remove dir successfully complete");
                return ResponseEntity.ok("Directory was removed successfully");
            }
        }
        logger.warn("Remove dir failed.");
        return ResponseEntity.notFound().build();
    }


    /**
     * gets all the optional directories to move (directory to another one)
     *
     * @param token     (unique String)
     * @param directory
     * @return json of all optional Directories, wrapped in Response Entity
     */
    @RequestMapping(value = "/get/optional/dir", method = RequestMethod.POST)
    public ResponseEntity<String> getOptionToMove(@RequestHeader("token") String token, @RequestBody Directory directory) {

        if (authService.getCachedUser(token) != null && directory.getId() != null && directory.getName() != null) {
            logger.info("get option to move to directory: " + directory.getName());

            List<Directory> optionalFolders = directoryService.getOptionToMove(directory);
            if (optionalFolders != null) {
                logger.info("get option to move successfully complete");
                return ResponseEntity.ok(gson.toJson(optionalFolders));
            }
        }
        logger.warn("get option to move to directory failed.");
        return ResponseEntity.notFound().build();
    }
}
