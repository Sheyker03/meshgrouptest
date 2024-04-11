package t.meshgroup.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import t.meshgroup.models.Account;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Account as a set a.balance = a.balance + :money where a.id = :accountId")
    public void addMoney(Long accountId, BigDecimal money);

    @Transactional
    @Modifying
    @Query(value = "update Account as a set a.balance = a.balance - :money where  a.id = :accountId")
    public void minusMoney(Long accountId, BigDecimal money);
}
