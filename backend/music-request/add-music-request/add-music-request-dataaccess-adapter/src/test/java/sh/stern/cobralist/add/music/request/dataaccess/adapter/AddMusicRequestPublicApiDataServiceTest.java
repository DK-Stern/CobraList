package sh.stern.cobralist.add.music.request.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.exceptions.PlaylistNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddMusicRequestPublicApiDataServiceTest {

    private AddMusicRequestPublicApiDataService testSubject;

    @Mock
    private PlaylistRepository playlistRepositoryMock;

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new AddMusicRequestPublicApiDataService(playlistRepositoryMock, musicRequestRepositoryMock);
    }

    @Test
    public void getPlaylistStreamingId() {
        // given
        final long playlistId = 123L;
        final String expectedStreamingId = "streamingId";

        final Playlist playlist = new Playlist();
        playlist.setPlaylistId(expectedStreamingId);
        when(playlistRepositoryMock.findById(playlistId)).thenReturn(Optional.of(playlist));

        // when
        final String resultedId = testSubject.getPlaylistStreamingId(playlistId);

        // then
        assertThat(resultedId).isEqualTo(expectedStreamingId);
    }

    @Test
    public void getPlaylistStreamingIdThrowsExceptionIfPlaylistNotFound() {
        // given
        final long playlistId = 123L;
        when(playlistRepositoryMock.findById(playlistId)).thenReturn(Optional.empty());

        // when u. then
        assertThatExceptionOfType(PlaylistNotFoundException.class)
                .isThrownBy(() -> testSubject.getPlaylistStreamingId(playlistId))
                .withMessage("Playlist mit der id '" + playlistId + "' konnte nicht gefunden werden.");
    }

    @Test
    public void doesMusicRequestExistReturnsFalseIfMusicRequestNotFound() {
        // given
        final long playlistId = 123L;
        final String trackStreamingId = "trackStreamingId";

        when(musicRequestRepositoryMock.findByPlaylist_IdAndTrackIdAndIsPlayed(playlistId, trackStreamingId, false)).thenReturn(Optional.empty());

        // when
        final boolean result = testSubject.doesMusicRequestExist(playlistId, trackStreamingId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void doesMusicRequestExistReturnsFalseIfMusicRequestExistButIsPlayed() {
        // given
        final long playlistId = 123L;
        final String trackStreamingId = "trackStreamingId";

        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlayed(true);
        when(musicRequestRepositoryMock.findByPlaylist_IdAndTrackIdAndIsPlayed(playlistId, trackStreamingId, false)).thenReturn(Optional.of(musicRequest));

        // when
        final boolean result = testSubject.doesMusicRequestExist(playlistId, trackStreamingId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void doesMusicRequestExistReturnsTrueIfMusicRequestExistsAndIsNotPlayed() {
        // given
        final long playlistId = 123L;
        final String trackStreamingId = "trackStreamingId";

        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlayed(false);
        when(musicRequestRepositoryMock.findByPlaylist_IdAndTrackIdAndIsPlayed(playlistId, trackStreamingId, false)).thenReturn(Optional.of(musicRequest));

        // when
        final boolean result = testSubject.doesMusicRequestExist(playlistId, trackStreamingId);

        // then
        assertThat(result).isTrue();
    }
}