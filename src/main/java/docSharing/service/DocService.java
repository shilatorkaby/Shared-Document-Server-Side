package docSharing.service;
import docSharing.Entities.Document;
import docSharing.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    public String save(Document document)
    {
        if (getDocFromDatabase(document) != null) {
            docRepository.updateFileContent(document.getId(), document.getFileContent());
            return "file's content was updated";
        }
        return "File doesn't exists";
    }

    Document getDocFromDatabase(Document document)
    {
        return docRepository.findByDocId(document.getId());
    }

}
