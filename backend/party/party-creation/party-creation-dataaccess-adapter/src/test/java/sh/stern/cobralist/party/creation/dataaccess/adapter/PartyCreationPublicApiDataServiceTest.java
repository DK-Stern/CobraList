package sh.stern.cobralist.party.creation.dataaccess.adapter;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.dataaccess.adapter.mapper.PartyToPartyDTOMapper;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.domain.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyCreationPublicApiDataServiceTest {

    private PartyCreationPublicApiDataService testSubject;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Mock
    private PartyToPartyDTOMapper partyToPartyDTOMapperMock;

    @Mock
    private PlaylistRepository playlistRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new PartyCreationPublicApiDataService(partyRepositoryMock,
                playlistRepositoryMock,
                userRepositoryMock,
                musicRequestRepositoryMock,
                partyToPartyDTOMapperMock);
    }

    @Test
    public void getParty() {
        // given
        final String partyCode = "123456";

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        final PartyDTO expectedPartyDTO = new PartyDTO();
        when(partyToPartyDTOMapperMock.map(party)).thenReturn(expectedPartyDTO);

        // when
        final PartyDTO resultedPartyDTO = testSubject.getParty(partyCode);

        // then
        assertThat(resultedPartyDTO).isEqualTo(expectedPartyDTO);
    }

    @Test
    public void createParty() {
        // given
        final long userId = 1L;
        final String partyName = "partyName";
        final String password = "password";
        final boolean downVoting = false;
        final String description = "description";
        final String partyCode = "partyCode";

        final User user = new User();
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        final ArgumentCaptor<Party> partyArgumentCaptor = ArgumentCaptor.forClass(Party.class);
        final Party savedParty = new Party();
        when(partyRepositoryMock.save(partyArgumentCaptor.capture())).thenReturn(savedParty);

        // when
        testSubject.createParty(userId, partyName, password, downVoting, description, partyCode);

        // then
        verify(partyToPartyDTOMapperMock).map(savedParty);
        final Party expectedParty = partyArgumentCaptor.getValue();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(expectedParty.getPartyCode()).isEqualTo(partyCode);
        softly.assertThat(expectedParty.getUser()).isEqualTo(user);
        softly.assertThat(expectedParty.getName()).isEqualTo(partyName);
        softly.assertThat(expectedParty.getCreationDate()).isInstanceOf(Date.class);
        softly.assertThat(expectedParty.getPassword()).isEqualTo(password);
        softly.assertThat(expectedParty.isDownVotable()).isEqualTo(downVoting);
        softly.assertThat(expectedParty.getDescription()).isEqualTo(description);
        softly.assertAll();
    }

    @Test
    public void savePlaylistWithTracksSavesPlaylistCorrectly() {
        // given
        final String partyCode = "partyCode";
        final PlaylistDTO playlistDTO = new PlaylistDTO();

        final String playlistName = "playlistName";
        playlistDTO.setName(playlistName);

        final String playlistId = "playlistId";
        playlistDTO.setPlaylistId(playlistId);

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        final ArgumentCaptor<Playlist> playlistArgumentCaptor = ArgumentCaptor.forClass(Playlist.class);
        when(playlistRepositoryMock.saveAndFlush(playlistArgumentCaptor.capture())).thenReturn(new Playlist());

        // when
        testSubject.savePlaylistWithTracks(partyCode, playlistDTO);

        // then
        final Playlist expectedPlaylist = playlistArgumentCaptor.getValue();
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(expectedPlaylist.getName()).isEqualTo(playlistName);
        softly.assertThat(expectedPlaylist.getPlaylistId()).isEqualTo(playlistId);
        softly.assertThat(expectedPlaylist.getParty()).isEqualTo(party);
        softly.assertAll();
    }

    @Test
    public void savePlaylistWithTracksSavesMusicRequestsCorrectly() {
        // given
        final String partyCode = "partyCode";
        final PlaylistDTO playlistDTO = new PlaylistDTO();

        final ArrayList<TrackDTO> tracks = new ArrayList<>();
        final TrackDTO track = new TrackDTO();

        final String trackId = "trackId";
        track.setId(trackId);
        final String uri = "uri";
        track.setUri(uri);
        final String name = "name";
        track.setName(name);
        final String imageUrl = "imageUrl";
        track.setImageUrl(imageUrl);
        final int imageWidth = 3;
        track.setImageWidth(imageWidth);
        final int imageHeight = 4;
        track.setImageHeight(imageHeight);
        final ArrayList<String> artists = new ArrayList<>();
        track.setArtists(artists);
        final String albumName = "albumName";
        track.setAlbumName(albumName);
        final int duration = 1234;
        track.setDuration(duration);

        tracks.add(track);
        playlistDTO.setTracks(tracks);

        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(new Party()));

        final Playlist savedPlaylist = new Playlist();
        when(playlistRepositoryMock.saveAndFlush(any(Playlist.class))).thenReturn(savedPlaylist);

        // when
        testSubject.savePlaylistWithTracks(partyCode, playlistDTO);

        // then
        final ArgumentCaptor<MusicRequest> musicRequestArgumentCaptor = ArgumentCaptor.forClass(MusicRequest.class);
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequestArgumentCaptor.capture());
        final MusicRequest expectedMusicRequest = musicRequestArgumentCaptor.getValue();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(expectedMusicRequest.getTrackId()).isEqualTo(trackId);
        softly.assertThat(expectedMusicRequest.getImageUrl()).isEqualTo(imageUrl);
        softly.assertThat(expectedMusicRequest.getImageWidth()).isEqualTo(imageWidth);
        softly.assertThat(expectedMusicRequest.getImageHeight()).isEqualTo(imageHeight);
        softly.assertThat(expectedMusicRequest.getTitle()).isEqualTo(name);
        softly.assertThat(expectedMusicRequest.getArtist()).isEqualTo(artists);
        softly.assertThat(expectedMusicRequest.getUri()).isEqualTo(uri);
        softly.assertThat(expectedMusicRequest.getPlaylist()).isEqualTo(savedPlaylist);
        softly.assertThat(expectedMusicRequest.getDuration()).isEqualTo(duration);
        softly.assertAll();
    }
}