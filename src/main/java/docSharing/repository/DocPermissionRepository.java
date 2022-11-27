package docSharing.repository;

import docSharing.Entities.DocPermission;
import docSharing.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocPermissionRepository extends JpaRepository<DocPermission, Long> {

    @Query("SELECT d FROM DocPermission d WHERE d.docId = :docId")
    DocPermission findByDocId(@Param("docId") Long docId);

    @Query("SELECT d FROM DocPermission d WHERE d.docId = :docId and d.email = :email")
    DocPermission findByDocIdAndEmail(@Param("docId") Long docId, @Param("email") String email);
}
