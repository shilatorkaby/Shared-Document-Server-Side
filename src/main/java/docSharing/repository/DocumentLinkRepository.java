package docSharing.repository;

import docSharing.Entities.DocumentLink;
import docSharing.Entities.Unconfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentLinkRepository extends JpaRepository<DocumentLink, Long> {
}
