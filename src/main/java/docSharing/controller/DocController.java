package docSharing.controller;

import docSharing.Entities.Document;
import docSharing.Entities.User;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @Autowired
    private AuthService authService;

    public void importDoc(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            int i;
            while ((i = fileReader.read()) != -1) ;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody Document document) {
        return docService.save(document);
    }
}