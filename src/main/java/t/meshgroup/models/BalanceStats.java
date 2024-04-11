package t.meshgroup.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RedisHash("Balance")
public class BalanceStats implements Serializable {
    @Id
    private Long userId;
    private BigDecimal maxBalance;
    private long lastUpdate;

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
