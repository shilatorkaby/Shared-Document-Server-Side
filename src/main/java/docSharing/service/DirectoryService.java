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
            directoryRepository.findById(fatherDir.getFatherId()).ifPresent(optionalDirs::add);
        }
        return optionalDirs.size() > 0 ? optionalDirs : null;
    }

    public Directory changeDir(Directory directory) {

        Directory currentDirectory = directoryRepository.findById(directory.getId()).orElse(null);
        Directory futureFatherDirectory = directoryRepository.findById(directory.getFatherId()).orElse(null);

        if (currentDirectory != null && futureFatherDirectory != null) {

            List<Directory> futureFatherOptions = getOptionToMove(currentDirectory);
            boolean futureFatherDirIsExist = futureFatherOptions.stream().anyMatch(o -> o.getId().equals(futureFatherDirectory.getId()));

            if (futureFatherDirIsExist) {
                directoryRepository.updateFatherId(directory.getFatherId(), directory.getId());
            }
        }
        return directoryRepository.findByFatherIdAndName(directory.getFatherId(), directory.getName());
    }

    public List<Directory> getSubDirs(Directory directory) {
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
