package sh.stern.cobralist.party.persistence.domain;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.user.domain.StreamingProvider;

@Component
public class UserFactory {
    public User create(String name, String email, StreamingProvider streamingProvider, String providerId) {
        final User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setProvider(streamingProvider);
        user.setProviderId(providerId);

        return user;
    }
}
