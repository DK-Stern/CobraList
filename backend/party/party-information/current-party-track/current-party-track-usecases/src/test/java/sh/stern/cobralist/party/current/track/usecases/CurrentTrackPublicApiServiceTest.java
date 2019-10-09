package sh.stern.cobralist.party.current.track.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.current.track.dataaccess.port.CurrentTrackDataService;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.streaming.api.CurrentTrackStreamingService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class CurrentTrackPublicApiServiceTest {

    private CurrentTrackPublicApiService testSubject;

    @Mock
    private CurrentTrackDataService currentTrackDataServiceMock;

    @Mock
    private CurrentTrackStreamingService currentTrackStreamingServiceMock;

    @Before
    public void setUp() {
        testSubject = new CurrentTrackPublicApiService(currentTrackDataServiceMock, currentTrackStreamingServiceMock);
    }

    @Test
    public void getCurrentTrackFromParty() {
        // given
        final String partyCode = "partyCode";
        final String creatorName = "creatorName";
        final String creatorEmail = "creatorEmail";

        final ActivePartyDTO activePartyDTO = new ActivePartyDTO();
        activePartyDTO.setPartyCode(partyCode);
        activePartyDTO.setCreatorName(creatorName);
        activePartyDTO.setCreatorEmail(creatorEmail);
        final List<ActivePartyDTO> activeParties = Collections.singletonList(activePartyDTO);
        when(currentTrackDataServiceMock.getActiveParties()).thenReturn(activeParties);

        final HashMap<String, CurrentPlaybackDTO> currentPartyTracks = new HashMap<>();
        final CurrentPlaybackDTO expectedCurrentTrack = new CurrentPlaybackDTO();
        expectedCurrentTrack.setPlaying(true);
        currentPartyTracks.put(partyCode, expectedCurrentTrack);
        when(currentTrackStreamingServiceMock.getCurrentTrackFromParties(activeParties)).thenReturn(currentPartyTracks);

        testSubject.refreshCurrentTracks();

        // when
        final CurrentPlaybackDTO currentTrack = testSubject.getCurrentTrack(partyCode);

        // then
        assertThat(currentTrack).isEqualTo(expectedCurrentTrack);
    }

    @Test
    public void getCurrentTrackFromPartyIsNullIfPartyIsNotActive() {
        // given
        final String partyCode = "partyCode";

        // when
        final CurrentPlaybackDTO currentTrack = testSubject.getCurrentTrack(partyCode);

        // then
        assertThat(currentTrack).isNull();
    }

    @Test
    public void changePartyActiveStatusIfPlayingIsFalse() {
        // given
        final String partyCode = "partyCode";
        final String creatorName = "creatorName";
        final String creatorEmail = "creatorEmail";

        final ActivePartyDTO activePartyDTO = new ActivePartyDTO();
        activePartyDTO.setPartyCode(partyCode);
        activePartyDTO.setCreatorName(creatorName);
        activePartyDTO.setCreatorEmail(creatorEmail);
        final List<ActivePartyDTO> activeParties = Collections.singletonList(activePartyDTO);
        when(currentTrackDataServiceMock.getActiveParties()).thenReturn(activeParties);

        final HashMap<String, CurrentPlaybackDTO> currentPartyTracks = new HashMap<>();
        final CurrentPlaybackDTO currentPlaybackDTO = new CurrentPlaybackDTO();
        currentPlaybackDTO.setPlaying(false);
        currentPartyTracks.put(partyCode, currentPlaybackDTO);
        when(currentTrackStreamingServiceMock.getCurrentTrackFromParties(activeParties)).thenReturn(currentPartyTracks);

        testSubject.refreshCurrentTracks();

        // when
        testSubject.getCurrentTrack(partyCode);

        // then
        verify(currentTrackDataServiceMock).changePartyActiveStatus(partyCode, false);
    }

    @Test
    public void changeMusicRequestPlayedStatusIfPlayedStatusIsFalse() {
        // given
        final String partyCode = "partyCode";
        final String creatorName = "creatorName";
        final String creatorEmail = "creatorEmail";

        final ActivePartyDTO activePartyDTO = new ActivePartyDTO();
        activePartyDTO.setPartyCode(partyCode);
        activePartyDTO.setCreatorName(creatorName);
        activePartyDTO.setCreatorEmail(creatorEmail);
        final List<ActivePartyDTO> activeParties = Collections.singletonList(activePartyDTO);
        when(currentTrackDataServiceMock.getActiveParties()).thenReturn(activeParties);

        final HashMap<String, CurrentPlaybackDTO> currentPartyTracks = new HashMap<>();
        final CurrentPlaybackDTO currentPlaybackDTO = new CurrentPlaybackDTO();
        final TrackDTO currentTrack = new TrackDTO();
        final String currentTrackId = "12";
        currentTrack.setStreamingId(currentTrackId);
        currentPlaybackDTO.setCurrentTrack(currentTrack);
        currentPlaybackDTO.setPlaying(false);
        currentPartyTracks.put(partyCode, currentPlaybackDTO);
        when(currentTrackStreamingServiceMock.getCurrentTrackFromParties(activeParties)).thenReturn(currentPartyTracks);

        final long playlistId = 1234L;
        when(currentTrackDataServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);
        final long musicRequestId = 23L;
        when(currentTrackDataServiceMock.getMusicRequestId(playlistId, currentTrackId)).thenReturn(musicRequestId);

        final String playlistStreamingId = "playlistStreamingId";
        when(currentTrackDataServiceMock.getPlaylistStreamingId(partyCode)).thenReturn(playlistStreamingId);

        testSubject.refreshCurrentTracks();

        // when
        testSubject.getCurrentTrack(partyCode);

        // then
        verify(currentTrackStreamingServiceMock).removeTrackFromPlaylist(creatorEmail, playlistStreamingId, currentTrackId);
        verify(currentTrackDataServiceMock).changeMusicRequestPlayedStatus(musicRequestId, true);
    }
}