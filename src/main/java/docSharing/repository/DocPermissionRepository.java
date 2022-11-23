package docSharing.repository;

import docSharing.Entities.DocPermission;
import docSharing.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocPermissionRepository extends JpaRepository<DocPermission, Long> {

    @Query("SELECT d FROM DocPermission d WHERE d.docId = :docId")
    DocPermission findByDocId(@Param("docId") Long docId);

}
