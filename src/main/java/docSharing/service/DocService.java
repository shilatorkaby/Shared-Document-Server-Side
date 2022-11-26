package docSharing.service;

import com.google.gson.Gson;
import docSharing.Entities.Directory;
import docSharing.Entities.Document;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;
    @Autowired
    private DirectoryRepository directoryRepository;

    private static final Gson gson = new Gson();

    public String save(Document document) {
        if (getDocFromDatabase(document) != null) {
            docRepository.updateFileContent(document.getId(), document.getFileContent());
            return "file's content was updated";
        }
        return "File doesn't exists";
    }

    Document getDocFromDatabase(Document document) {
        return docRepository.findByDocId(document.getId());
    }

    public List<Directory> getSonsByDirId(Long id) {
        if (directoryRepository.existsById(id)) {
            return directoryRepository.findByFatherId(id);
        }
        return null;
    }

    public String addNewDir(Directory directory) {
        return (directory.getFatherId() != null) ?
                addNewDir(directory.getFatherId(),directory.getDirName()) : addNewDir(directory.getDirName());

    }

    public String addNewDir(Long fatherId, String dirName) {
        Directory fatherDir = directoryRepository.findById(fatherId).orElse(null);
        if (fatherDir != null) {
            Directory newDir = new Directory(fatherId, dirName);
            fatherDir.getSubDirs().add(newDir.getId());
            directoryRepository.save(newDir);
        }
        return (fatherDir != null) ? gson.toJson(fatherDir.getSubDirs()) : null;
    }

    public String addNewDir(String dirName) {
        Directory dir = new Directory(dirName);
        directoryRepository.save(dir);
        return gson.toJson(directoryRepository.findById(dir.getId()).orElse(null));
    }

    public List<Long> changeDir(Long futureFatherId, Long dirId) {
        Directory currentDir = directoryRepository.findById(dirId).orElse(null);

        if (currentDir != null) {
            Directory fatherDir = directoryRepository.findById(currentDir.getFatherId()).orElse(null);
            Directory futureFatherDir = directoryRepository.findById(futureFatherId).orElse(null);

            if (futureFatherDir != null) {
                if (fatherDir != null) {
                    fatherDir.getSubDirs().remove(dirId);
                }
                futureFatherDir.getSubDirs().add(dirId);
                currentDir.setFatherId(futureFatherId);
                return futureFatherDir.getSubDirs();
            }
        }
        return null;
    }


    public boolean removeDir(Long dirId) {
        Directory directory = directoryRepository.findById(dirId).orElse(null);
        if (directory != null && directory.getSubDirs().size() == 0) {
            directoryRepository.delete(directory);
        }
        return directoryRepository.existsById(dirId);
    }


}
