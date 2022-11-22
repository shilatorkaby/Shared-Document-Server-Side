package docSharing.repository;

import docSharing.Entities.User;
import docSharing.Entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT u FROM VerificationToken u WHERE u.token = :token")
    public VerificationToken findByToken(@Param("token") String token);

    @Query("SELECT u FROM VerificationToken u WHERE u.email = :email")
    public VerificationToken findByEmail(@Param("email") String email);
}
