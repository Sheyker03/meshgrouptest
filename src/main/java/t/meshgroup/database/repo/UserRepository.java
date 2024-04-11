package t.meshgroup.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import t.meshgroup.database.custom.UserRepositoryCustom;
import t.meshgroup.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Query(value = "select users.id from phones, emails, users where emails.email = :email limit 1", nativeQuery = true)
    Long findUserIdByEmail(String email);

    @Override
    List<User> findUsers(String dateOfBirth, String phone, String email, String name, Integer limit, Integer offset);
}
