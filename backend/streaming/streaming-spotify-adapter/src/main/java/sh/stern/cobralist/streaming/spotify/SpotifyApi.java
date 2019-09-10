package sh.stern.cobralist.streaming.spotify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.errorhandler.AccessTokenExpiredErrorHandler;
import sh.stern.cobralist.streaming.spotify.valueobjects.PagingObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.PlaylistObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.SimplifiedPlaylistObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.TrackValueObjectWrapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.AddTracksTracksToPlaylistRequest;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.CreatePlaylistRequest;

import java.util.List;

@Component
public class SpotifyApi extends ApiBinding {

    private static final Logger LOG = LoggerFactory.getLogger(SpotifyApi.class);


    @Autowired
    public SpotifyApi(AccessTokenExpiredErrorHandler accessTokenExpiredErrorHandler,
                      OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                      RestTemplateFactory restTemplateFactory) {
        super(accessTokenExpiredErrorHandler, oAuth2AuthorizedClientService, restTemplateFactory.create());
    }

    public PagingObject<SimplifiedPlaylistObject> getUserPlaylists(String url) throws AccessTokenExpiredException {
        LOG.info(String.format("Getting user playlists: %s", url));
        final ResponseEntity<PagingObject<SimplifiedPlaylistObject>> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PagingObject<SimplifiedPlaylistObject>>() {
                        }
                );

        checkStatusCode(response.getStatusCode());

        return response.getBody();
    }

    public PlaylistObject createPlaylist(String url, String name, String description) throws AccessTokenExpiredException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        final CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest();
        createPlaylistRequest.setName(name);
        createPlaylistRequest.setDescription(description);
        createPlaylistRequest.setCollaborative(false);
        createPlaylistRequest.setPublic(false);

        LOG.info(String.format("Create user playlist: %s", url));
        final ResponseEntity<PlaylistObject> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(createPlaylistRequest, httpHeaders),
                PlaylistObject.class
        );

        checkStatusCode(response.getStatusCode());

        return response.getBody();
    }

    public PagingObject<TrackValueObjectWrapper> getTracksFromPlaylist(String url) throws AccessTokenExpiredException {
        LOG.info(String.format("Getting tracks from playlist: %s", url));
        final ResponseEntity<PagingObject<TrackValueObjectWrapper>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagingObject<TrackValueObjectWrapper>>() {
                });

        checkStatusCode(response.getStatusCode());

        return response.getBody();
    }

    public String postTracksToPlaylist(String url, List<String> trackUris) throws AccessTokenExpiredException {
        LOG.info(String.format("Add tracks to playlist: %s", url));

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        final ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(new AddTracksTracksToPlaylistRequest(trackUris), httpHeaders),
                String.class);

        checkStatusCode(response.getStatusCode());

        return response.getBody();
    }

    private void checkStatusCode(HttpStatus statusCode) throws AccessTokenExpiredException {
        if (statusCode == HttpStatus.UNAUTHORIZED) {
            throw new AccessTokenExpiredException();
        }
    }
}