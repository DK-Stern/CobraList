package sh.stern.cobralist.streaming.spotify.errorhandler;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.client.RestTemplate;
import sh.stern.cobralist.security.SecurityContextFacade;
import sh.stern.cobralist.streaming.spotify.RestTemplateFactory;
import sh.stern.cobralist.streaming.spotify.valueobjects.UserTokenObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings({"squid:S1192"}) // duplicated literals are allowed in unit tests
@RunWith(MockitoJUnitRunner.class)
public class AccessTokenExpiredErrorHandlerTest {

    private AccessTokenExpiredErrorHandler testSubject;

    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientServiceMock;

    @Mock
    private RestTemplateFactory restTemplateFactoryMock;

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private OAuth2AuthorizedClient oAuth2AuthorizedClientMock;

    @Mock
    private SecurityContextFacade securityContextFacadeMock;

    @Before
    public void setUp() {
        when(restTemplateFactoryMock.create()).thenReturn(restTemplateMock);
        testSubject = new AccessTokenExpiredErrorHandler(
                oAuth2AuthorizedClientServiceMock,
                securityContextFacadeMock,
                restTemplateFactoryMock);
    }

    @Test
    public void hasErrorIsTrueIfHttpStatusCodeIsUnauthorized() throws IOException {
        // given
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);
        when(responseMock.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);

        // then
        final boolean result = testSubject.hasError(responseMock);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void hasErrorIsFalseIfHttpStatusCodeIsNotUnauthorizedAndForbidden() throws IOException {
        // given
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);
        when(responseMock.getStatusCode()).thenReturn(HttpStatus.FORBIDDEN);

        // then
        final boolean result = testSubject.hasError(responseMock);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void hasErrorIsFalseIfHttpStatusCodeIsNotUnauthorizedAndOk() throws IOException {
        // given
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);
        when(responseMock.getStatusCode()).thenReturn(HttpStatus.OK);

        // then
        final boolean result = testSubject.hasError(responseMock);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void handleErrorThrowsExceptionIfNoRefreshTokenExists() {
        // given
        testSubject.setAuthentication(oAuth2AuthorizedClientMock);
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);

