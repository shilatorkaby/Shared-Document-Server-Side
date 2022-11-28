package docSharing.controller;

import com.google.gson.Gson;
import docSharing.Entities.Contender;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import docSharing.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class SharingController {

    @Autowired
    AuthService authService;

    @Autowired
    SharingService sharingService;

    @RequestMapping(value="/share/via/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestBody Contender contender){
        // we should check if this email valid
        sharingService.shareViaEmail(contender);
        return ResponseEntity.ok("All good");
    }

//    @RequestMapping(value="/share/via/link", method = RequestMethod.POST)
//    public ResponseEntity<String> shareViaLink(@RequestBody String json){
//
//        Gson gson = new Gson();
//        Map<String, String> map = gson.fromJson(json, HashMap.class);
//        String token = map.get("token");
//
//        User user = authService.getCachedUser(token);
//
//        if(user != null){
//            sharingService.shareViaLink(map);
//            if(docs != null)
//                return ResponseEntity.ok(docs);
//        }
//        return ResponseEntity.notFound().build();
//    }

    @RequestMapping(value = "/accept/email-invite/{token}")
    public String emailVerification(@PathVariable("token") String token) {
        return sharingService.verifyToken(token);
    }
}
