package sh.stern.cobralist.user.userprincipal;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.user.domain.AuthProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
@Scope("prototype")
public class UserPrincipalBuilder {

    private Long partyId;

    private String name;

    private String email;

    private Map<String, Object> attributes;

    private Collection<SimpleGrantedAuthority> authorities;

    private AuthProvider authProvider;

    public UserPrincipalBuilder withPartyId(Long id) {
        this.partyId = id;
        return this;
    }

    public UserPrincipalBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserPrincipalBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserPrincipalBuilder withAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public UserPrincipalBuilder withAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public UserPrincipalBuilder withProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
        return this;
    }

    public UserPrincipal build() {
        UserPrincipal userPrincipal = new UserPrincipal();

        checkAttributesNonNull();

        userPrincipal.setId(partyId);
        userPrincipal.setName(name);
        userPrincipal.setEmail(email);
        userPrincipal.setAttributes(attributes);
        userPrincipal.setAuthorities(authorities);
        userPrincipal.setAuthProvider(authProvider);

        return userPrincipal;
    }

    private void checkAttributesNonNull() {
        if (isNull(partyId)) {
            throw new IllegalStateException("'partyId' is null!");
        }
        if (isNull(name)) {
            throw new IllegalStateException("'name' is null!");
        }
        if (isNull(email) && !isNull(authorities) && authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            throw new IllegalStateException("'email' is null!");
        }
        if (isNull(authorities)) {
            throw new IllegalStateException("'authorities' are null!");
        }
    }

}
