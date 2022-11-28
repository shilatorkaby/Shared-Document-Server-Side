package docSharing.repository;

import docSharing.Entities.DocumentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentLinkRepository extends JpaRepository<DocumentLink, Long> {
    @Query("SELECT d FROM DocumentLink d WHERE d.documentId = :documentId")
    DocumentLink findByDocId(@Param("documentId") Long docId);

    @Query("SELECT d FROM DocumentLink d WHERE d.editorToken = :token")
    DocumentLink findByEditorToken(@Param("token") String token);

    @Query("SELECT d FROM DocumentLink d WHERE d.viewerToken = :token")
    DocumentLink findByViewerToken(@Param("token") String token);
}
