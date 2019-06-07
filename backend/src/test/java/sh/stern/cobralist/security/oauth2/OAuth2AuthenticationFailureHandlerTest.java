package sh.stern.cobralist.security.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static sh.stern.cobralist.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@RunWith(MockitoJUnitRunner.class)
public class OAuth2AuthenticationFailureHandlerTest {

    private OAuth2AuthenticationFailureHandler testSubject;

    @Mock
    private CookieService cookieServiceMock;

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepositoryMock;

    @Before
    public void setUp() throws Exception {
        testSubject = new OAuth2AuthenticationFailureHandler(cookieServiceMock,
                httpCookieOAuth2AuthorizationRequestRepositoryMock);
    }

    @Test
    public void onAuthenticationFailure() throws IOException, ServletException {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);
        final AuthenticationException authenticationExceptionMock = mock(AuthenticationException.class);

        final Cookie cookieMock = mock(Cookie.class);
        when(cookieMock.getValue()).thenReturn("/");
        when(cookieServiceMock.getCookie(requestMock, REDIRECT_URI_PARAM_COOKIE_NAME)).thenReturn(Optional.of(cookieMock));

        // when
        testSubject.onAuthenticationFailure(requestMock, responseMock,
                authenticationExceptionMock);

        // then
        verify(httpCookieOAuth2AuthorizationRequestRepositoryMock).removeAuthorizationRequestCookies(requestMock,
                responseMock);
    }
}