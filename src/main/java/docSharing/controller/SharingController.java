package docSharing.controller;

import docSharing.Entities.Contender;
import docSharing.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/share/via")
public class SharingController {

    @Autowired
    SharingService sharingService;

    @RequestMapping(value="/email", method = RequestMethod.POST)
    public ResponseEntity<String> shareViaEmail(@RequestBody Contender contender){
        // we should check if this email valid
        sharingService.sendmail(contender);
        return ResponseEntity.ok("All good");
    }

}
