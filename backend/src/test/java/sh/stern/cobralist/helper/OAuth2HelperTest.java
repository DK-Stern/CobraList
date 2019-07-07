package sh.stern.cobralist.helper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import sh.stern.cobralist.security.TokenProvider;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;
import sh.stern.cobralist.security.oauth2.user.model.AuthProvider;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2HelperTest {

    private OAuth2Helper testSubject;

    @Mock
    private TokenProvider tokenProviderMock;

    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizationClientServiceMock;

    @Before
    public void setUp() throws Exception {
        testSubject = new OAuth2Helper(tokenProviderMock, oAuth2AuthorizationClientServiceMock);
    }

    @Test
    public void createTokenForUser() {
        // given
        final UserPrincipal expectedUserPrincipal = new UserPrincipal();
        expectedUserPrincipal.setId(1L);
        expectedUserPrincipal.setAuthProvider(AuthProvider.spotify);
        expectedUserPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // when
        testSubject.createToken(expectedUserPrincipal);

        // then
        final ArgumentCaptor<OAuth2AuthenticationToken> oAuth2AuthenticationTokenArgumentCaptor =
                ArgumentCaptor.forClass(OAuth2AuthenticationToken.class);
        verify(tokenProviderMock).createToken(oAuth2AuthenticationTokenArgumentCaptor.capture());

        final OAuth2AuthenticationToken capturedAuthentication = oAuth2AuthenticationTokenArgumentCaptor.getValue();
        assertThat(capturedAuthentication.getPrincipal()).isEqualTo(expectedUserPrincipal);
    }

    @Test
    public void throwsExceptionIfAuthProviderIsNotSetOnCreatingToken() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(1L);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // then
        assertThatCode(() -> testSubject.createToken(userPrincipal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("'AuthProvider' vom User muss gesetzt sein.");
    }

    @Test
    public void throwsExceptionIfIdIsNotSetOnCreatingToken() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setAuthProvider(AuthProvider.spotify);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // then
        assertThatCode(() -> testSubject.createToken(userPrincipal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("'id' vom User muss gesetzt sein.");
    }

    @Test
    public void throwsExceptionIfAuthoritiesIsNotSetOnCreatingToken() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(1L);
        userPrincipal.setAuthProvider(AuthProvider.spotify);

        // then
        assertThatCode(() -> testSubject.createToken(userPrincipal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("'authorities' vom User muss gesetzt sein.");
    }

    @Test
    public void loginUser() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(1L);
        userPrincipal.setEmail("test@email.de");
        userPrincipal.setAuthProvider(AuthProvider.spotify);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        final OAuth2AuthenticationToken authenticationMock = mock(OAuth2AuthenticationToken.class);
        testSubject.setAuthentication(authenticationMock);

        // when
        testSubject.loginUser(userPrincipal);

        // then
        final ArgumentCaptor<OAuth2AuthorizedClient> oAuth2AuthorizedClientArgumentCaptor =
                ArgumentCaptor.forClass(OAuth2AuthorizedClient.class);
        verify(oAuth2AuthorizationClientServiceMock).saveAuthorizedClient(oAuth2AuthorizedClientArgumentCaptor.capture(),
                eq(authenticationMock));

        final OAuth2AuthorizedClient capturedAuthentication = oAuth2AuthorizedClientArgumentCaptor.getValue();
        assertThat(capturedAuthentication.getPrincipalName()).isEqualTo(userPrincipal.getUsername());
    }

    @Test
    public void throwsExceptionIfAuthenticationIsNotSetOnLoginUserMethod() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setEmail("test@email.de");
        userPrincipal.setAuthProvider(AuthProvider.spotify);

        // then
        assertThatCode(() -> testSubject.loginUser(userPrincipal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Zuerst 'createToken()' aufrufen, damit 'authentication' erzeugt wird.");
    }

    @Test
    public void throwsExceptionIfAuthProviderIsNotSetOnLoginUserMethod() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setEmail("test@email.de");

        testSubject.setAuthentication(mock(OAuth2AuthenticationToken.class));

        // then
        assertThatCode(() -> testSubject.loginUser(userPrincipal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("'AuthProvider' vom User muss gesetzt sein.");
    }

    @Test
    public void throwsExceptionIfEmailIsNotSetOnLoginUserMethod() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setAuthProvider(AuthProvider.spotify);

        testSubject.setAuthentication(mock(OAuth2AuthenticationToken.class));

        // then
        assertThatCode(() -> testSubject.loginUser(userPrincipal))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("'Email' vom User muss gesetzt sein.");
    }
}