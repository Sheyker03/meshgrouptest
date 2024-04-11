package t.meshgroup.database.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import t.meshgroup.models.BalanceStats;

@Repository
public interface BalanceStatRepository extends CrudRepository<BalanceStats, Long> {
}
