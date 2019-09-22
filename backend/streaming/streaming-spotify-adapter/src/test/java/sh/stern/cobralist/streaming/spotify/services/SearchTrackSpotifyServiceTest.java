package sh.stern.cobralist.streaming.spotify.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.streaming.spotify.mapper.TrackValueObjectToTrackDTOMapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.SearchTrackValueObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.TrackValueObject;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchTrackSpotifyServiceTest {

    private SearchTrackSpotifyService testSubject;

    @Mock
    private SpotifyApi spotifyApiMock;

    @Mock
    private TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapperMock;

    @Before
    public void setUp() {
        testSubject = new SearchTrackSpotifyService(spotifyApiMock, trackValueObjectToTrackDTOMapperMock);
    }

    @Test
    public void searchTrack() throws AccessTokenExpiredException {
        // given
        final String username = "username";
        final String searchString = "abba";

        final SearchTrackValueObject tracksValueObject = new SearchTrackValueObject();
        final TrackValueObject trackValueObject = new TrackValueObject();
        trackValueObject.setLocal(false);
        tracksValueObject.setItems(Collections.singletonList(trackValueObject));
        when(spotifyApiMock.searchTrack("https://api.spotify.com/v1/search?q=abba&type=track")).thenReturn(tracksValueObject);

        final TrackDTO expectedTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(trackValueObject)).thenReturn(expectedTrackDTO);

        // when
        final List<TrackDTO> resultedTrackDTOS = testSubject.searchTrack(username, searchString);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, username);
        assertThat(resultedTrackDTOS).containsExactly(expectedTrackDTO);
    }

    @Test
    public void retrySearchingTrackAfterAccessTokenExpiredException() throws AccessTokenExpiredException {
        // given
        final String username = "username";
        final String searchString = "abba";

        final SearchTrackValueObject tracksValueObject = new SearchTrackValueObject();
        final TrackValueObject trackValueObject = new TrackValueObject();
        trackValueObject.setLocal(false);
        tracksValueObject.setItems(Collections.singletonList(trackValueObject));
        when(spotifyApiMock.searchTrack("https://api.spotify.com/v1/search?q=abba&type=track")).thenThrow(new AccessTokenExpiredException()).thenReturn(tracksValueObject);

        final TrackDTO expectedTrackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(trackValueObject)).thenReturn(expectedTrackDTO);

        // when
        final List<TrackDTO> resultedTrackDTOS = testSubject.searchTrack(username, searchString);

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, username);
        assertThat(resultedTrackDTOS).containsExactly(expectedTrackDTO);
    }
}