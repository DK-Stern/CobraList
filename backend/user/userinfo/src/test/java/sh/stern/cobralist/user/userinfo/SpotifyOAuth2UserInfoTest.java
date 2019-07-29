package sh.stern.cobralist.user.userinfo;

import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class SpotifyOAuth2UserInfoTest {

    private SpotifyOAuth2UserInfo testSubject;

    @Test
    public void getName() {
        // given
        final HashMap<String, Object> attributes = new HashMap<>();
        final String expectedName = "name";
        attributes.put("display_name", expectedName);

        testSubject = new SpotifyOAuth2UserInfo(attributes);

        // when
        final String resultedName = testSubject.getName();

        // then
        assertThat(resultedName).isEqualTo(expectedName);
    }

    @Test
    public void getEmail() {
        // given
        final HashMap<String, Object> attributes = new HashMap<>();
        final String expectedEmail = "email";
        attributes.put("email", expectedEmail);

        testSubject = new SpotifyOAuth2UserInfo(attributes);

        // when
        final String resultedEmail = testSubject.getEmail();

        // then
        assertThat(resultedEmail).isEqualTo(expectedEmail);
    }

    @Test
    public void getId() {
        // given
        final HashMap<String, Object> attributes = new HashMap<>();
        final String expectedId = "spotifyId";
        attributes.put("id", expectedId);

        testSubject = new SpotifyOAuth2UserInfo(attributes);

        // when
        final String resultedId = testSubject.getId();

        // then
        assertThat(resultedId).isEqualTo(expectedId);
    }
}