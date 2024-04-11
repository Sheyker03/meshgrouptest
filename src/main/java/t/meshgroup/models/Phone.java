package t.meshgroup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import t.meshgroup.utils.autoloaders.databases.enums.UtilPostgreSQLDomains;

import javax.persistence.*;
import java.io.Serializable;

/**
 * If need it, may create interface fo tables with 1 value and oneToOne to User.
 **/
@Data
@Entity
@Table(name = "phones")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Phone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(columnDefinition = UtilPostgreSQLDomains.PHONE_NUMBER_DOMAIN + " not null unique")
    private String phone;

    @ManyToOne
    private User user;
}
