package sh.stern.cobralist.search.musicrequest.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class SearchMusicRequestPublicApiDataServiceTest {

    private SearchMusicRequestPublicApiDataService testSubject;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new SearchMusicRequestPublicApiDataService(partyRepositoryMock, musicRequestRepositoryMock);
    }

    @Test
    public void getPlaylistIdForPartyCode() {
        // given
        final String partyCode = "partyCode";
        final Party party = new Party();
        final Playlist playlist = new Playlist();
        final long expectedPlaylistId = 12L;
        playlist.setId(expectedPlaylistId);
        party.setPlaylist(playlist);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final Long resultedPlaylistId = testSubject.getPlaylistId(partyCode);

        // then
        assertThat(resultedPlaylistId).isEqualTo(expectedPlaylistId);
    }

    @Test
    public void getPlaylistIdForPartyCodeThrowsExceptionIfPartyNotExists() {
        // given
        final String partyCode = "partyCode";
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.empty());

        // when
        assertThatExceptionOfType(PartyNotFoundException.class)
                .isThrownBy(() -> testSubject.getPlaylistId(partyCode))
                .withMessage("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.");
    }

    @Test
    public void musicRequestIsNotInPlaylistShouldReturnFalseIfMusicRequestDoesntExist() {
        // given
        final long playlistId = 12L;
        final String trackId = "trackId";

        when(musicRequestRepositoryMock.findByPlaylist_IdAndTrackId(playlistId, trackId)).thenReturn(Optional.empty());

        // when
        final boolean isInPlaylist = testSubject.isMusicRequestAlreadyInPlaylist(playlistId, trackId);

        // then
        assertThat(isInPlaylist).isFalse();
    }

    @Test
    public void musicRequestIsNotInPlaylistShouldReturnFalseIfMusicRequestExistButIsAlreadyPlayed() {
        // given
        final long playlistId = 12L;
        final String trackId = "trackId";

        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlayed(true);
        when(musicRequestRepositoryMock.findByPlaylist_IdAndTrackId(playlistId, trackId)).thenReturn(Optional.of(musicRequest));

        // when
        final boolean isInPlaylist = testSubject.isMusicRequestAlreadyInPlaylist(playlistId, trackId);

        // then
        assertThat(isInPlaylist).isFalse();
    }

    @Test
    public void musicRequestIsNotInPlaylistShouldReturnTrueIfMusicRequestExistButIsNotPlayed() {
        // given
        final long playlistId = 12L;
        final String trackId = "trackId";

        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlayed(false);
        when(musicRequestRepositoryMock.findByPlaylist_IdAndTrackId(playlistId, trackId)).thenReturn(Optional.of(musicRequest));

        // when
        final boolean isInPlaylist = testSubject.isMusicRequestAlreadyInPlaylist(playlistId, trackId);

        // then
        assertThat(isInPlaylist).isTrue();
    }
}