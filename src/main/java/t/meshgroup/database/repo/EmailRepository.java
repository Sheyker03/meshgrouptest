package t.meshgroup.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import t.meshgroup.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    @Query
    boolean existsByEmailAndUserId(String deprecatedEmail, Long userId);

    @Query
    boolean existsByEmail(String email);


    @Query
    int countByUserId(Long userId);

    @Transactional
    @Query
    void deleteEmailByEmailAndUserId(String email, Long userId);


    @Transactional
    @Modifying
    @Query("update Email e set e.email = :newEmail where e.user.id = :userId and e.email = :deprecatedEmail")
    int changeEmail(@Param("userId") Long userId, @Param("deprecatedEmail") String deprecatedEmail, @Param("newEmail") String newEmail);
}
