package sh.stern.cobralist.streaming.spotify;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.client.RestTemplate;
import sh.stern.cobralist.streaming.api.AccessTokenExpiredErrorHandler;
import sh.stern.cobralist.user.domain.StreamingProvider;

public abstract class ApiBinding {
    private final AccessTokenExpiredErrorHandler accessTokenExpiredErrorHandler;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    protected RestTemplate restTemplate;

    public ApiBinding(AccessTokenExpiredErrorHandler accessTokenExpiredErrorHandler, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.accessTokenExpiredErrorHandler = accessTokenExpiredErrorHandler;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    public void setAuthentication(StreamingProvider streamingProvider, String userName) {
        OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(streamingProvider.name(), userName);

        this.restTemplate = new RestTemplate();
        if (oAuth2AuthorizedClient != null) {
            accessTokenExpiredErrorHandler.setAuthentication(oAuth2AuthorizedClient);
            this.restTemplate.getInterceptors()
                    .add(getBearerTokenInterceptor(oAuth2AuthorizedClient.getAccessToken().getTokenValue()));
            this.restTemplate.setErrorHandler(accessTokenExpiredErrorHandler);
        } else {
            this.restTemplate.getInterceptors().add(getNoTokenInterceptor());
        }
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        return (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, body);
        };
    }

    private ClientHttpRequestInterceptor getNoTokenInterceptor() {
        throw new IllegalStateException("Can't access spotify api without access token.");
    }
}
