package sh.stern.cobralist.security.oauth2;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.security.oauth2.user.userinfo.OAuth2UserInfo;
import sh.stern.cobralist.security.oauth2.user.userinfo.SpotifyOAuth2UserInfo;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2UserInfoFactoryTest {

    private OAuth2UserInfoFactory testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new OAuth2UserInfoFactory();
    }

    @Test
    public void createUserInfoForSpotifyProvider() {
        // given
        final String registrationId = "SPOTIFY";
        final HashMap<String, Object> attributes = new HashMap<>();

        // when
        final OAuth2UserInfo userInfo = testSubject.createUserInfo(registrationId, attributes);

        // then
        assertThat(userInfo).isInstanceOf(SpotifyOAuth2UserInfo.class);
    }

    @Test
    public void throwsOAuth2AuthenticationProcessingExceptionIfRegistrationIdIsNotSupported() {
        // given
        final String registrationId = "unknownProvider";
        final HashMap<String, Object> attributes = new HashMap<>();

        // then
        Assertions.assertThatCode(() -> testSubject.createUserInfo(registrationId, attributes))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class)
                .withFailMessage("Sorry!, Login with 'unknownProvider' is not supported yet.");
    }
}