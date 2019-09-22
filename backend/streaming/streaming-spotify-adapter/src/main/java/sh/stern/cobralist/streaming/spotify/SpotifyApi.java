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
import sh.stern.cobralist.streaming.spotify.valueobjects.*;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.AddTracksTracksToPlaylistRequest;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.CreatePlaylistRequest;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.RemoveTrack;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public CurrentPlaybackObject getCurrentPlayback(String url) throws AccessTokenExpiredException {
        LOG.info("Get current playback.");

        final ResponseEntity<CurrentPlaybackObject> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                CurrentPlaybackObject.class);

        checkStatusCode(responseEntity.getStatusCode());

        return responseEntity.getBody();
    }

    private void checkStatusCode(HttpStatus statusCode) throws AccessTokenExpiredException {
        if (statusCode == HttpStatus.UNAUTHORIZED) {
            throw new AccessTokenExpiredException();
        }
    }

    public void removeTrackFromPlaylist(String url, String trackId) throws AccessTokenExpiredException {
        LOG.info(String.format("Remove track with id '%s", trackId));

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        final RemoveTrack request = new RemoveTrack(Collections.singletonList(String.format("spotify:track:%s", trackId)));

        final ResponseEntity<String> responseEntitiy = restTemplate.exchange(url,
                HttpMethod.DELETE,
                new HttpEntity<>(request, httpHeaders),
                String.class);

        checkStatusCode(responseEntitiy.getStatusCode());
    }

    public SearchTrackValueObject searchTrack(String url) throws AccessTokenExpiredException {
        LOG.info(String.format("Search Tracks: %s", url));

        final ResponseEntity<SearchTrackValueObjectWrapper> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                SearchTrackValueObjectWrapper.class
        );

        checkStatusCode(response.getStatusCode());

        return Objects.requireNonNull(response.getBody()).getTracks();
    }
}
