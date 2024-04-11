package t.meshgroup.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import t.meshgroup.config.SecurityRoles;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(length = 500, nullable = false)
    private String name;

    /**
     * Mb may set to format... ?
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "DD.MM.YYYY")
    private Date dateOfBirth;

    @Size(min = 8)
    @Column(length = 500, nullable = false)
    private String password;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Account account;

    /**
     * If necessary transfer to two-way communication
     **/
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Builder.Default
    @JsonIgnoreProperties("user")
    private Set<Phone> phones = new HashSet<>();

    /**
     * If necessary transfer to two-way communication
     **/
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Builder.Default
    @JsonIgnoreProperties("user")
    private Set<Email> emails = new HashSet<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(SecurityRoles.USER));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
