package docSharing.repository;

import docSharing.Entities.DocPermission;
import docSharing.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DocPermissionRepository extends JpaRepository<DocPermission, Long> {

    @Query("SELECT d FROM DocPermission d WHERE d.docId = :docId")
    DocPermission findByDocId(@Param("docId") Long docId);

    @Query("SELECT d FROM DocPermission d WHERE d.docId = :docId and d.email = :email")
    DocPermission findByDocIdAndEmail(@Param("docId") Long docId, @Param("email") String email);

    @Query("SELECT d FROM DocPermission d WHERE d.email = :email")
    List<DocPermission> findAllPermissionsByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE DocPermission d SET d.role = :role WHERE d.docId = :docId and d.email = :email")
    void updatePermission(@Param("docId") Long docId, @Param("email") String email, @Param("role") UserRole userRole);
}
