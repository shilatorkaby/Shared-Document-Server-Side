package docSharing.repository;

import docSharing.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.fileName = :fileName and d.email = :email")
    Document findByNameAndEmail(@Param("fileName") String fileName, @Param("email") String email);
    @Query("SELECT d FROM Document d WHERE d.id = :id")
    Document findByDocId(@Param("id") Long id);

    @Query("update Document d set d.fileContent = :fileContent WHERE d.id = :id")
    Document updateFileContent(@Param("id") Long id,@Param("fileContent") String fileContent);

    @Query("SELECT d FROM Document d WHERE d.email = :email")
    List<Document> findByEmail(@Param("email") String email);
}
