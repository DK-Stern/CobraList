package sh.stern.cobralist.user.userprincipal;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
@Scope("prototype")
public class UserPrincipalBuilder {

    private Long userId;

    private String name;

    private String email;

    private Map<String, Object> attributes;

    private Collection<SimpleGrantedAuthority> authorities;

    private StreamingProvider streamingProvider;

    public UserPrincipalBuilder withUserId(Long id) {
        this.userId = id;
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

    public UserPrincipalBuilder withProvider(StreamingProvider streamingProvider) {
        this.streamingProvider = streamingProvider;
        return this;
    }

    public UserPrincipal build() {
        UserPrincipal userPrincipal = new UserPrincipal();

        checkAttributesNonNull();

        userPrincipal.setId(userId);
        userPrincipal.setName(name);
        userPrincipal.setEmail(email);
        userPrincipal.setAttributes(attributes);
        userPrincipal.setAuthorities(authorities);
        userPrincipal.setStreamingProvider(streamingProvider);

        return userPrincipal;
    }

    private void checkAttributesNonNull() {
        if (isNull(userId)) {
            throw new IllegalStateException("'userId' is null!");
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
