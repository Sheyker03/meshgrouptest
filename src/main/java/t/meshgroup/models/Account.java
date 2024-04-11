package t.meshgroup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import t.meshgroup.utils.autoloaders.databases.enums.UtilPostgreSQLDomains;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * Official recommendation numeric with 12/4 ~ >
     * <a href="https://docs.oracle.com/javase/1.5.0/docs/guide/jdbc/getstart/mapping.html"/>
     **/
    @Column(columnDefinition = UtilPostgreSQLDomains.ACCOUNT_BALANCE_DOMAIN + " not null")
    private BigDecimal balance;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
