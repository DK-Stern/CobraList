package sh.stern.cobralist.position.music.request.dataaccess.adapter;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.PlaylistNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MusicRequestPositionPublicApiDataServiceTest {

    private MusicRequestPositionPublicApiDataService testSubject;

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Mock
    private PlaylistRepository playlistRepositoryMock;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new MusicRequestPositionPublicApiDataService(musicRequestRepositoryMock, playlistRepositoryMock, partyRepositoryMock);
    }

    @Test
    public void getPlaylistId() {
        // given
        final String partyCode = "partyCode";

        final Party party = new Party();
        final Playlist playlist = new Playlist();
        final long expectedPlaylistId = 123L;
        playlist.setId(expectedPlaylistId);
        party.setPlaylist(playlist);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final Long resultedPlaylistId = testSubject.getPlaylistId(partyCode);

        // then
        assertThat(resultedPlaylistId).isEqualTo(expectedPlaylistId);
    }

    @Test
    public void getPlaylistIdThrowsExceptionIfPartyNotFound() {
        // given
        final String partyCode = "partyCode";

        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.empty());

        // when u. then
        assertThatExceptionOfType(PartyNotFoundException.class)
                .isThrownBy(() -> testSubject.getPlaylistId(partyCode))
                .withMessage("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.");
    }

    @Test
    public void increaseMusicRequestPositions() {
        // given
        final long playlistId = 12L;
        final int position = 3;

        // when
        testSubject.incrementMusicRequestPositions(playlistId, position);

        // then
        verify(musicRequestRepositoryMock).incrementPositions(playlistId, position);
    }

    @Test
    public void saveMusicRequest() {
        // given
        final long playlistId = 123L;
        final int position = 3;

        final TrackDTO trackDTO = new TrackDTO();
        final String trackStreamingId = "id";
        trackDTO.setStreamingId(trackStreamingId);
        final List<String> artists = Collections.singletonList("artist");
        trackDTO.setArtists(artists);
        final String uri = "uri";
        trackDTO.setUri(uri);
        final String name = "name";
        trackDTO.setName(name);
        final String albumName = "albumName";
        trackDTO.setAlbumName(albumName);
        final String imageUrl = "imageUrl";
        trackDTO.setImageUrl(imageUrl);
        final int imageWidth = 1;
        trackDTO.setImageWidth(imageWidth);
        final int imageHeight = 1;
        trackDTO.setImageHeight(imageHeight);
        final int duration = 12;
        trackDTO.setDuration(duration);

        final ArgumentCaptor<MusicRequest> musicRequestArgumentCaptor = ArgumentCaptor.forClass(MusicRequest.class);

        final Playlist playlist = new Playlist();
        when(playlistRepositoryMock.findById(playlistId)).thenReturn(Optional.of(playlist));

        // when
        testSubject.saveMusicRequest(playlistId, trackDTO, position);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequestArgumentCaptor.capture());
        final MusicRequest resultedMusicRequest = musicRequestArgumentCaptor.getValue();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(resultedMusicRequest.getTrackId()).isEqualTo(trackStreamingId);
        softly.assertThat(resultedMusicRequest.getUri()).isEqualTo(uri);
        softly.assertThat(resultedMusicRequest.getImageUrl()).isEqualTo(imageUrl);
        softly.assertThat(resultedMusicRequest.getImageHeight()).isEqualTo(imageHeight);
        softly.assertThat(resultedMusicRequest.getImageWidth()).isEqualTo(imageWidth);
        softly.assertThat(resultedMusicRequest.getDuration()).isEqualTo(duration);
        softly.assertThat(resultedMusicRequest.getUpVotes()).isZero();
        softly.assertThat(resultedMusicRequest.getDownVotes()).isZero();
        softly.assertThat(resultedMusicRequest.getRating()).isZero();
        softly.assertThat(resultedMusicRequest.getPosition()).isEqualTo(position);
        softly.assertThat(resultedMusicRequest.getPlayed()).isFalse();
        softly.assertThat(resultedMusicRequest.getPlaylist()).isEqualTo(playlist);
        softly.assertThat(resultedMusicRequest.getArtist()).isEqualTo(artists);
        softly.assertThat(resultedMusicRequest.getTitle()).isEqualTo(name);
        softly.assertAll();
    }

    @Test
    public void saveMusicRequestThrowsExceptionIfPlaylistNotFound() {
        // given
        final long playlistId = 123L;
        final TrackDTO trackDTO = new TrackDTO();
        final int position = 5;

        // when u. then
        assertThatExceptionOfType(PlaylistNotFoundException.class)
                .isThrownBy(() -> testSubject.saveMusicRequest(playlistId, trackDTO, position))
                .withMessage("Playlist mit der id '" + playlistId + "' konnte nicht gefunden werden.");
    }
}