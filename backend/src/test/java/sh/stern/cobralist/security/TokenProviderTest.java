package sh.stern.cobralist.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import sh.stern.cobralist.AppProperties;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenProviderTest {

    private static final String SECRET =
            "superSecretSecret1234567890superSecretSecret1234567890superSecretSecret1234567890superSecretSecret1234567890superSecretSecret1234567890";
    public static final String TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0" +
            ".T1IRD_rEu6fHDDeatGZq8Xb1HnHoqvwX7ki1_UPIydufZHu8jloXCQ-l5dAn8aMSLUzJ5qGNb7O2R51EWjsWYw";

    private TokenProvider testSubject;

    @Mock
    private AppProperties appPropertiesMock;

    @Mock
    private AppProperties.Auth authMock;


    @Before
    public void setUp() {
        when(authMock.getTokenExpirationMsec()).thenReturn(3L);
        when(authMock.getTokenSecret()).thenReturn(SECRET);
        when(appPropertiesMock.getAuth()).thenReturn(authMock);

        testSubject = new TokenProvider(appPropertiesMock);
    }

    @Test
    public void createToken() {
        // given
        final Authentication authenticationMock = mock(Authentication.class);

        final UserPrincipal userPrincipalMock = mock(UserPrincipal.class);
        when(authenticationMock.getPrincipal()).thenReturn(userPrincipalMock);

        // when
        testSubject.createToken(authenticationMock);

        // then
        verify(userPrincipalMock).getId();
        verify(authMock).getTokenExpirationMsec();
        verify(authMock).getTokenSecret();
    }

    @Test
    public void getUserIdFromToken() {
        // given
        final Long expectedId = 1234567890L;

        // when
        final Long resultedUserIdFromToken = testSubject.getUserIdFromToken(TOKEN);

        // then
        assertThat(resultedUserIdFromToken).isEqualTo(expectedId);
    }

    @Test
    public void validateTokenSucessfully() {
        // when
        boolean resultedValidation = testSubject.validateToken(TOKEN);

        // then
        assertThat(resultedValidation).isTrue();
    }

    @Test
    public void validateEmptyToken() {
        // when
        boolean resultedValidation = testSubject.validateToken("");

        // then
        assertThat(resultedValidation).isFalse();
    }

    @Test
    public void validateTokenWithInvalidSignature() {
        // when
        boolean resultedValidation = testSubject.validateToken("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIn0" +
                ".Mh66AtsbkR0jM4jrwhj7_KFnGz_hBOpJVRFFiaBlk9bA0m8fB2nrCf5ggS8za9uW1ANLPlPRp2cBDupDUi0i4w");

        // then
        assertThat(resultedValidation).isFalse();
    }

    @Test
    public void validateInvalidToken() {
        // when
        boolean resultedValidation = testSubject.validateToken("abc");

        // then
        assertThat(resultedValidation).isFalse();
    }

    @Test
    public void validateExpiredToken() {
        // when
        boolean resultedValidation = testSubject.validateToken("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIiwiZXhwIjoiMSJ9" +
                ".MnbKvRb4Zn05fQlWXVd6B6scw-pRiQTMg7yJBgEYOr0EWEyxVbWNE9IHBPb0lb87xvIdSzUxjygI-Sdhwk-cDg");

        // then
        assertThat(resultedValidation).isFalse();
    }

    @Test
    public void validateTokenWithWrongAlgorithm() {
        // when
        boolean resultedValidation = testSubject.validateToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJzdWIiOiIxMjM0NTY3ODkwIn0" +
                ".DR98G-xK5wMloBXteqRGJb0t34zxswlf4x2QFFBKHYLftY1UFPFLlT5jQLNOcqWL1rJg3tuOZIg1TezRT-oTO" +
                "-ZLgvuuDOHXMTQ08lp3mAiYw9UcrdtgBjgquqTekrV9lAb9Q0Mz5OzByLEPaTJSmKEgO0gkDCCMrfHn6uIoXUteET599AlD2w7pk0UPN43_0yAhXKpnO8UEtPyEIu07aqjLzbHf_rsHW1T_yV2fA-7d8K26i5ejBqZGUrZ-agxABHjN4b0WtKtFsqnS6YrSZpvOAFR2e9pOWBYFtLHgh-KZN0IhHeLA94jFCJWAGFgl5Xuv5fJp1Sz_9NIyWxPzOQ");

        // then
        assertThat(resultedValidation).isFalse();
    }
}