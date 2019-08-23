package sh.stern.cobralist.party.persistence.domain;

import org.springframework.lang.NonNull;
import sh.stern.cobralist.user.domain.StreamingProvider;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @Email
    private String email;

    @NonNull
    @Enumerated(EnumType.STRING)
    private StreamingProvider provider;

    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Party> parties;

    @ManyToMany
    @JoinTable(
            name = "user_joined_party",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id"))
    private Set<Party> joinedParties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StreamingProvider getProvider() {
        return provider;
    }

    public void setProvider(StreamingProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Set<Party> getParties() {
        return parties;
    }

    public Set<Party> getJoinedParties() {
        return joinedParties;
    }
}
