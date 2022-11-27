package docSharing.repository;

import docSharing.Entities.Contender;
import docSharing.Entities.Unconfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenderRepository extends JpaRepository<Contender, Long> {

}
