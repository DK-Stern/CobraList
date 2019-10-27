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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(12);
        addMusicRequestDTO.setTrack(trackDTO);

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
        trackDTO.setStreamingId(trackStreamingId);
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(12);
        addMusicRequestDTO.setTrack(trackDTO);

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
        trackDTO.setStreamingId(trackStreamingId);
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(12);
        addMusicRequestDTO.setTrack(trackDTO);

        final long playlistId = 123L;
        when(musicRequestPositionServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        final int position = 5;
        when(musicRequestPositionServiceMock.getPositionOfLastMusicRequest(playlistId)).thenReturn(position);

        final String playlistStreamingId = "playlistSteamingId";
        when(addMusicRequestDataServiceMock.getPlaylistStreamingId(playlistId)).thenReturn(playlistStreamingId);

        // when
        testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        final int expectedPosition = position + 1;
        verify(playlistServiceMock).addTracksWithPositionToPlaylist(username, playlistStreamingId, Collections.singletonList(trackDTO), expectedPosition);
        verify(musicRequestPositionServiceMock).persistNewMusicRequest(playlistId, trackDTO, expectedPosition);
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
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track' ist leer.");
    }

    @Test
    public void throwsExceptionIfIDOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        addMusicRequestDTO.setTrack(new TrackDTO());

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.id' ist leer.");
    }

    @Test
    public void throwsExceptionIfArtistsOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.artists' ist leer.");
    }

    @Test
    public void throwsExceptionIfArtistsOfTrackDTOInRequestIsEmpty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.emptyList());
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.artists' ist leer.");
    }

    @Test
    public void throwsExceptionIfArtistsOfTrackDTOInRequestIsOnlyWhitespace() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("  "));
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.artists' ist leer.");
    }

    @Test
    public void throwsExceptionIfUriOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.uri' ist leer.");
    }

    @Test
    public void throwsExceptionIfNameOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.name' ist leer.");
    }

    @Test
    public void throwsExceptionIfAlbumNameOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.albumName' ist leer.");
    }

    @Test
    public void throwsExceptionIfImageUrlOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.imageUrl' ist leer.");
    }

    @Test
    public void throwsExceptionIfImageWidthOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.imageWidth' ist leer.");
    }

    @Test
    public void throwsExceptionIfImageHeightOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.imageHeight' ist leer.");
    }

    @Test
    public void throwsExceptionIfDurationOfTrackDTOInRequestIsMissing() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        addMusicRequestDTO.setPartyCode("partyCode");
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        addMusicRequestDTO.setTrack(trackDTO);

        // when u. then
        assertThatExceptionOfType(AddMusicRequestDTONotFulfilledException.class)
                .isThrownBy(() -> testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO))
                .withMessage("Das Request-Objekt ist nicht vollstaendig. Das Attribute 'track.duration' ist leer.");
    }

    @Test
    public void newMusicRequestPositionIsTheSameAsMusicRequestsWithNegativeRating() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String userPrincipalUsername = "email";
        userPrincipal.setEmail(userPrincipalUsername);

        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        final String partyCode = "partyCode";
        addMusicRequestDTO.setPartyCode(partyCode);
        final TrackDTO trackDTO = new TrackDTO();
        trackDTO.setStreamingId("id");
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(123);
        addMusicRequestDTO.setTrack(trackDTO);

        final long playlistId = 1L;
        when(musicRequestPositionServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        final int expectedPositionOfNegativeMusicRequest = 2;
        when(musicRequestPositionServiceMock.getPositionOfMusicRequestWithNegativeRatingAndLowestPosition(playlistId)).thenReturn(Optional.of(expectedPositionOfNegativeMusicRequest));

        final String playlistStreamingId = "streamingId";
        when(addMusicRequestDataServiceMock.getPlaylistStreamingId(playlistId)).thenReturn(playlistStreamingId);

        // when
        testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        verify(playlistServiceMock).addTracksWithPositionToPlaylist(eq(userPrincipalUsername), eq(playlistStreamingId), anyList(), eq(expectedPositionOfNegativeMusicRequest));
        verify(musicRequestPositionServiceMock).persistNewMusicRequest(playlistId, trackDTO, expectedPositionOfNegativeMusicRequest);
    }

    @Test
    public void newMusicRequestPositionIsZeroIfPlaylistIsEmpty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String userPrincipalUsername = "email";
        userPrincipal.setEmail(userPrincipalUsername);

        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        final String partyCode = "partyCode";
        addMusicRequestDTO.setPartyCode(partyCode);
        final TrackDTO trackDTO = new TrackDTO();
        final String trackStramingId = "id";
        trackDTO.setStreamingId(trackStramingId);
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(123);
        addMusicRequestDTO.setTrack(trackDTO);

        final long playlistId = 1L;
        when(musicRequestPositionServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        when(musicRequestPositionServiceMock.isPlaylistEmpty(playlistId)).thenReturn(true);

        final String playlistStreamingId = "streamingId";
        when(addMusicRequestDataServiceMock.getPlaylistStreamingId(playlistId)).thenReturn(playlistStreamingId);

        // when
        testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        verify(playlistServiceMock).addTracksWithPositionToPlaylist(eq(userPrincipalUsername), eq(playlistStreamingId), anyList(), eq(0));
        verify(musicRequestPositionServiceMock).persistNewMusicRequest(playlistId, trackDTO, 0);
    }

    @Test
    public void newMusicRequestPositionIsLastPositionOfPlaylistIfPlaylistIsNotEmpty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String userPrincipalUsername = "email";
        userPrincipal.setEmail(userPrincipalUsername);

        final AddMusicRequestDTO addMusicRequestDTO = new AddMusicRequestDTO();
        final String partyCode = "partyCode";
        addMusicRequestDTO.setPartyCode(partyCode);
        final TrackDTO trackDTO = new TrackDTO();
        final String trackStramingId = "id";
        trackDTO.setStreamingId(trackStramingId);
        trackDTO.setArtists(Collections.singletonList("artist"));
        trackDTO.setUri("uri");
        trackDTO.setName("name");
        trackDTO.setAlbumName("albumName");
        trackDTO.setImageUrl("imageUrl");
        trackDTO.setImageWidth(1);
        trackDTO.setImageHeight(1);
        trackDTO.setDuration(123);
        addMusicRequestDTO.setTrack(trackDTO);

        final long playlistId = 1L;
        when(musicRequestPositionServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        final int lastPositionOfMusicRequest = 2;
        when(musicRequestPositionServiceMock.getPositionOfLastMusicRequest(playlistId)).thenReturn(lastPositionOfMusicRequest);

        final String playlistStreamingId = "streamingId";
        when(addMusicRequestDataServiceMock.getPlaylistStreamingId(playlistId)).thenReturn(playlistStreamingId);

        int expectedPosition = lastPositionOfMusicRequest + 1;

        // when
        testSubject.addMusicRequest(userPrincipal, addMusicRequestDTO);

        // then
        verify(playlistServiceMock).addTracksWithPositionToPlaylist(eq(userPrincipalUsername), eq(playlistStreamingId), anyList(), eq(expectedPosition));
        verify(musicRequestPositionServiceMock).persistNewMusicRequest(playlistId, trackDTO, expectedPosition);
    }
}