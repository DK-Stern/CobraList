package sh.stern.cobralist.streaming.spotify.errorhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sh.stern.cobralist.streaming.api.AccessTokenExpiredErrorHandler;
import sh.stern.cobralist.streaming.spotify.valueobjects.UserTokenObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Base64;

@Component
public class SpotifyAccessTokenExpiredErrorHandler implements AccessTokenExpiredErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SpotifyAccessTokenExpiredErrorHandler.class);

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private OAuth2AuthorizedClient oAuth2AuthorizedClient;

    @Autowired
    public SpotifyAccessTokenExpiredErrorHandler(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @Override
    public void setAuthentication(OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        this.oAuth2AuthorizedClient = oAuth2AuthorizedClient;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() == HttpStatus.FORBIDDEN;
    }

    @Override
    public void handleError(ClientHttpResponse response) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final ClientRegistration clientRegistration = oAuth2AuthorizedClient.getClientRegistration();
        httpHeaders.add("Authorization", MessageFormat.format("Basic {0}", getAuthorization(clientRegistration)));

        final LinkedMultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("grant_type", "refresh_token");
        values.add("refresh_token", "AQDb2bjmie6R3HshOIqlf6H1QVnB1x7X-gHcA7aiYgJUTTXJK5GrEZrLqi-kEMqlQB0S0vKEDfl_R7GN9hzmtsNM8Otgwqu9697V0vVD3cgIJlKlAhqYOB-lf2kOD3XrZqV4Og");

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(values, httpHeaders);
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<UserTokenObject> responseEntity = restTemplate.exchange("https://accounts.spotify.com/api/token", HttpMethod.POST, request, UserTokenObject.class);
        final UserTokenObject responseEntityBody = responseEntity.getBody();

        if (responseEntityBody == null) {
            throw new IllegalStateException("Es konnte kein neuer Access Token von Spotify abgerufen werden.");
        }

        oAuth2AuthorizedClientService.saveAuthorizedClient(createNewOAuth2AuthorizedClient(responseEntityBody), authentication);
        LOG.info("Spotify Access Token erfolgreich erneuert.");
    }

    private OAuth2AuthorizedClient createNewOAuth2AuthorizedClient(UserTokenObject userToken) {
        final OAuth2AccessToken newOAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, userToken.getAccessToken(), Instant.now(), Instant.ofEpochSecond(userToken.getExpiresIn()));

        OAuth2RefreshToken oAuth2RefreshToken = null;
        if (userToken.getRefreshToken() != null) {
            oAuth2RefreshToken = new OAuth2RefreshToken(userToken.getRefreshToken(), Instant.now());
        }

        return oAuth2RefreshToken != null
                ? new OAuth2AuthorizedClient(oAuth2AuthorizedClient.getClientRegistration(), oAuth2AuthorizedClient.getPrincipalName(), newOAuth2AccessToken, oAuth2RefreshToken)
                : new OAuth2AuthorizedClient(oAuth2AuthorizedClient.getClientRegistration(), oAuth2AuthorizedClient.getPrincipalName(), newOAuth2AccessToken);
    }

    private String getAuthorization(ClientRegistration clientRegistration) {
        return Base64.getEncoder().encodeToString(String.format("%s:%s", clientRegistration.getClientId(), clientRegistration.getClientSecret()).getBytes());
    }
}
