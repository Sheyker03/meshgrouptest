package t.meshgroup.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import t.meshgroup.models.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query
    boolean existsByPhoneAndUserId(String phone, Long userId);

    @Query
    boolean existsByPhone(String phone);

    @Query
    int countByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("update Phone p set p.phone = :newPhone where p.user.id = :userId and p.phone = :deprecatedPhone")
    int changePhone(@Param("userId") Long userId, @Param("deprecatedPhone") String deprecatedPhone, @Param("newPhone") String newPhone);

    @Transactional
    @Query
    void deletePhoneByPhoneAndUserId(String phone, Long userId);
}
