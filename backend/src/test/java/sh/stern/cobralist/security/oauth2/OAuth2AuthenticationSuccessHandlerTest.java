package sh.stern.cobralist.security.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import sh.stern.cobralist.AppProperties;
import sh.stern.cobralist.security.TokenProvider;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static sh.stern.cobralist.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2AuthenticationSuccessHandlerTest {

    private OAuth2AuthenticationSuccessHandler testSubject;

    @Mock
    private AppProperties appProptertiesMock;
    @Mock
    private TokenProvider tokenProviderMock;
    @Mock
    private CookieService cookieServiceMock;
    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new OAuth2AuthenticationSuccessHandler(appProptertiesMock,
                tokenProviderMock,
                cookieServiceMock,
                httpCookieOAuth2AuthorizationRequestRepositoryMock);
    }

    @Test
    public void onAuthenticationSuccessful() throws IOException, ServletException {
        // given
        final String redirectUrl = "http://test.de";
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);
        final Authentication authenticationMock = mock(Authentication.class);

        final AppProperties.OAuth2 oAuth2Mock = mock(AppProperties.OAuth2.class);
        when(oAuth2Mock.getAuthorizedRedirectUris()).thenReturn(singletonList(redirectUrl));
        when(appProptertiesMock.getOauth2()).thenReturn(oAuth2Mock);

        final Cookie cookieMock = mock(Cookie.class);
        when(cookieMock.getValue()).thenReturn(redirectUrl);
        when(cookieServiceMock.getCookie(requestMock, REDIRECT_URI_PARAM_COOKIE_NAME)).thenReturn(Optional.of(cookieMock));

        // when
        testSubject.onAuthenticationSuccess(requestMock, responseMock, authenticationMock);

        // then
        verify(tokenProviderMock).createToken(authenticationMock);
        verify(httpCookieOAuth2AuthorizationRequestRepositoryMock).removeAuthorizationRequestCookies(requestMock,
                responseMock);
    }

    @Test
    public void clearAuthenticationAttributes() {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);

        when(requestMock.getSession(false)).thenReturn(mock(HttpSession.class));

        // when
        testSubject.clearAuthenticationAttributes(requestMock, responseMock);

        // then
        verify(httpCookieOAuth2AuthorizationRequestRepositoryMock).removeAuthorizationRequestCookies(requestMock,
                responseMock);
    }
}