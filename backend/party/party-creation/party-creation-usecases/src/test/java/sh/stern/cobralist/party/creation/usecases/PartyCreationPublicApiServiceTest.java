package sh.stern.cobralist.party.creation.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.api.PartyCreationRequestDTO;
import sh.stern.cobralist.party.creation.api.PartyCreationResponseDTO;
import sh.stern.cobralist.party.creation.dataaccess.port.PartyCreationDataService;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.creation.usecases.mapper.PartyDTOToPartyCreationResponseDTOMapper;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.streaming.dataaccess.port.StreamingDataService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyCreationPublicApiServiceTest {

    private PartyCreationPublicApiService testSubject;

    @Mock
    private PartyCodeGenerator partyCodeGeneratorMock;

    @Mock
    private PlaylistService playlistServiceMock;

    @Mock
    private PartyCreationDataService partyCreationDataServiceMock;

    @Mock
    private StreamingDataService streamingDataServiceMock;

    @Mock
    private PartyDTOToPartyCreationResponseDTOMapper partyDTOToPartyCreationResponseDTOMapperMock;

    @Before
    public void setUp() {
        this.testSubject = new PartyCreationPublicApiService(
                playlistServiceMock,
                partyCreationDataServiceMock,
                streamingDataServiceMock,
                partyDTOToPartyCreationResponseDTOMapperMock,
                partyCodeGeneratorMock);
    }

    @Test
    public void createParty() {
        // given
        final String partyCode = "1234s56";
        final long userId = 123L;
        final String username = "username";

        final PartyCreationRequestDTO partyRequestDTO = new PartyCreationRequestDTO();
        final String partyName = "PartyName";
        partyRequestDTO.setPartyName(partyName);
        final String partyDescription = "PartyDescription";
        partyRequestDTO.setDescription(partyDescription);
        final boolean downVoting = true;
        partyRequestDTO.setDownVoting(downVoting);
        @SuppressWarnings("squid:S2068") // credentials are allowed in tests
        final String partyPassword = "password";
        partyRequestDTO.setPassword(partyPassword);
        final String basePlaylistId = "basePlaylistId";
        partyRequestDTO.setBasePlaylistId(basePlaylistId);

        final String streamingProviderId = "userStreamingProviderId";
        when(streamingDataServiceMock.getUsersProviderId(userId)).thenReturn(streamingProviderId);

        final PlaylistDTO playlistDTO = new PlaylistDTO();
        when(playlistServiceMock.createPlaylist(username, streamingProviderId, partyName, partyDescription)).thenReturn(playlistDTO);

        when(partyCodeGeneratorMock.generatePartyCode()).thenReturn(partyCode);

        final PartyDTO partyDTO = new PartyDTO();
        partyDTO.setPartyCode(partyCode);
        when(partyCreationDataServiceMock.createParty(userId, partyName, partyPassword, downVoting, partyDescription, partyCode)).thenReturn(partyDTO);

        final PartyCreationResponseDTO expectedPartyResponseDTO = new PartyCreationResponseDTO();
        when(partyDTOToPartyCreationResponseDTOMapperMock.map(partyDTO, playlistDTO)).thenReturn(expectedPartyResponseDTO);

        // when
        final PartyCreationResponseDTO resultedPartyResponseDTO = testSubject.createParty(username, userId, partyRequestDTO);

        // then
        verify(partyCreationDataServiceMock).savePlaylistWithTracks(partyCode, playlistDTO);
        assertThat(resultedPartyResponseDTO).isEqualTo(expectedPartyResponseDTO);
    }

    @Test
    public void addMusicTracksFromBaseplaylistToNewPlaylist() {
        // given
        final String partyCode = "123456";
        final long userId = 123L;

        final PartyCreationRequestDTO partyRequestDTO = new PartyCreationRequestDTO();
        final String partyName = "PartyName";
        partyRequestDTO.setPartyName(partyName);
        final String partyDescription = "PartyDescription";
        partyRequestDTO.setDescription(partyDescription);
        final boolean downVoting = true;
        partyRequestDTO.setDownVoting(downVoting);
        @SuppressWarnings("squid:S2068") // credentials are allowed in tests
        final String partyPassword = "partyPassword";
        partyRequestDTO.setPassword(partyPassword);
        final String username = "username";
        final String basePlaylistId = "basePlaylistId";
        partyRequestDTO.setBasePlaylistId(basePlaylistId);

        final String streamingProviderId = "userStreamingProviderId";
        when(streamingDataServiceMock.getUsersProviderId(userId)).thenReturn(streamingProviderId);

        final PlaylistDTO playlistDTO = new PlaylistDTO();
        final String playlistId = "playlistId";
        playlistDTO.setId(playlistId);
        when(playlistServiceMock.createPlaylist(username, streamingProviderId, partyName, partyDescription)).thenReturn(playlistDTO);

        final TrackDTO trackDTO = new TrackDTO();
        final List<TrackDTO> tracksFromPlaylist = Collections.singletonList(trackDTO);
        when(playlistServiceMock.getTracksFromPlaylist(username, basePlaylistId)).thenReturn(tracksFromPlaylist);

        when(partyCodeGeneratorMock.generatePartyCode()).thenReturn(partyCode);

        final PartyDTO partyDTO = new PartyDTO();
        partyDTO.setPartyCode(partyCode);
        when(partyCreationDataServiceMock.createParty(userId, partyName, partyPassword, downVoting, partyDescription, partyCode)).thenReturn(partyDTO);

        // when
        testSubject.createParty(username, userId, partyRequestDTO);

        // then
        final ArgumentCaptor<PlaylistDTO> playlistDTOArgumentCaptor = ArgumentCaptor.forClass(PlaylistDTO.class);
        verify(partyDTOToPartyCreationResponseDTOMapperMock).map(eq(partyDTO), playlistDTOArgumentCaptor.capture());
        final PlaylistDTO resultedPlaylistDTO = playlistDTOArgumentCaptor.getValue();
        assertThat(resultedPlaylistDTO.getTracks()).containsExactly(trackDTO);
    }
}