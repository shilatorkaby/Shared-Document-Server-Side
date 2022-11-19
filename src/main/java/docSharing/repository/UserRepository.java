package docSharing.repository;

import docSharing.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
       @Query("SELECT u FROM user u WHERE u.email = ?1")
        User findByEmail(String email);

}
