package sh.stern.cobralist.streaming.spotify;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.client.RestTemplate;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.errorhandler.AccessTokenExpiredErrorHandler;
import sh.stern.cobralist.streaming.spotify.valueobjects.*;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.AddTracksTracksToPlaylistRequest;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.CreatePlaylistRequest;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.RemoveTrack;
import sh.stern.cobralist.streaming.spotify.valueobjects.requests.Track;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpotifyApiTest {

    private SpotifyApi testSubject;

    @Mock
    private AccessTokenExpiredErrorHandler accesTokenExpiredErrorHandlerMock;

    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientServiceMock;

    @Mock
    private RestTemplateFactory restTemplateFactoryMock;

    @Mock
    private RestTemplate restTemplateMock;

    @Before
    public void setUp() {
        when(restTemplateFactoryMock.create()).thenReturn(restTemplateMock);

        testSubject = new SpotifyApi(
                accesTokenExpiredErrorHandlerMock,
                oAuth2AuthorizedClientServiceMock,
                restTemplateFactoryMock);
    }

    @Test
    public void getUserPlaylists() throws AccessTokenExpiredException {
        // given
        final String url = "url";
        final PagingObject<SimplifiedPlaylistObject> expectedPagingObject = new PagingObject<>();
        // noinspection unchecked
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity(expectedPagingObject, HttpStatus.OK));

        // when
        final PagingObject<SimplifiedPlaylistObject> resultedPagingObject = testSubject.getUserPlaylists(url);

        // then
        assertThat(resultedPagingObject).isEqualTo(expectedPagingObject);
    }

    @Test
    public void getUserPlaylistsThrowsExceptionOnResponseStatusUnauthorized() {
        // given
        final String url = "url";
        // noinspection unchecked
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.getUserPlaylists(url));
    }

    @Test
    public void createPlaylist() throws AccessTokenExpiredException {
        // given
        final String url = "url";
        final String name = "name";
        final String description = "description";

        final PlaylistObject expectedPlaylistObject = new PlaylistObject();
        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.POST), httpEntityArgumentCaptor.capture(), eq(PlaylistObject.class)))
                .thenReturn(new ResponseEntity<>(expectedPlaylistObject, HttpStatus.OK));

        // when
        final PlaylistObject resultedPlaylistObject = testSubject.createPlaylist(url, name, description);

        // then
        final HttpEntity httpEntityArgumentCaptorValue = httpEntityArgumentCaptor.getValue();
        final CreatePlaylistRequest createPlaylistRequest = (CreatePlaylistRequest) httpEntityArgumentCaptorValue.getBody();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(createPlaylistRequest).isNotNull();
        softly.assertThat(createPlaylistRequest.getDescription()).isEqualTo(description);
        softly.assertThat(createPlaylistRequest.getName()).isEqualTo(name);
        softly.assertThat(createPlaylistRequest.getCollaborative()).isFalse();
        softly.assertThat(createPlaylistRequest.getPublic()).isFalse();
        softly.assertThat(resultedPlaylistObject).isEqualTo(expectedPlaylistObject);
        softly.assertAll();
    }

    @Test
    public void createPlaylistThrowsExceptionOnResponseStatusUnauthorized() {
        // given
        final String url = "url";
        final String name = "name";
        final String description = "description";

        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(PlaylistObject.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.createPlaylist(url, name, description));
    }

    @Test
    public void getTracksFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String url = "url";

        final PagingObject<TrackValueObjectWrapper> expectedPagingObject = new PagingObject<>();
        // noinspection unchecked
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity(expectedPagingObject, HttpStatus.OK));

        // when
        final PagingObject<TrackValueObjectWrapper> resultedPagingObject = testSubject.getTracksFromPlaylist(url);

        // then
        assertThat(resultedPagingObject).isEqualTo(expectedPagingObject);
    }

    @Test
    public void getTracksFromPlaylistThrowsExceptionOnResponseStatusUnauthorized() {
        // given
        final String url = "url";

        // noinspection unchecked
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.getTracksFromPlaylist(url));
    }

    @Test
    public void postTracksToPlaylist() throws AccessTokenExpiredException {
        // given
        final String url = "url";
        final List<String> spotifyUris = Collections.singletonList("spotifyUri");

        final String expectedSnapshotId = "snapshotId";
        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.POST), httpEntityArgumentCaptor.capture(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(expectedSnapshotId, HttpStatus.OK));

        // when
        final String resultedSnapshotId = testSubject.postTracksToPlaylist(url, spotifyUris);

        // then
        final HttpEntity httpEntityArgumentCaptorValue = httpEntityArgumentCaptor.getValue();
        final AddTracksTracksToPlaylistRequest addTracksTracksToPlaylistRequest = (AddTracksTracksToPlaylistRequest) httpEntityArgumentCaptorValue.getBody();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(addTracksTracksToPlaylistRequest).isNotNull();
        softly.assertThat(addTracksTracksToPlaylistRequest.getUris()).isEqualTo(spotifyUris);
        softly.assertThat(resultedSnapshotId).isEqualTo(expectedSnapshotId);
        softly.assertAll();
    }

    @Test
    public void postTracksToPlaylistThrowsExceptionOnResponseStatusUnauthorized() {
        // given
        final String url = "url";
        final List<String> spotifyUris = Collections.singletonList("spotifyUri");

        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.postTracksToPlaylist(url, spotifyUris));
    }

    @Test
    public void getCurrentPlayback() throws AccessTokenExpiredException {
        // given
        final String url = "url";

        final CurrentPlaybackObject expectedCurrentPlaybackObject = new CurrentPlaybackObject();
        when(restTemplateMock.exchange(url, HttpMethod.GET, null, CurrentPlaybackObject.class))
                .thenReturn(new ResponseEntity<>(expectedCurrentPlaybackObject, HttpStatus.OK));

        // when
        final CurrentPlaybackObject resultedCurrentPlayback = testSubject.getCurrentPlayback(url);

        // then
        assertThat(resultedCurrentPlayback).isEqualTo(expectedCurrentPlaybackObject);
    }

    @Test
    public void getCurrentPlaybackThrowsExceptionOnResponseStatusUnauthorized() throws AccessTokenExpiredException {
        // given
        final String url = "url";

        when(restTemplateMock.exchange(url, HttpMethod.GET, null, CurrentPlaybackObject.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.getCurrentPlayback(url));
    }

    @Test
    public void removeTrackFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String url = "url";
        final String trackId = "trackId";

        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.DELETE), httpEntityArgumentCaptor.capture(), eq(String.class))).thenReturn(ResponseEntity.ok("snapshotId"));

        // when
        testSubject.removeTrackFromPlaylist(url, trackId);

        // then
        final HttpEntity httpEntity = httpEntityArgumentCaptor.getValue();
        final RemoveTrack resultedRequest = (RemoveTrack) httpEntity.getBody();
        assertThat(Objects.requireNonNull(resultedRequest).getTracks())
                .extracting(Track::getUri).containsExactly("spotify:track:" + trackId);
    }

    @Test
    public void removeTrackFromPlaylistThrowsExceptionIfStatusCodeUnauthorized() throws AccessTokenExpiredException {
        // given
        final String url = "url";
        final String trackId = "trackId";

        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplateMock.exchange(eq(url), eq(HttpMethod.DELETE), httpEntityArgumentCaptor.capture(), eq(String.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.removeTrackFromPlaylist(url, trackId));
    }

    @Test
    public void searchTrack() throws AccessTokenExpiredException {
        // given
        final String url = "https://api.spotify.com/v1/search?q=abba&type=track";

        final SearchTrackValueObjectWrapper searchTrackValueObjectWrapper = new SearchTrackValueObjectWrapper();
        final SearchTrackValueObject tracks = new SearchTrackValueObject();
        searchTrackValueObjectWrapper.setTracks(tracks);
        when(restTemplateMock.exchange(url, HttpMethod.GET, null, SearchTrackValueObjectWrapper.class)).thenReturn(new ResponseEntity<>(searchTrackValueObjectWrapper, HttpStatus.OK));

        // when
        final SearchTrackValueObject resultedResponse = testSubject.searchTrack(url);

        // then
        assertThat(resultedResponse).isEqualTo(tracks);
    }

    @Test
    public void searchTrackThrowsExceptionIfStatusCodeUnauthorized() throws AccessTokenExpiredException {
        // given
        final String url = "https://api.spotify.com/v1/search?q=abba&type=track";

        when(restTemplateMock.exchange(url, HttpMethod.GET, null, SearchTrackValueObjectWrapper.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));

        // when u. then
        assertThatExceptionOfType(AccessTokenExpiredException.class)
                .isThrownBy(() -> testSubject.searchTrack(url));
    }
}