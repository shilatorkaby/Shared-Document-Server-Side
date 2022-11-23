package docSharing.repository;

import docSharing.Entities.Unconfirmed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnconfirmedRepository extends JpaRepository<Unconfirmed, Long> {

    @Query("SELECT u FROM Unconfirmed u WHERE u.token = :token")
    public Unconfirmed findByToken(@Param("token") String token);

    @Query("SELECT u FROM Unconfirmed u WHERE u.email = :email")
    public Unconfirmed findByEmail(@Param("email") String email);
}
