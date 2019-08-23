package sh.stern.cobralist.party.persistence.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.user.domain.StreamingProvider;

public class UserFactoryTest {

    private UserFactory testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new UserFactory();
    }

    @Test
    public void createUser() {
        // given
        final String name = "name";
        final String email = "email@email.de";
        final StreamingProvider streamingProvider = StreamingProvider.spotify;
        final String providerId = "providerId";

        // when
        User user = testSubject.create(name, email, streamingProvider, providerId);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(user.getName()).isEqualTo(name);
        softly.assertThat(user.getEmail()).isEqualTo(email);
        softly.assertThat(user.getProvider()).isEqualTo(streamingProvider);
        softly.assertThat(user.getProviderId()).isEqualTo(providerId);
        softly.assertAll();
    }
}