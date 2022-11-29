package docSharing.repository;

import docSharing.Entities.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {

     @Query("SELECT d FROM Directory d WHERE d.fatherId = :fatherId")
     List<Directory> findByFatherId(@Param("fatherId") Long fatherId);
     @Query("SELECT d FROM Directory d WHERE d.fatherId = :fatherId")
     Directory getRootDir(@Param("fatherId") Long fatherId);
     @Query("SELECT d FROM Directory d WHERE d.fatherId = :fatherId and d.docId is null and d.id <>:id")
     List<Directory> findDirsByFatherId(@Param("fatherId") Long fatherId,@Param("id") Long id);
     @Query("SELECT d FROM Directory d WHERE d.id = :id and d.docId is null")
     Directory findFutureFatherDir(@Param("id") Long id);
     @Query("SELECT d FROM Directory d WHERE d.fatherId= :fatherId and d.id = :id")
     Directory findByIdAndFatherId(@Param("fatherId") Long fatherId, @Param("id") Long id);
     @Query("SELECT d FROM Directory d WHERE d.fatherId= :fatherId and d.name = :name")
     Directory findByFatherIdAndName(@Param("fatherId") Long fatherId, @Param("name") String name);
     @Modifying
     @Transactional
     @Query("UPDATE Directory d set d.fatherId = :fatherId WHERE d.id = :id")
     void updateFatherId(@Param("fatherId") Long fatherId,@Param("id") Long id);
     @Modifying
     @Transactional
     @Query("DELETE from Directory d where d.name = :name")
     void deleteByName(@Param("name") String name);






}
