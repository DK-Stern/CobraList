package sh.stern.cobralist.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.security.TokenProvider;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;

import java.time.Instant;

@Component
public class OAuth2Helper {

    private final TokenProvider tokenProvider;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    public OAuth2Helper(TokenProvider tokenProvider, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.tokenProvider = tokenProvider;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    private OAuth2AuthenticationToken authentication;

    public String createToken(UserPrincipal userPrincipal) {
        if (userPrincipal.getAuthProvider() == null) {
            throw new IllegalStateException("'AuthProvider' vom User muss gesetzt sein.");
        } else if (userPrincipal.getAuthorities() == null) {
            throw new IllegalStateException("'authorities' vom User muss gesetzt sein.");
        } else if (userPrincipal.getId() == null) {
            throw new IllegalStateException("'id' vom User muss gesetzt sein.");
        }

        authentication = new OAuth2AuthenticationToken(userPrincipal,
                userPrincipal.getAuthorities(),
                userPrincipal.getAuthProvider().name());
        return tokenProvider.createToken(authentication);
    }

    public void loginUser(UserPrincipal userPrincipal) {
        if (authentication == null) {
            throw new IllegalStateException("Zuerst 'createToken()' aufrufen, damit 'authentication' erzeugt wird.");
        } else if (userPrincipal.getAuthProvider() == null) {
            throw new IllegalStateException("'AuthProvider' vom User muss gesetzt sein.");
        } else if (userPrincipal.getUsername() == null) {
            throw new IllegalStateException("'Email' vom User muss gesetzt sein.");
        }

        ClientRegistration clientRegistration =
                ClientRegistration.withRegistrationId(userPrincipal.getAuthProvider().name())
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUriTemplate("/redirect/path")
                        .authorizationUri("/authorization/uri")
                        .tokenUri("/token/uri")
                        .clientId("1234567890")
                        .build();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "123",
                Instant.now(),
                Instant.now().plusSeconds(100000));
        OAuth2AuthorizedClient oAuth2AuthorizedClient =
                new OAuth2AuthorizedClient(clientRegistration, userPrincipal.getUsername(), accessToken);

        oAuth2AuthorizedClientService.saveAuthorizedClient(oAuth2AuthorizedClient, authentication);
    }

    void setAuthentication(OAuth2AuthenticationToken authentication) {
        this.authentication = authentication;
    }
}
