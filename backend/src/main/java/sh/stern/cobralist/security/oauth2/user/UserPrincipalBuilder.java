package sh.stern.cobralist.security.oauth2.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.security.oauth2.user.model.AuthProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
public class UserPrincipalBuilder {


    private Long id;

    private String name;

    private String email;

    private Map<String, Object> attributes;

    private Collection<SimpleGrantedAuthority> authorities;

    private AuthProvider authProvider;

    public UserPrincipalBuilder withId(Long id) {
        this.id = id;
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

    public UserPrincipalBuilder withAuthorities(Collection<SimpleGrantedAuthority> authorities) {
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
        setAuthoritiesIfNull();

        userPrincipal.setId(id);
        userPrincipal.setName(name);
        userPrincipal.setEmail(email);
        userPrincipal.setAttributes(attributes);
        userPrincipal.setAuthorities(authorities);
        userPrincipal.setAuthProvider(authProvider);

        return userPrincipal;
    }

    private void setAuthoritiesIfNull() {
        if (isNull(authorities)) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    private void checkAttributesNonNull() {
        if (isNull(id)) {
            throw new IllegalStateException("'id' is null!");
        }
        if (isNull(name)) {
            throw new IllegalStateException("'name' is null!");
        }
        if (isNull(email)) {
            throw new IllegalStateException("'email' is null!");
        }
        if (isNull(authProvider)) {
            throw new IllegalStateException("'authProvider' is null!");
        }
    }

}
