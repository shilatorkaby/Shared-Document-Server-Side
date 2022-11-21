package docSharing.controller;

import docSharing.Entities.Document;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {
//    @Autowired
//    private DocService docService;
//
//    public void importDod(String fileName) {
//        try {
//            FileReader fileReader = new FileReader(fileName);
//            int i;
//            while ((i = fileReader.read()) != -1) ;
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void exportDoc(String fileName, String text) {
//        BufferedWriter writer = null;
//        try {
//            writer = new BufferedWriter(new FileWriter(fileName));
//            writer.write(text);
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String save(@RequestBody Document document) {
//        return docService.save(document);
//    }
}