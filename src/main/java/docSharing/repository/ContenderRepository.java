package docSharing.repository;

import docSharing.Entities.Contender;
import docSharing.Entities.Unconfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenderRepository extends JpaRepository<Contender, Long> {
    @Query("SELECT u FROM Contender u WHERE u.token = :token")
    Contender findByToken(@Param("token") String token);
}
