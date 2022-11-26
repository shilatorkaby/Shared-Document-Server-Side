package docSharing.repository;

import docSharing.Entities.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectoryRepository  extends JpaRepository<Directory, Long> {

     @Query("SELECT d FROM Directory d WHERE d.fatherId = :fatherId")
     List<Directory> findByFatherId(@Param("fatherId") Long fatherId);

     @Query("SELECT d FROM Directory d WHERE d.dirName = :dirName")
     List<Directory> findByDirName(@Param("dirName") String dirName);

}
