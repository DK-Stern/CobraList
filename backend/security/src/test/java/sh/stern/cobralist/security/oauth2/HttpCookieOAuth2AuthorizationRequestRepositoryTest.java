package sh.stern.cobralist.security.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static sh.stern.cobralist.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpCookieOAuth2AuthorizationRequestRepositoryTest {

    private HttpCookieOAuth2AuthorizationRequestRepository testSubject;

    @Mock
    private CookieService cookieServiceMock;

    @Before
    public void setUp() throws Exception {
        testSubject =
                new HttpCookieOAuth2AuthorizationRequestRepository(cookieServiceMock);
    }

    @Test
    public void loadAuthorizationRequestFromRequest() {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final Cookie cookie = new Cookie("cookie_name", "value");

        when(cookieServiceMock.getCookie(requestMock, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)).thenReturn(Optional.of(cookie));

        // when
        testSubject.loadAuthorizationRequest(requestMock);

        // then
        verify(cookieServiceMock).deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    @Test
    public void returnsNullForAuthorizationReguestIfNoCookieIsFound() {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);

        // when
        OAuth2AuthorizationRequest resultedAuthorizationRequest =
                testSubject.loadAuthorizationRequest(requestMock);

        // then
        assertThat(resultedAuthorizationRequest).isNull();
    }

    @Test
    public void saveAuthorizationRequestAsCookie() {
        // given
        final OAuth2AuthorizationRequest authorizationRequestMock =
                mock(OAuth2AuthorizationRequest.class);
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock =
                mock(HttpServletResponse.class);

        final String serializedAuthorizationRequest = "serialized_authorization_request";
        when(cookieServiceMock.serialize(authorizationRequestMock)).thenReturn(serializedAuthorizationRequest);

        // when
        testSubject.saveAuthorizationRequest(authorizationRequestMock,
                requestMock, responseMock);

        // then
        verify(cookieServiceMock).addCookie(responseMock, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                serializedAuthorizationRequest, COOKIE_EXPIRE_SECONDS);
    }

    @Test
    public void saveRedirectUriAsCookie() {
        // given
        final OAuth2AuthorizationRequest authorizationRequestMock = mock(OAuth2AuthorizationRequest.class);
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);

        final String redirectValue = "redirect_value";
        when(requestMock.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)).thenReturn(redirectValue);

        // when
        testSubject.saveAuthorizationRequest(authorizationRequestMock, requestMock,
                responseMock);

        // then
        verify(cookieServiceMock).addCookie(responseMock, REDIRECT_URI_PARAM_COOKIE_NAME, redirectValue,
                COOKIE_EXPIRE_SECONDS);
    }

    @Test
    public void resetCookiesInsteadOfSavingAuthorizationRequestIfAuthorizationRequestIsNull() {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);

        // when
        testSubject.saveAuthorizationRequest(null, requestMock, responseMock);

        // then
        verify(cookieServiceMock).deleteCookie(requestMock, responseMock, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        verify(cookieServiceMock).deleteCookie(requestMock, responseMock, REDIRECT_URI_PARAM_COOKIE_NAME);
        verify(cookieServiceMock, never()).addCookie(any(HttpServletResponse.class), anyString(), anyString(),
                anyInt());
    }

    @Test
    public void removeAuthorizationRequest() {
        // given
        final HttpCookieOAuth2AuthorizationRequestRepository testSubjectSpy = spy(testSubject);
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);

        // when
        testSubjectSpy.removeAuthorizationRequest(requestMock);

        // then
        verify(testSubjectSpy).loadAuthorizationRequest(requestMock);
    }
}