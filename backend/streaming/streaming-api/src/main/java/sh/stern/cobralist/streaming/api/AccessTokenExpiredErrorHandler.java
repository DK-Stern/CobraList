package sh.stern.cobralist.streaming.api;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.ResponseErrorHandler;

public interface AccessTokenExpiredErrorHandler extends ResponseErrorHandler {
    void setAuthentication(OAuth2AuthorizedClient oAuth2AuthorizedClient);
}
