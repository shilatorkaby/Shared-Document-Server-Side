package docSharing.service;

import docSharing.Entities.Directory;
import docSharing.Entities.User;
import docSharing.Entities.UserBody;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DirectoryRepository directoryRepository;
    @Autowired
    private DirectoryRepository documentLinkRepository;
    @Autowired
    private DirectoryRepository docPermissionRepository;

    private static final Logger logger = LogManager.getLogger(DirectoryService.class.getName());



    public List<Directory> getOptionToMove(Directory directory) {
        logger.info("get Option To Move: " + directory.getName());
        directory.setFatherId(directoryRepository.findById(directory.getId()).get().getFatherId());

        if (!directoryRepository.existsById(directory.getId()) || !directoryRepository.existsById(directory.getFatherId())){
            logger.warn("get Option To Move failed");
        return null;
    }
        directory.setFatherId(directoryRepository.findById(directory.getId()).get().getFatherId());
        List<Directory> optionalDirs = new ArrayList<>();
        List<Directory> sisterDirs = directoryRepository.findDirsByFatherId(directory.getFatherId(), directory.getId());
        if (sisterDirs.size() > 0) {
            optionalDirs.addAll(sisterDirs);
        }
        Directory fatherDir = directoryRepository.findById(directory.getFatherId()).orElse(null);
        if (fatherDir != null && fatherDir.getFatherId() > 0) {
            directoryRepository.findById(fatherDir.getFatherId()).ifPresent(optionalDirs::add);
        }
        if(optionalDirs.size() > 0){
            logger.info("get Option To Mov completed");
            return optionalDirs;
        }
        else {
            logger.warn("get Option To Mov failed");
            return null;
        }
    }

    public Directory changeDir(Directory directory) {
        logger.info("Change dir : " + directory.getName());
        Directory currentDirectory = directoryRepository.findById(directory.getId()).orElse(null);
        Directory futureFatherDirectory = directoryRepository.findById(directory.getFatherId()).orElse(null);

        if (currentDirectory != null && futureFatherDirectory != null) {

            List<Directory> futureFatherOptions = getOptionToMove(currentDirectory);
            boolean futureFatherDirIsExist = futureFatherOptions.stream().anyMatch(o -> o.getId().equals(futureFatherDirectory.getId()));

            if (futureFatherDirIsExist) {
                directoryRepository.updateFatherId(directory.getFatherId(), directory.getId());
            }
        }
        Directory dir = directoryRepository.findByFatherIdAndName(directory.getFatherId(), directory.getName());
        logger.info("Directory: "+ dir.getName() + dir.getId());
        return dir;
    }

    public List<Directory> getSubDirs(Directory directory) {
        logger.info("Get subs dir of: " +directory.getName() );
        if (directoryRepository.existsById(directory.getId())) {
            logger.info("Get subs dir completed" );
            return directoryRepository.findByFatherId(directory.getId());
        }
        logger.warn("Get subs dir failed" );
        return null;
    }

    public List<Directory> getSubDirs(UserBody user) {
        logger.info("Get subs dir of: " +user.getEmail() );
        Long userId = userRepository.findByEmail(user.getEmail()).getId();

        Directory rootDir = directoryRepository.findByFatherId(-1 * userId).get(0);
        if (rootDir != null) {
            logger.info("Get subs dir from user completed" );
            return directoryRepository.findByFatherId(rootDir.getId());
        }
        logger.warn("Get subs dir from user failed" );
        return null;
    }


    public Directory addNewDir(UserBody temp, Directory currentDirectory) {
        logger.info("Add new dir :" + currentDirectory.getName() );
        User user = userRepository.findByEmail(temp.getEmail());
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
        if(newDir != null){
            logger.info("Add new dir completed");
            return directoryRepository.save(newDir);
        }
        else{
            logger.warn("Add new dir failed");
            return null;
        }
    }

    boolean directoryIsNotExist(Long fatherId, String name) {
        return directoryRepository.findByFatherIdAndName(fatherId, name) == null;
    }

    public boolean removeDir(Directory directory) {
        logger.info("Remove dir: " + directory.getName());
        if (directory != null) {
            if (directory.getDocId() != null) {
                documentLinkRepository.deleteByDocId(directory.getDocId());
                docPermissionRepository.deleteByDocId(directory.getDocId());
                directoryRepository.deleteByDocId(directory.getDocId());
            } else {
                directoryRepository.delete(directory);
                deleteDirectoryRec(directory);
            }
            logger.info("Remove dir completed");
            return !directoryRepository.existsById(directory.getId());
        }
        logger.warn("Remove dir failed");
        return false;
    }

    private void deleteDirectoryRec(Directory directory) {
        logger.info("Delete dir: " + directory.getName());
        List<Directory> subDirs = directoryRepository.findByFatherId(directory.getId());
        if (subDirs.size() > 0) {
            for (Directory dir : subDirs) {
                if (dir.getDocId() != null) {
                    documentLinkRepository.deleteByDocId(dir.getDocId());
                    docPermissionRepository.deleteByDocId(dir.getDocId());
                    directoryRepository.deleteByDocId(dir.getDocId());
                    System.out.println(dir.getName() + " was deleted");
                } else {
                    deleteDirectoryRec(dir);
                    directoryRepository.delete(dir);
                    logger.info("Delete dir completed");
                }
            }
        }
        logger.warn("Delete dir failed.");
    }


}
