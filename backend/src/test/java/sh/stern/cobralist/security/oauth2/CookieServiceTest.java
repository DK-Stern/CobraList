package sh.stern.cobralist.security.oauth2;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

public class CookieServiceTest {

    private CookieService testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new CookieService();
    }

    @Test
    public void getCookieFromRequest() {
        // given
        final String cookieName = "test_cookie";
        final String cookieValue = "cookie_value";
        final HttpServletRequest httpServletRequestMock =
                mock(HttpServletRequest.class);
        final Cookie expectedCookie = new Cookie(cookieName, cookieValue);
        final Cookie[] cookies = new Cookie[]{expectedCookie};

        when(httpServletRequestMock.getCookies()).thenReturn(cookies);

        // when
        Optional<Cookie> resultedCookieOptional =
                testSubject.getCookie(httpServletRequestMock, cookieName);

        // then
        assertThat(resultedCookieOptional.get()).isEqualTo(expectedCookie);
    }

    @Test
    public void getEmptyOptionalForRequestWithoutWantedCookieName() {
        // given
        final String cookieNameThatNotExists = "cookie_name_that_not_exists";
        final HttpServletRequest httpServletRequestMock =
                mock(HttpServletRequest.class);
        final Cookie cookie = new Cookie("random_cookie", "value");
        final Cookie[] cookies = {cookie};

        when(httpServletRequestMock.getCookies()).thenReturn(cookies);

        // when
        Optional<Cookie> resultedCookieOptional =
                testSubject.getCookie(httpServletRequestMock,
                        cookieNameThatNotExists);

        // then
        assertThat(resultedCookieOptional).isEmpty();
    }

    @Test
    public void getEmptyOptionalForRequestWithoutCookies() {
        // given
        final HttpServletRequest httpServletRequestMock =
                mock(HttpServletRequest.class);
        final String cookiesName = "cookies_name";

        // when
        Optional<Cookie> resultedCookieOptional =
                testSubject.getCookie(httpServletRequestMock, cookiesName);

        // then
        assertThat(resultedCookieOptional).isEmpty();
    }

    @Test
    public void addCookieToResponse() {
        // given
        final HttpServletResponse responseMock =
                mock(HttpServletResponse.class);
        final String expectedCookieName = "cookie_name_to_add";
        final String expectedCookieValue = "value";
        final int expectedMaxAge = 3200;

        // when
        testSubject.addCookie(responseMock, expectedCookieName,
                expectedCookieValue, expectedMaxAge);

        // then
        ArgumentCaptor<Cookie> cookieArgumentCaptor =
                ArgumentCaptor.forClass(Cookie.class);
        verify(responseMock).addCookie(cookieArgumentCaptor.capture());
        Cookie cookie = cookieArgumentCaptor.getValue();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(cookie.getName()).isEqualTo(expectedCookieName);
        softly.assertThat(cookie.getValue()).isEqualTo(expectedCookieValue);
        softly.assertThat(cookie.getMaxAge()).isEqualTo(expectedMaxAge);
        softly.assertThat(cookie.getPath()).isEqualTo("/");
        softly.assertThat(cookie.isHttpOnly()).isTrue();
        softly.assertAll();
    }

    @Test
    public void deleteCookie() {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock =
                mock(HttpServletResponse.class);
        final String cookieName = "cookie_name";
        final Cookie cookie = new Cookie(cookieName, "value");
        cookie.setPath("/path");
        cookie.setMaxAge(3600);
        final Cookie[] cookies = {cookie};

        when(requestMock.getCookies()).thenReturn(cookies);

        // when
        testSubject.deleteCookie(requestMock, responseMock, cookieName);

        // then
        ArgumentCaptor<Cookie> cookieArgumentCaptor =
                ArgumentCaptor.forClass(Cookie.class);
        verify(responseMock).addCookie(cookieArgumentCaptor.capture());
        Cookie resultedResettedCookie = cookieArgumentCaptor.getValue();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(resultedResettedCookie.getValue()).isBlank();
        softly.assertThat(resultedResettedCookie.getMaxAge()).isZero();
        softly.assertThat(resultedResettedCookie.getPath()).isEqualTo("/");
        softly.assertAll();
    }

    @Test
    public void doesNotThrowExceptionIfNoCookiesAreInRequest() {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock =
                mock(HttpServletResponse.class);
        final String cookieName = "cookie_name";

        when(requestMock.getCookies()).thenReturn(new Cookie[0]);

        // then
        assertThatCode(() -> testSubject.deleteCookie(requestMock,
                responseMock, cookieName))
                .doesNotThrowAnyException();
    }

    @Test
    public void serializeCookie() {
        // given
        final Cookie cookie = new Cookie("name", "value");
        final String expectedSerializedCookie =
                Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(cookie));

        // when
        final String resultedSerializesCookie = testSubject.serialize(cookie);

        // then
        assertThat(resultedSerializesCookie).isEqualTo(expectedSerializedCookie);
    }

    @Test
    public void deserializeFromCookieToOAuth2AuthorizationRequest() {
        // given
        final String expectedString = "value";
        final String serializedString =
                Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(expectedString));
        final Cookie cookie = new Cookie("name", serializedString);

        // when
        String resultedString = testSubject.deserialize(cookie,
                String.class);

        // then
        assertThat(resultedString).isEqualTo(expectedString);
    }
}