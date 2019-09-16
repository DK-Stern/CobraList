package sh.stern.cobralist.streaming.spotify.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.domain.SimplePlaylistDTO;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.streaming.spotify.mapper.TrackValueObjectToTrackDTOMapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.*;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpotifyPlaylistPublicApiServiceTest {

    private SpotifyPlaylistPublicApiService testSubject;

    @Mock
    private SpotifyApi spotifyApiMock;

    @Mock
    private TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapperMock;

    @Before
    public void setUp() {
        testSubject = new SpotifyPlaylistPublicApiService(spotifyApiMock, trackValueObjectToTrackDTOMapperMock);
    }

    @Test
    public void getUsersPlaylistsWithoutFetchingNextPlaylists() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";

        final PagingObject<SimplifiedPlaylistObject> pagingObject = new PagingObject<>();
        final SimplifiedPlaylistObject simplifiedPlaylistObject = new SimplifiedPlaylistObject();
        final String playlistId = "123";
        simplifiedPlaylistObject.setId(playlistId);
        final String playlistName = "PlaylistName";
        simplifiedPlaylistObject.setName(playlistName);
        pagingObject.setItems(Collections.singletonList(simplifiedPlaylistObject));
        when(spotifyApiMock.getUserPlaylists("https://api.spotify.com/v1/me/playlists")).thenReturn(pagingObject);

        // when
        final List<SimplePlaylistDTO> resultedUsersPlaylists = testSubject.getUsersPlaylists(userName);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedUsersPlaylists).extracting(SimplePlaylistDTO::getPlaylistId).containsExactly(playlistId);
    }


    @Test
    public void retryGetUsersPlaylistsWithoutFetchingNextPlaylistsIfAccessTokenExpiredExceptionIsThrown() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";

        final PagingObject<SimplifiedPlaylistObject> pagingObject = new PagingObject<>();
        final SimplifiedPlaylistObject simplifiedPlaylistObject = new SimplifiedPlaylistObject();
        final String playlistId = "123";
        simplifiedPlaylistObject.setId(playlistId);
        final String playlistName = "PlaylistName";
        simplifiedPlaylistObject.setName(playlistName);
        pagingObject.setItems(Collections.singletonList(simplifiedPlaylistObject));
        when(spotifyApiMock.getUserPlaylists("https://api.spotify.com/v1/me/playlists"))
                .thenThrow(new AccessTokenExpiredException())
                .thenReturn(pagingObject);

        // when
        final List<SimplePlaylistDTO> resultedUsersPlaylists = testSubject.getUsersPlaylists(userName);

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedUsersPlaylists).extracting(SimplePlaylistDTO::getPlaylistId).containsExactly(playlistId);
    }

    @Test
    public void getUsersPlaylistsAndFetchingNextPlaylists() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";

        final PagingObject<SimplifiedPlaylistObject> pagingObject = new PagingObject<>();
        final String fetchUrl = "url";
        pagingObject.setNext(fetchUrl);

        final SimplifiedPlaylistObject simplifiedPlaylistObject = new SimplifiedPlaylistObject();
        final String playlistId = "123";
        simplifiedPlaylistObject.setId(playlistId);
        final String playlistName = "PlaylistName";
        simplifiedPlaylistObject.setName(playlistName);
        final ArrayList<SimplifiedPlaylistObject> items = new ArrayList<>();
        items.add(simplifiedPlaylistObject);
        pagingObject.setItems(items);
        when(spotifyApiMock.getUserPlaylists("https://api.spotify.com/v1/me/playlists")).thenReturn(pagingObject);

        final PagingObject<SimplifiedPlaylistObject> fetchedPagingObject = new PagingObject<>();
        final SimplifiedPlaylistObject fetchedSimplifiedPlaylistObject = new SimplifiedPlaylistObject();
        final String fetchedPlaylistId = "456";
        fetchedSimplifiedPlaylistObject.setId(fetchedPlaylistId);
        final String fetchedPlaylistName = "fetched playlist name";
        fetchedSimplifiedPlaylistObject.setName(fetchedPlaylistName);
        fetchedPagingObject.setItems(Collections.singletonList(fetchedSimplifiedPlaylistObject));
        when(spotifyApiMock.getUserPlaylists(fetchUrl)).thenReturn(fetchedPagingObject);

        // when
        final List<SimplePlaylistDTO> resultedUsersPlaylists = testSubject.getUsersPlaylists(userName);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedUsersPlaylists).extracting(SimplePlaylistDTO::getPlaylistId).containsExactly(playlistId, fetchedPlaylistId);
    }

    @Test
    public void retryGetUsersPlaylistsAndFetchingNextPlaylistsIfAccessTokenExpiredExceptionIsThrown() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";

        final PagingObject<SimplifiedPlaylistObject> pagingObject = new PagingObject<>();
        final String fetchUrl = "url";
        pagingObject.setNext(fetchUrl);

        final SimplifiedPlaylistObject simplifiedPlaylistObject = new SimplifiedPlaylistObject();
        final String playlistId = "123";
        simplifiedPlaylistObject.setId(playlistId);
        final String playlistName = "PlaylistName";
        simplifiedPlaylistObject.setName(playlistName);
        final ArrayList<SimplifiedPlaylistObject> items = new ArrayList<>();
        items.add(simplifiedPlaylistObject);
        pagingObject.setItems(items);
        when(spotifyApiMock.getUserPlaylists("https://api.spotify.com/v1/me/playlists"))
                .thenThrow(new AccessTokenExpiredException())
                .thenReturn(pagingObject);

        final PagingObject<SimplifiedPlaylistObject> fetchedPagingObject = new PagingObject<>();
        final SimplifiedPlaylistObject fetchedSimplifiedPlaylistObject = new SimplifiedPlaylistObject();
        final String fetchedPlaylistId = "456";
        fetchedSimplifiedPlaylistObject.setId(fetchedPlaylistId);
        final String fetchedPlaylistName = "fetched playlist name";
        fetchedSimplifiedPlaylistObject.setName(fetchedPlaylistName);
        fetchedPagingObject.setItems(Collections.singletonList(fetchedSimplifiedPlaylistObject));
        when(spotifyApiMock.getUserPlaylists(fetchUrl)).thenReturn(fetchedPagingObject);

        // when
        final List<SimplePlaylistDTO> resultedUsersPlaylists = testSubject.getUsersPlaylists(userName);

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedUsersPlaylists).extracting(SimplePlaylistDTO::getPlaylistId).containsExactly(playlistId, fetchedPlaylistId);
    }

    @Test
    public void createPlaylistAndGetId() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";
        final String userProviderId = "userNameProviderId";
        final String playlistName = "PlaylistName";
        final String description = "Description";

        final PlaylistObject playlistObject = new PlaylistObject();
        final String expectedPlaylistId = "id";
        playlistObject.setId(expectedPlaylistId);

        when(spotifyApiMock.createPlaylist("https://api.spotify.com/v1/users/" + userProviderId + "/playlists", playlistName, description)).thenReturn(playlistObject);

        // when
        final PlaylistDTO resultedPlaylistDTO = testSubject.createPlaylist(userName, userProviderId, playlistName, description);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedPlaylistDTO.getPlaylistId()).isEqualTo(expectedPlaylistId);
    }

    @Test
    public void createPlaylistAndGetPlaylistName() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";
        final String userProviderId = "userNameProviderId";
        final String expectedPlaylistName = "PlaylistName";
        final String description = "Description";

        final PlaylistObject playlistObject = new PlaylistObject();
        playlistObject.setName(expectedPlaylistName);

        when(spotifyApiMock.createPlaylist("https://api.spotify.com/v1/users/userNameProviderId/playlists", expectedPlaylistName, description)).thenReturn(playlistObject);

        // when
        final PlaylistDTO resultedPlaylistDTO = testSubject.createPlaylist(userName, userProviderId, expectedPlaylistName, description);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedPlaylistDTO.getName()).isEqualTo(expectedPlaylistName);
    }

    @Test
    public void retryCreatingPlaylistAfterAccessTokenExpiredException() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";
        final String userProviderId = "userNameProviderId";
        final String expectedPlaylistName = "PlaylistName";
        final String description = "Description";

        final PlaylistObject playlistObject = new PlaylistObject();
        final String expectedPlaylistId = "playlistId";
        playlistObject.setId(expectedPlaylistId);

        when(spotifyApiMock.createPlaylist("https://api.spotify.com/v1/users/" + userProviderId + "/playlists", expectedPlaylistName, description))
                .thenThrow(new AccessTokenExpiredException())
                .thenReturn(playlistObject);

        // when
        final PlaylistDTO resultedPlaylistDTO = testSubject.createPlaylist(userName, userProviderId, expectedPlaylistName, description);

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedPlaylistDTO.getPlaylistId()).isEqualTo(expectedPlaylistId);
    }

    @Test
    public void verifyAuthenticationIsSetForGettingTracksFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String playlistId = "playlistId";
        final String userName = "username";

        final String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total";
        when(spotifyApiMock.getTracksFromPlaylist(url)).thenReturn(new PagingObject<>());

        // when
        testSubject.getTracksFromPlaylist(userName, playlistId);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, userName);
    }

    @Test
    public void getTracksFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String playlistId = "playlistId";
        final String userName = "username";

        final String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total";
        final PagingObject<TrackValueObjectWrapper> pagingObject = new PagingObject<>();
        final TrackValueObjectWrapper trackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject track = new TrackValueObject();
        track.setLocal(false);
        trackValueObjectWrapper.setTrack(track);
        pagingObject.setItems(Collections.singletonList(trackValueObjectWrapper));
        when(spotifyApiMock.getTracksFromPlaylist(url)).thenReturn(pagingObject);

        final TrackDTO expectedTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(track)).thenReturn(expectedTrackDTO);

        // when
        final List<TrackDTO> resultedTracks = testSubject.getTracksFromPlaylist(userName, playlistId);

        // then
        assertThat(resultedTracks).containsExactly(expectedTrackDTO);
    }

    @Test
    public void filterLocalTracksOnGettingTracksFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String playlistId = "playlistId";
        final String userName = "username";

        final String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total";
        final PagingObject<TrackValueObjectWrapper> pagingObject = new PagingObject<>();

        final TrackValueObjectWrapper trackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject track = new TrackValueObject();
        track.setLocal(false);
        trackValueObjectWrapper.setTrack(track);

        final TrackValueObjectWrapper localTrackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject localTrack = new TrackValueObject();
        localTrack.setLocal(true);
        localTrackValueObjectWrapper.setTrack(localTrack);

        pagingObject.setItems(Arrays.asList(trackValueObjectWrapper, localTrackValueObjectWrapper));
        when(spotifyApiMock.getTracksFromPlaylist(url)).thenReturn(pagingObject);

        final TrackDTO expectedTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(track)).thenReturn(expectedTrackDTO);

        // when
        final List<TrackDTO> resultedTracks = testSubject.getTracksFromPlaylist(userName, playlistId);

        // then
        verify(trackValueObjectToTrackDTOMapperMock, never()).map(localTrack);
        assertThat(resultedTracks).containsExactly(expectedTrackDTO);
    }

    @Test
    public void fetchingNextTracksWhenPagingObjectHasNextOnGettingTracksFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String playlistId = "playlistId";
        final String userName = "username";

        final String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total";
        final PagingObject<TrackValueObjectWrapper> firstPagingObject = new PagingObject<>();
        final String urlForNextTracks = "urlForNextTracks";
        firstPagingObject.setNext(urlForNextTracks);
        final TrackValueObjectWrapper firstTrackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject firstTrack = new TrackValueObject();
        firstTrack.setLocal(false);
        firstTrackValueObjectWrapper.setTrack(firstTrack);
        firstPagingObject.setItems(Collections.singletonList(firstTrackValueObjectWrapper));

        final PagingObject<TrackValueObjectWrapper> secondPagingObject = new PagingObject<>();
        final TrackValueObjectWrapper secondTrackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject secondTrack = new TrackValueObject();
        secondTrack.setLocal(false);
        secondTrackValueObjectWrapper.setTrack(secondTrack);
        secondPagingObject.setItems(Collections.singletonList(secondTrackValueObjectWrapper));

        when(spotifyApiMock.getTracksFromPlaylist(url)).thenReturn(firstPagingObject);
        when(spotifyApiMock.getTracksFromPlaylist(urlForNextTracks)).thenReturn(secondPagingObject);

        final TrackDTO firstTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(firstTrack)).thenReturn(firstTrackDTO);
        final TrackDTO secondTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(secondTrack)).thenReturn(secondTrackDTO);

        // when
        final List<TrackDTO> resultedTracks = testSubject.getTracksFromPlaylist(userName, playlistId);

        // then
        assertThat(resultedTracks).containsExactly(firstTrackDTO, secondTrackDTO);
    }

    @Test
    public void retryGettingPlaylistTracksAfterAccessTokenExpiredExceptionWIsThrown() throws AccessTokenExpiredException {
        // given
        final String playlistId = "playlistId";
        final String userName = "username";

        final String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total";
        final PagingObject<TrackValueObjectWrapper> pagingObject = new PagingObject<>();
        final TrackValueObjectWrapper trackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject track = new TrackValueObject();
        track.setLocal(false);
        trackValueObjectWrapper.setTrack(track);
        pagingObject.setItems(Collections.singletonList(trackValueObjectWrapper));
        when(spotifyApiMock.getTracksFromPlaylist(url)).thenThrow(new AccessTokenExpiredException()).thenReturn(pagingObject);

        final TrackDTO expectedTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(track)).thenReturn(expectedTrackDTO);

        // when
        final List<TrackDTO> resultedTracks = testSubject.getTracksFromPlaylist(userName, playlistId);

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedTracks).containsExactly(expectedTrackDTO);
    }

    @Test
    public void retryGettingTracksFromPlaylistWithFetchingNextTracksAfterAccessTokenExpiredExceptionIsThrown() throws AccessTokenExpiredException {
        // given
        final String playlistId = "playlistId";
        final String userName = "username";

        final String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks?fields=items(track(id,is_local,duration_ms,uri,name,artists,album(name,images))),next,href,previous,limit,offset,total";

        final PagingObject<TrackValueObjectWrapper> firstPagingObject = new PagingObject<>();
        final String urlForNextTracks = "urlForNextTracks";
        firstPagingObject.setNext(urlForNextTracks);
        final TrackValueObjectWrapper firstTrackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject firstTrack = new TrackValueObject();
        firstTrack.setLocal(false);
        firstTrackValueObjectWrapper.setTrack(firstTrack);
        firstPagingObject.setItems(Collections.singletonList(firstTrackValueObjectWrapper));

        final PagingObject<TrackValueObjectWrapper> secondPagingObject = new PagingObject<>();
        final TrackValueObjectWrapper secondTrackValueObjectWrapper = new TrackValueObjectWrapper();
        final TrackValueObject secondTrack = new TrackValueObject();
        secondTrack.setLocal(false);
        secondTrackValueObjectWrapper.setTrack(secondTrack);
        secondPagingObject.setItems(Collections.singletonList(secondTrackValueObjectWrapper));

        when(spotifyApiMock.getTracksFromPlaylist(url)).thenThrow(new AccessTokenExpiredException()).thenReturn(firstPagingObject);
        when(spotifyApiMock.getTracksFromPlaylist(urlForNextTracks)).thenReturn(secondPagingObject);

        final TrackDTO firstTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(firstTrack)).thenReturn(firstTrackDTO);
        final TrackDTO secondTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(secondTrack)).thenReturn(secondTrackDTO);

        // when
        final List<TrackDTO> resultedTracks = testSubject.getTracksFromPlaylist(userName, playlistId);

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, userName);
        assertThat(resultedTracks).containsExactly(firstTrackDTO, secondTrackDTO);
    }

    @Test
    public void setAuthenticationToApiOnAddingTracksIntoPlaylist() {
        // given
        final String userName = "userName";
        final String playlistId = "playlistId";
        final ArrayList<TrackDTO> tracks = new ArrayList<>();
        tracks.add(new TrackDTO());

        // when
        testSubject.addTracksToPlaylist(userName, playlistId, tracks);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, userName);
    }

    @Test
    public void addTracksToPlaylist() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";
        final String playlistId = "playlistId";
        final String url = String.format("https://api.spotify.com/v1/playlists/%s/tracks", playlistId);

        final ArrayList<TrackDTO> tracks = new ArrayList<>();
        final TrackDTO track = new TrackDTO();
        final String expectedTrackUri = "spotifyTrackUri";
        track.setUri(expectedTrackUri);
        tracks.add(track);

        // when
        testSubject.addTracksToPlaylist(userName, playlistId, tracks);

        // then
        // noinspection unchecked
        final ArgumentCaptor<List<String>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(spotifyApiMock).postTracksToPlaylist(eq(url), listArgumentCaptor.capture());

        assertThat(listArgumentCaptor.getValue()).containsExactly(expectedTrackUri);
    }

    @Test
    public void addTracksToPlaylistInTwoPostRequests() throws AccessTokenExpiredException {
        // given
        final SpotifyPlaylistPublicApiService testSubjectSpy = spy(testSubject);

        final String userName = "userName";
        final String playlistId = "playlistId";
        final String url = String.format("https://api.spotify.com/v1/playlists/%s/tracks", playlistId);

        final ArrayList<TrackDTO> tracks = new ArrayList<>();

        final TrackDTO firstTrack = new TrackDTO();
        final String firstTrackUri = "spotifyTrackUri";
        firstTrack.setUri(firstTrackUri);
        tracks.add(firstTrack);

        final TrackDTO secondTrack = new TrackDTO();
        final String secondTrackUri = "secondTrackUri";
        secondTrack.setUri(secondTrackUri);
        tracks.add(secondTrack);

        final List<List<String>> splittedList = new ArrayList<>();
        final List<String> firstTrackList = new ArrayList<>();
        firstTrackList.add(firstTrackUri);
        final List<String> secondTrackList = new ArrayList<>();
        secondTrackList.add(secondTrackUri);
        splittedList.add(firstTrackList);
        splittedList.add(secondTrackList);
        when(testSubjectSpy.splitList(anyList())).thenReturn(splittedList);

        // when
        testSubjectSpy.addTracksToPlaylist(userName, playlistId, tracks);

        // then
        // noinspection unchecked
        final ArgumentCaptor<List<String>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(spotifyApiMock, times(2)).postTracksToPlaylist(eq(url), listArgumentCaptor.capture());

        final List<List<String>> resultedTrackUris = listArgumentCaptor.getAllValues();
        assertThat(resultedTrackUris).containsExactly(firstTrackList, secondTrackList);
    }

    @Test
    public void retryAddingTracksToPlaylistIfAccessTokenExpiredExceptionIsThrown() throws AccessTokenExpiredException {
        // given
        final String userName = "userName";
        final String playlistId = "playlistId";
        final String url = String.format("https://api.spotify.com/v1/playlists/%s/tracks", playlistId);

        final ArrayList<TrackDTO> tracks = new ArrayList<>();
        final TrackDTO track = new TrackDTO();
        final String expectedTrackUri = "spotifyTrackUri";
        track.setUri(expectedTrackUri);
        tracks.add(track);

        final String snapshotId = "snapshotId";
        when(spotifyApiMock.postTracksToPlaylist(eq(url), anyList()))
                .thenThrow(new AccessTokenExpiredException())
                .thenReturn(snapshotId);

        // when
        testSubject.addTracksToPlaylist(userName, playlistId, tracks);

        // then
        // noinspection unchecked
        final ArgumentCaptor<List<String>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(spotifyApiMock, times(2)).postTracksToPlaylist(eq(url), listArgumentCaptor.capture());

        final List<String> expectedUriList = Collections.singletonList(expectedTrackUri);
        assertThat(listArgumentCaptor.getAllValues())
                .containsExactly(expectedUriList, expectedUriList);
    }

    @Test
    public void retryAddingTracksToPlaylistInTwoPostRequestsIfAccessTokenExpiredExceptionIsThrown() throws AccessTokenExpiredException {
        // given
        final SpotifyPlaylistPublicApiService testSubjectSpy = spy(testSubject);

        final String userName = "userName";
        final String playlistId = "playlistId";
        final String url = String.format("https://api.spotify.com/v1/playlists/%s/tracks", playlistId);

        final ArrayList<TrackDTO> tracks = new ArrayList<>();

        final TrackDTO firstTrack = new TrackDTO();
        final String firstTrackUri = "spotifyTrackUri";
        firstTrack.setUri(firstTrackUri);
        tracks.add(firstTrack);

        final TrackDTO secondTrack = new TrackDTO();
        final String secondTrackUri = "secondTrackUri";
        secondTrack.setUri(secondTrackUri);
        tracks.add(secondTrack);

        final List<List<String>> splittedList = new ArrayList<>();
        final List<String> firstTrackList = new ArrayList<>();
        firstTrackList.add(firstTrackUri);
        final List<String> secondTrackList = new ArrayList<>();
        secondTrackList.add(secondTrackUri);
        splittedList.add(firstTrackList);
        splittedList.add(secondTrackList);
        when(testSubjectSpy.splitList(anyList())).thenReturn(splittedList);

        final String snapshotId = "snapshotId";
        final String secondSnapshotId = "secondSnapshotId";
        when(spotifyApiMock.postTracksToPlaylist(eq(url), anyList()))
                .thenThrow(new AccessTokenExpiredException())
                .thenReturn(snapshotId)
                .thenReturn(secondSnapshotId);

        // when
        testSubjectSpy.addTracksToPlaylist(userName, playlistId, tracks);

        // then
        // noinspection unchecked
        final ArgumentCaptor<List<String>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(spotifyApiMock, times(3)).postTracksToPlaylist(eq(url), listArgumentCaptor.capture());

        final List<List<String>> resultedTrackUris = listArgumentCaptor.getAllValues();
        assertThat(resultedTrackUris).containsExactly(firstTrackList, firstTrackList, secondTrackList);
    }
}