        // when u. then
        Assertions.assertThatIllegalStateException()
                .describedAs("Refresh Token muss vorhanden sein!")
                .isThrownBy(() -> testSubject.handleError(responseMock));
    }

    @Test
    public void handleErrorCreatesNewAuthorizedClientWithNewAccessToken() {
        // given
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);

        final SecurityContext securityContextMock = mock(SecurityContext.class);
        final Authentication authenticationMock = mock(Authentication.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(securityContextFacadeMock.getContext()).thenReturn(securityContextMock);

        final String refreshTokenValue = "refreshVal";
        final OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken(refreshTokenValue, Instant.now());
        when(oAuth2AuthorizedClientMock.getRefreshToken()).thenReturn(oAuth2RefreshToken);
        testSubject.setAuthentication(oAuth2AuthorizedClientMock);

        final ClientRegistration clientRegistrationMock = mock(ClientRegistration.class);
        final String clienId = "clienId";
        when(clientRegistrationMock.getClientId()).thenReturn(clienId);
        final String clientSecret = "clientSecret";
        when(clientRegistrationMock.getClientSecret()).thenReturn(clientSecret);
        final ClientRegistration.ProviderDetails providerDetailsMock = mock(ClientRegistration.ProviderDetails.class);
        final String tokenUrl = "tokenUrl";
        when(providerDetailsMock.getTokenUri()).thenReturn(tokenUrl);
        when(clientRegistrationMock.getProviderDetails()).thenReturn(providerDetailsMock);
        when(oAuth2AuthorizedClientMock.getClientRegistration()).thenReturn(clientRegistrationMock);

        final String principalName = "principalName";
        when(oAuth2AuthorizedClientMock.getPrincipalName()).thenReturn(principalName);

        final UserTokenObject userTokenObject = new UserTokenObject();
        final String expectedNewAccessToken = "newAccessToken";
        userTokenObject.setAccessToken(expectedNewAccessToken);
        final long expiresIn = LocalDateTime.now().plusHours(1L).toEpochSecond(ZoneOffset.UTC);
        userTokenObject.setExpiresIn(expiresIn);
        final String scope = "scope";
        userTokenObject.setScope(scope);
        final String tokenType = "Bearer";
        userTokenObject.setTokenType(tokenType);
        final ResponseEntity<UserTokenObject> responseEntity = new ResponseEntity<>(userTokenObject, HttpStatus.OK);
        when(restTemplateMock.exchange(eq(tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(UserTokenObject.class))).thenReturn(responseEntity);

        final ArgumentCaptor<OAuth2AuthorizedClient> oAuth2AuthorizedClientArgumentCaptor = ArgumentCaptor.forClass(OAuth2AuthorizedClient.class);

        // when
        testSubject.handleError(responseMock);

        // then
        verify(oAuth2AuthorizedClientServiceMock).saveAuthorizedClient(oAuth2AuthorizedClientArgumentCaptor.capture(), eq(authenticationMock));
        final OAuth2AuthorizedClient resultedNewClient = oAuth2AuthorizedClientArgumentCaptor.getValue();
        assertThat(resultedNewClient.getAccessToken().getTokenValue()).isEqualTo(expectedNewAccessToken);
    }

    @Test
    public void handleErrorCreatesNewAuthorizedClientWithNewRefreshToken() {
        // given
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);

        final SecurityContext securityContextMock = mock(SecurityContext.class);
        final Authentication authenticationMock = mock(Authentication.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(securityContextFacadeMock.getContext()).thenReturn(securityContextMock);

        final OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken("refreshVal", Instant.now());
        when(oAuth2AuthorizedClientMock.getRefreshToken()).thenReturn(oAuth2RefreshToken);
        testSubject.setAuthentication(oAuth2AuthorizedClientMock);

        final ClientRegistration clientRegistrationMock = mock(ClientRegistration.class);
        final String clienId = "clienId";
        when(clientRegistrationMock.getClientId()).thenReturn(clienId);
        final String clientSecret = "clientSecret";
        when(clientRegistrationMock.getClientSecret()).thenReturn(clientSecret);
        final ClientRegistration.ProviderDetails providerDetailsMock = mock(ClientRegistration.ProviderDetails.class);
        final String tokenUrl = "tokenUrl";
        when(providerDetailsMock.getTokenUri()).thenReturn(tokenUrl);
        when(clientRegistrationMock.getProviderDetails()).thenReturn(providerDetailsMock);
        when(oAuth2AuthorizedClientMock.getClientRegistration()).thenReturn(clientRegistrationMock);

        final String principalName = "principalName";
        when(oAuth2AuthorizedClientMock.getPrincipalName()).thenReturn(principalName);

        final UserTokenObject userTokenObject = new UserTokenObject();
        final String newAccessToken = "newAccessToken";
        userTokenObject.setAccessToken(newAccessToken);
        final String expectedNewRefreshToken = "newRefreshToken";
        userTokenObject.setRefreshToken(expectedNewRefreshToken);
        final long expiresIn = LocalDateTime.now().plusHours(1L).toEpochSecond(ZoneOffset.UTC);
        userTokenObject.setExpiresIn(expiresIn);
        final String scope = "scope";
        userTokenObject.setScope(scope);
        final String tokenType = "Bearer";
        userTokenObject.setTokenType(tokenType);
        final ResponseEntity<UserTokenObject> responseEntity = new ResponseEntity<>(userTokenObject, HttpStatus.OK);
        when(restTemplateMock.exchange(eq(tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(UserTokenObject.class))).thenReturn(responseEntity);

        final ArgumentCaptor<OAuth2AuthorizedClient> oAuth2AuthorizedClientArgumentCaptor = ArgumentCaptor.forClass(OAuth2AuthorizedClient.class);

        // when
        testSubject.handleError(responseMock);

        // then
        verify(oAuth2AuthorizedClientServiceMock).saveAuthorizedClient(oAuth2AuthorizedClientArgumentCaptor.capture(), eq(authenticationMock));
        final OAuth2AuthorizedClient resultedNewClient = oAuth2AuthorizedClientArgumentCaptor.getValue();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(resultedNewClient.getRefreshToken()).isNotNull();
        softly.assertThat(resultedNewClient.getRefreshToken().getTokenValue()).isEqualTo(expectedNewRefreshToken);
        softly.assertAll();
    }

    @Test
    public void handleErrorThrowsExceptionIfResponseIsEmpty() {
        // given
        final ClientHttpResponse responseMock = mock(ClientHttpResponse.class);

        final SecurityContext securityContextMock = mock(SecurityContext.class);
        final Authentication authenticationMock = mock(Authentication.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(securityContextFacadeMock.getContext()).thenReturn(securityContextMock);

        final OAuth2RefreshToken oAuth2RefreshToken = new OAuth2RefreshToken("refreshVal", Instant.now());
        when(oAuth2AuthorizedClientMock.getRefreshToken()).thenReturn(oAuth2RefreshToken);
        testSubject.setAuthentication(oAuth2AuthorizedClientMock);

        final ClientRegistration clientRegistrationMock = mock(ClientRegistration.class);
        final String clienId = "clienId";
        when(clientRegistrationMock.getClientId()).thenReturn(clienId);
        final String clientSecret = "clientSecret";
        when(clientRegistrationMock.getClientSecret()).thenReturn(clientSecret);
        final ClientRegistration.ProviderDetails providerDetailsMock = mock(ClientRegistration.ProviderDetails.class);
        final String tokenUrl = "tokenUrl";
        when(providerDetailsMock.getTokenUri()).thenReturn(tokenUrl);
        when(clientRegistrationMock.getProviderDetails()).thenReturn(providerDetailsMock);
        when(oAuth2AuthorizedClientMock.getClientRegistration()).thenReturn(clientRegistrationMock);

        @SuppressWarnings("squid:S4449") // response body shouldn't be null, but exception should be test
        final ResponseEntity<UserTokenObject> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplateMock.exchange(eq(tokenUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(UserTokenObject.class))).thenReturn(responseEntity);

        // when u. then
        assertThatIllegalStateException()
                .describedAs("Es konnte kein neuer Access Token von Spotify abgerufen werden.")
                .isThrownBy(() -> testSubject.handleError(responseMock));
    }
}