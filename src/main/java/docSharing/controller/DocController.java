package docSharing.controller;

import docSharing.Entities.DirBody;
import docSharing.Entities.Directory;
import docSharing.Entities.Document;
import docSharing.service.AuthService;
import docSharing.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @Autowired
    private AuthService authService;


    @RequestMapping(value = "get/subFolders", method = RequestMethod.GET)
    public ResponseEntity<List<Directory>> getSonsByDirId(@RequestParam Long id) {
        if(id != null) {
            List<Directory> subFolders = docService.getSonsByDirId(id);
            if (subFolders != null) {
                return ResponseEntity.ok(subFolders);
            }
        }
        return ResponseEntity.notFound().build();
    }
    @RequestMapping(value = "add/dir", method = RequestMethod.POST)
    public ResponseEntity<String> addNewDir(@RequestBody Directory directory) {
        if(directory != null) {
            String subFoldersJson = docService.addNewDir(directory);
            if (subFoldersJson != null) {
                return ResponseEntity.ok(subFoldersJson);
            }
        }
        return ResponseEntity.notFound().build();
    }

        @RequestMapping(value = "change/dir", method = RequestMethod.POST)
        public ResponseEntity<String> changeDir(DirBody dirBody) {
        if(dirBody.directory != null && dirBody.fatherId != null) {
            List<Long> subDirs = docService.changeDir(dirBody.fatherId,dirBody.directory.getId());
            if (subDirs != null) {
                return ResponseEntity.ok(subDirs.toString());
            }
        }
        return ResponseEntity.notFound().build();
    }
    @RequestMapping(value = "delete/dir", method = RequestMethod.POST)
        public ResponseEntity<String> removeDir(@RequestBody Directory directory) {
        if(directory != null) {
            if (docService.removeDir(directory.getId())) {
                return ResponseEntity.ok("Directory was deleted successfully");
            }
        }
        return ResponseEntity.notFound().build();
    }



    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody Document document) {
        return docService.save(document);
    }
}