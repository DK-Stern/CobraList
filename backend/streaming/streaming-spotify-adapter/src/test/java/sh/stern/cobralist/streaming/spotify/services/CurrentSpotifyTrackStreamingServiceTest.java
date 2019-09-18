package sh.stern.cobralist.streaming.spotify.services;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.streaming.exceptions.AccessTokenExpiredException;
import sh.stern.cobralist.streaming.spotify.SpotifyApi;
import sh.stern.cobralist.streaming.spotify.mapper.TrackValueObjectToTrackDTOMapper;
import sh.stern.cobralist.streaming.spotify.valueobjects.CurrentPlaybackObject;
import sh.stern.cobralist.streaming.spotify.valueobjects.TrackValueObject;
import sh.stern.cobralist.user.domain.StreamingProvider;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CurrentSpotifyTrackStreamingServiceTest {

    private CurrentSpotifyTrackStreamingService testSubject;

    @Mock
    private SpotifyApi spotifyApiMock;

    @Mock
    private TrackValueObjectToTrackDTOMapper trackValueObjectToTrackDTOMapperMock;

    @Before
    public void setUp() {
        testSubject = new CurrentSpotifyTrackStreamingService(spotifyApiMock, trackValueObjectToTrackDTOMapperMock);
    }

    @Test
    public void getCurrentPlaybackFromParties() throws AccessTokenExpiredException {
        // given
        final String creatorName = "creatorName";
        final String partyCode = "partyCode";
        final ActivePartyDTO activePartyDTO = new ActivePartyDTO();
        activePartyDTO.setCreatorName(creatorName);
        activePartyDTO.setPartyCode(partyCode);

        final CurrentPlaybackObject currentPlaybackObject = new CurrentPlaybackObject();
        final TrackValueObject trackValueObject = new TrackValueObject();
        currentPlaybackObject.setItem(trackValueObject);
        final boolean isPlaying = true;
        currentPlaybackObject.setPlaying(isPlaying);
        final int progressMs = 12346;
        currentPlaybackObject.setProgressMs(progressMs);
        when(spotifyApiMock.getCurrentPlayback("https://api.spotify.com/v1/me/player")).thenReturn(currentPlaybackObject);

        final TrackDTO trackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(trackValueObject)).thenReturn(trackDTO);

        // when
        final Map<String, CurrentPlaybackDTO> currentTrackFromParties = testSubject.getCurrentTrackFromParties(Collections.singletonList(activePartyDTO));

        // then
        final CurrentPlaybackDTO currentPlaybackDTO = currentTrackFromParties.get(partyCode);
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(currentPlaybackDTO.getCurrentTrack()).isEqualTo(trackDTO);
        softly.assertThat(currentPlaybackDTO.getPlaying()).isTrue();
        softly.assertThat(currentPlaybackDTO.getProgressMs()).isEqualTo(progressMs);
        softly.assertAll();
    }

    @Test
    public void retryGettingCurrentPlaybackFromPartiesOnAccessTokenExpiredException() throws AccessTokenExpiredException {
        // given
        final String creatorEmail = "creatorEmail";
        final String partyCode = "partyCode";
        final ActivePartyDTO activePartyDTO = new ActivePartyDTO();
        activePartyDTO.setCreatorEmail(creatorEmail);
        activePartyDTO.setPartyCode(partyCode);

        final CurrentPlaybackObject currentPlaybackObject = new CurrentPlaybackObject();
        final TrackValueObject trackValueObject = new TrackValueObject();
        currentPlaybackObject.setItem(trackValueObject);
        final boolean isPlaying = true;
        currentPlaybackObject.setPlaying(isPlaying);
        final int progressMs = 12346;
        currentPlaybackObject.setProgressMs(progressMs);
        when(spotifyApiMock.getCurrentPlayback("https://api.spotify.com/v1/me/player"))
                .thenThrow(new AccessTokenExpiredException())
                .thenReturn(currentPlaybackObject);

        final TrackDTO trackDTO = new TrackDTO();
        when(trackValueObjectToTrackDTOMapperMock.map(trackValueObject)).thenReturn(trackDTO);

        // when
        testSubject.getCurrentTrackFromParties(Collections.singletonList(activePartyDTO));

        // then
        verify(spotifyApiMock, times(2)).setAuthentication(StreamingProvider.spotify, creatorEmail);
    }

    @Test
    public void onIllegalStateExceptionOnGettingCurrentPlaybackTheDtoShouldHasPlayingStatusFalse() throws AccessTokenExpiredException {
        // given
        final String creatorName = "creatorName";
        final String partyCode = "partyCode";
        final ActivePartyDTO activePartyDTO = new ActivePartyDTO();
        activePartyDTO.setCreatorName(creatorName);
        activePartyDTO.setPartyCode(partyCode);

        when(spotifyApiMock.getCurrentPlayback("https://api.spotify.com/v1/me/player")).thenThrow(new IllegalStateException());

        // when
        final Map<String, CurrentPlaybackDTO> currentTrackFromParties = testSubject.getCurrentTrackFromParties(Collections.singletonList(activePartyDTO));

        // then
        final CurrentPlaybackDTO currentPlaybackDTO = currentTrackFromParties.get(partyCode);
        assertThat(currentPlaybackDTO.getPlaying()).isFalse();
    }

    @Test
    public void removeTrackFromPlaylist() throws AccessTokenExpiredException {
        // given
        final String username = "username";
        final String playlistId = "playlistId";
        final String trackId = "trackId";

        // when
        testSubject.removeTrackFromPlaylist(username, playlistId, trackId);

        // then
        verify(spotifyApiMock).setAuthentication(StreamingProvider.spotify, username);
        verify(spotifyApiMock).removeTrackFromPlaylist("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks", trackId);
    }
}