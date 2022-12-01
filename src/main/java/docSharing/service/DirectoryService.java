package docSharing.service;

import docSharing.Entities.Directory;
import docSharing.Entities.User;
import docSharing.repository.DirectoryRepository;
import docSharing.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryService {

    private static Logger logger = LogManager.getLogger(DirectoryService.class.getName());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DirectoryRepository directoryRepository;


    public List<Directory> getOptionToMove(Directory directory) {

        if (!directoryRepository.existsById(directory.getId()) || !directoryRepository.existsById(directory.getFatherId())) {
            if(!directoryRepository.existsById(directory.getId())){
                logger.warn("Directory not exist in the repository");
            }
            else{
                logger.warn("Directory's father not exist in the repository");
            }
            logger.warn("Get option to move failed.");
            return null;
        }
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
        logger.info("Change dir completed");
        return directoryRepository.findByFatherIdAndName(directory.getFatherId(), directory.getName());
    }

    public List<Directory> getSubDirs(Directory directory) {
        if (directoryRepository.existsById(directory.getId())) {
            logger.info("Get subs dir completed");
            return directoryRepository.findByFatherId(directory.getId());
        }
        logger.warn("Get subs dir failed.");
        return null;
    }

    public List<Directory> getSubDirs(User user) {

        Long userId = userRepository.findByEmail(user.getEmail()).getId();

        Directory rootDir = directoryRepository.findByFatherId(-1 * userId).get(0);
        if (rootDir != null) {
            logger.info("Get subs dir completed");
            return directoryRepository.findByFatherId(rootDir.getId());
        }
        logger.warn("Get subs dir failed.");
        return null;
    }


    public Directory addNewDir(User user, Directory currentDirectory) {
        logger.info("Add new directory: "+currentDirectory);
        user.setId(userRepository.findByEmail(user.getEmail()).getId());
        Directory newDir = null;

        if (currentDirectory != null && user.getId() != null) {
            if (currentDirectory.getFatherId() != null && directoryRepository.existsById(currentDirectory.getFatherId()) &&
                    directoryIsNotExist(currentDirectory.getFatherId(), currentDirectory.getName())) {
                newDir = new Directory(currentDirectory.getFatherId(), currentDirectory.getName());
            } else if (currentDirectory.getFatherId() == null) {

                Directory directory = directoryRepository.findByFatherId(-1 * user.getId()).get(0);
                Long rootId = directory.getId();

                if (directoryIsNotExist(rootId, currentDirectory.getName())) {
                    newDir = new Directory(rootId, currentDirectory.getName());
                }
            }
        }
        return newDir != null ? directoryRepository.save(newDir) : null;
    }

    boolean directoryIsNotExist(Long fatherId, String name) {
        logger.warn("Directory not exist");
        return directoryRepository.findByFatherIdAndName(fatherId, name) == null;
    }

    public boolean removeDir(Long dirId) {
        logger.info("Remove directory: "+dirId);
        Directory directory = directoryRepository.findById(dirId).orElse(null);
        if (directory != null) {
            directoryRepository.delete(directory);
            logger.info("Remove directory completed");
            return !directoryRepository.existsById(dirId);
        }
        logger.warn("Remove directory failed.");
        return false;
    }

    public Long getRootId(Long userId) {
        return directoryRepository.findByFatherId(userId * -1).get(0).getId();
    }

}
