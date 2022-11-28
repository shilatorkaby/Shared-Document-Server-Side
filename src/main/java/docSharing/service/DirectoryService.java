package docSharing.service;

import docSharing.Entities.Directory;
import docSharing.repository.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryService {
    @Autowired
    private DirectoryRepository directoryRepository;


    public List<Directory> getOptionToMove(Directory directory) {

        if (!directoryRepository.existsById(directory.getId()) || !directoryRepository.existsById(directory.getFatherId()))
            return null;

        List<Directory> optionalDirs = new ArrayList<>();
        List<Directory> sisterDirs = directoryRepository.findDirsByFatherId(directory.getFatherId(), directory.getId());
        if (sisterDirs.size() > 0) {
            optionalDirs.addAll(sisterDirs);
        }
        Directory fatherDir = directoryRepository.findById(directory.getFatherId()).orElse(null);
        if (fatherDir != null && fatherDir.getFatherId() > 0) {
            Directory grandFatherDir = directoryRepository.findById(fatherDir.getFatherId()).orElse(null);
            optionalDirs.add(grandFatherDir);
        }
        return optionalDirs.size() > 0 ? optionalDirs : null;
    }

    public List<Directory> getSonsByDirId(Directory directory) {
        if (directoryRepository.existsById(directory.getId())) {
            return directoryRepository.findByFatherId(directory.getId());
        }
        return null;
    }


    public Directory addNewDir(Directory currentDirectory) {
        if (currentDirectory != null &&
                directoryRepository.existsById(currentDirectory.getFatherId())
                && directoryRepository.findByFatherIdAndName(currentDirectory.getFatherId(), currentDirectory.getName()) == null) {
            Directory newDir = new Directory(currentDirectory.getFatherId(), currentDirectory.getName());
            directoryRepository.save(newDir);
            return newDir;
        }
        return null;
    }


    public Directory changeDir(Long futureFatherId, Long dirId) {

        Directory directory = null;
        if (directoryRepository.findFutureFatherDir(futureFatherId) != null && directoryRepository.existsById(dirId)) {

            directoryRepository.updateFatherId(futureFatherId, dirId);
            directory = directoryRepository.findByIdAndFatherId(futureFatherId, dirId);
        }
        return directory;
    }


    public boolean removeDir(Long dirId) {
        Directory directory = directoryRepository.findById(dirId).orElse(null);
        if (directory != null) {
            directoryRepository.delete(directory);
        }
        return directoryRepository.existsById(dirId);
    }

    public Long getRootId(Long userId) {
        return directoryRepository.findByFatherId(userId * -1).get(0).getId();
    }

}
