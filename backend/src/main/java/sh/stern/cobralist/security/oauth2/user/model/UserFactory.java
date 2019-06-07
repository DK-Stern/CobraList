package sh.stern.cobralist.security.oauth2.user.model;

import org.springframework.stereotype.Component;

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
