package sh.stern.cobralist.streaming.spotify.errorhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import sh.stern.cobralist.security.SecurityContextFacade;
import sh.stern.cobralist.streaming.spotify.RestTemplateFactory;
import sh.stern.cobralist.streaming.spotify.valueobjects.UserTokenObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

@Component
public class AccessTokenExpiredErrorHandler implements ResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenExpiredErrorHandler.class);

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final SecurityContextFacade securityContextFacade;
    private final RestTemplate restTemplate;
    private OAuth2AuthorizedClient oAuth2AuthorizedClient;

    @Autowired
    public AccessTokenExpiredErrorHandler(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                                          SecurityContextFacade securityContextFacade,
                                          RestTemplateFactory restTemplateFactory) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.securityContextFacade = securityContextFacade;
        this.restTemplate = restTemplateFactory.create();
    }

    public void setAuthentication(OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        this.oAuth2AuthorizedClient = oAuth2AuthorizedClient;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Override
    public void handleError(ClientHttpResponse response) {
        if (Objects.isNull(oAuth2AuthorizedClient.getRefreshToken())) {
            throw new IllegalStateException("Refresh Token muss vorhanden sein!");
        }

        final Authentication authentication = securityContextFacade.getContext().getAuthentication();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        final ClientRegistration clientRegistration = oAuth2AuthorizedClient.getClientRegistration();
        httpHeaders.add("Authorization", MessageFormat.format("Basic {0}", getAuthorization(clientRegistration)));

        final LinkedMultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("grant_type", "refresh_token");
        values.add("refresh_token", oAuth2AuthorizedClient.getRefreshToken().getTokenValue());

        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(values, httpHeaders);

        final ResponseEntity<UserTokenObject> responseEntity = restTemplate.exchange(
                clientRegistration.getProviderDetails().getTokenUri(),
                HttpMethod.POST,
                request,
                UserTokenObject.class);
        final UserTokenObject responseEntityBody = responseEntity.getBody();

        if (responseEntityBody == null) {
            throw new IllegalStateException("Es konnte kein neuer Access Token von Spotify abgerufen werden.");
        }

        oAuth2AuthorizedClientService.saveAuthorizedClient(createNewOAuth2AuthorizedClient(responseEntityBody), authentication);
        LOG.info("Spotify Access Token erfolgreich erneuert.");
    }

    private OAuth2AuthorizedClient createNewOAuth2AuthorizedClient(UserTokenObject userToken) {
        final Instant now = Instant.now();
        final OAuth2AccessToken newOAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                userToken.getAccessToken(),
                now,
                now.plusSeconds(userToken.getExpiresIn()));

        return new OAuth2AuthorizedClient(oAuth2AuthorizedClient.getClientRegistration(),
                oAuth2AuthorizedClient.getPrincipalName(),
                newOAuth2AccessToken,
                userToken.getRefreshToken() != null
                        ? new OAuth2RefreshToken(userToken.getRefreshToken(), now)
                        : oAuth2AuthorizedClient.getRefreshToken());
    }

    private String getAuthorization(ClientRegistration clientRegistration) {
        return Base64.getEncoder().encodeToString(String.format("%s:%s", clientRegistration.getClientId(), clientRegistration.getClientSecret()).getBytes());
    }
}
