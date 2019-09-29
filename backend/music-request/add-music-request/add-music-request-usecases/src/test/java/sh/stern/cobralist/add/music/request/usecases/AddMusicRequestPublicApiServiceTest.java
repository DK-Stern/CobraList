package sh.stern.cobralist.add.music.request.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTO;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTONotFulfilledException;
import sh.stern.cobralist.add.music.request.api.MusicRequestAlreadyExistException;
import sh.stern.cobralist.add.music.request.dataaccess.port.AddMusicRequestDataService;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddMusicRequestPublicApiServiceTest {

    private AddMusicRequestPublicApiService testSubject;

    @Mock
    private PartySecurityService partySecurityServiceMock;

    @Mock
    private MusicRequestPositionService musicRequestPositionServiceMock;

    @Mock
    private PlaylistService playlistServiceMock;

    @Mock
    private AddMusicRequestDataService addMusicRequestDataServiceMock;

    @Before
    public void setUp() {
        testSubject = new AddMusicRequestPublicApiService(partySecurityServiceMock, musicRequestPositionServiceMock, playlistServiceMock, addMusicRequestDataServiceMock);
    }

    @Test
    public void checkParticipationPermission() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        final String partyCode = "partyCode";
        addMusicRequestDTO.setPartyCode(partyCode);
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(12);
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when
        testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        verify(partySecurityServiceMock).checkGetPartyInformationPermission(userPrincipal, partyCode);
    }

    @Test
    public void throwsExceptionIfMusicRequestAlreadyExist() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        final String partyCode = "partyCode";
        addMusicRequestDTO.setPartyCode(partyCode);
        final TrackDTO trackDTO = new TrackDTO();
        final String trackStreamingId = "id";
        trackDTO.setId(trackStreamingId);
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(12);
        addMusicRequestDTO.setTrackDTO(trackDTO);

        final long playlistId = 123L;
        when(musicRequestPositionServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        when(addMusicRequestDataServiceMock.doesMusicRequestExist(playlistId, trackStreamingId)).thenReturn(true);

        // when u. then
        assertThatExceptionOfType(MusicRequestAlreadyExistException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("MusicRequest mit streaming id '" + trackStreamingId + "' existiert bereits.");
    }

    @Test
    public void addTrackToSpotifyWithPositionAndPersist() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String username = "username";
        userPrincipal.setEmail(username);
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        final String partyCode = "partyCode";
        addMusicRequestDTO.setPartyCode(partyCode);
        final TrackDTO trackDTO = new TrackDTO();
        final String trackStreamingId = "id";
        trackDTO.setId(trackStreamingId);
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(12);
        addMusicRequestDTO.setTrackDTO(trackDTO);

        final long playlistId = 123L;
        when(musicRequestPositionServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        final int position = 5;
        when(musicRequestPositionServiceMock.calculateMusicRequestPosition(playlistId, 0)).thenReturn(position);

        final String playlistStreamingId = "playlistSteamingId";
        when(addMusicRequestDataServiceMock.getPlaylistStreamingId(playlistId)).thenReturn(playlistStreamingId);

        // when
        testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        verify(playlistServiceMock).addTracksWithPositionToPlaylist(username, playlistStreamingId, Collections.singletonList(trackDTO), position);
        verify(musicRequestPositionServiceMock).persistNewMusicRequest(playlistId, trackDTO, position);
    }

    @Test
    public void throwsExceptionIfRequestDTOMissingPartyCode() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'partyCode' ist leer.");
    }

    @Test
    public void throwsExceptionIfRequestDTOMissingTrackDTO() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO' ist leer.");
    }

    @Test
    public void throwsExceptionIfIDOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        addMusicRequestDTO.setTrackDTO(new TrackDTO());

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.id' ist leer.");
    }

    @Test
    public void throwsExceptionIfArtistsOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.artists' ist leer.");
    }

    @Test
    public void throwsExceptionIfArtistsOfTrackDTOInRequestIsEmpty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.emptyList());
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.artists' ist leer.");
    }

    @Test
    public void throwsExceptionIfArtistsOfTrackDTOInRequestIsOnlyWhitespace() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("  "));
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.artists' ist leer.");
    }

    @Test
    public void throwsExceptionIfUriOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.uri' ist leer.");
    }

    @Test
    public void throwsExceptionIfNameOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.name' ist leer.");
    }

    @Test
    public void throwsExceptionIfAlbumNameOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.albumName' ist leer.");
    }

    @Test
    public void throwsExceptionIfImageUrlOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.imageUrl' ist leer.");
    }

    @Test
    public void throwsExceptionIfImageWidthOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.imageWidth' ist leer.");
    }

    @Test
    public void throwsExceptionIfImageHeightOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.imageHeight' ist leer.");
    }

    @Test
    public void throwsExceptionIfDurationOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        addMusicRequestDTO.setTrackDTO(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'trackDTO.duration' ist leer.");
    }
}