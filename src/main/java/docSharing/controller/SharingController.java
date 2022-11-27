package docSharing.controller;

import docSharing.Entities.Contender;
import docSharing.Entities.User;
import docSharing.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;

@RestController
@CrossOrigin
public class SharingController {

    @Autowired
    SharingService sharingService;

    @RequestMapping(value="/share/via/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestBody Contender contender){
        // we should check if this email valid
        sharingService.shareViaEmail(contender);
        return ResponseEntity.ok("All good");
    }

    @RequestMapping(value = "/accept/email-invite/{token}")
    public String emailVerification(@PathVariable("token") String token) {
        return sharingService.verifyToken(token);
    }

}
