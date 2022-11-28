package docSharing.controller;

import docSharing.Entities.Document;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @Autowired
    private AuthService authService;


        @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody Document document) {
        return docService.save(document);
    }
}