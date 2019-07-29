package sh.stern.cobralist.persistence.domain;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.user.domain.AuthProvider;

@Component
public class UserFactory {
    public User create(String name, String email, AuthProvider authProvider, String providerId) {
        final User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setProvider(authProvider);
        user.setProviderId(providerId);

        return user;
    }
}
