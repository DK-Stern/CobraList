package sh.stern.cobralist.api.spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.api.ApiBinding;
import sh.stern.cobralist.api.exceptions.AccessTokenExpired;
import sh.stern.cobralist.api.spotify.errorhandler.SpotifyAccessTokenExpiredErrorHandler;
import sh.stern.cobralist.api.spotify.valueobjects.PagingObject;
import sh.stern.cobralist.api.spotify.valueobjects.SimplifiedPlaylistObject;

@Component
public class SpotifyApi extends ApiBinding {

    @Autowired
    public SpotifyApi(SpotifyAccessTokenExpiredErrorHandler spotifyAccessTokenExpiredErrorHandler, OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        super(spotifyAccessTokenExpiredErrorHandler, oAuth2AuthorizedClientService);
    }

    public PagingObject<SimplifiedPlaylistObject> getUserPlaylists(String url) {
        final ResponseEntity<PagingObject<SimplifiedPlaylistObject>> resultedResponse = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PagingObject<SimplifiedPlaylistObject>>() {
                        }
                );

        if (resultedResponse.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new AccessTokenExpired();
        }

        return resultedResponse.getBody();
    }
}
