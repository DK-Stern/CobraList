package sh.stern.cobralist.party.creation.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.api.PartyCreationRequestDTO;
import sh.stern.cobralist.party.creation.api.PartyCreationService;
import sh.stern.cobralist.party.creation.dataaccess.port.PartyCreationDataService;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.party.information.api.PartyInformationService;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.streaming.dataaccess.port.StreamingDataService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;

@Service
public class PartyCreationPublicApiService implements PartyCreationService {

    private final PlaylistService playlistService;
    private final PartyCreationDataService partyCreationDataService;
    private final StreamingDataService streamingDataService;
    private final PartyInformationService partyInformationService;
    private final PartyCodeGenerator partyCodeGenerator;

    @Autowired
    public PartyCreationPublicApiService(PlaylistService playlistService,
                                         PartyCreationDataService partyCreationDataService,
                                         StreamingDataService streamingDataService,
                                         PartyCodeGenerator partyCodeGenerator,
                                         PartyInformationService partyInformationService) {
        this.playlistService = playlistService;
        this.partyCreationDataService = partyCreationDataService;
        this.streamingDataService = streamingDataService;
        this.partyInformationService = partyInformationService;
        this.partyCodeGenerator = partyCodeGenerator;
    }

    public PartyInformationDTO createParty(UserPrincipal userPrincipal, PartyCreationRequestDTO partyRequestDTO) {
        final Long userId = userPrincipal.getId();
        final String usersProviderId = streamingDataService.getUsersProviderId(userId);
        final String username = userPrincipal.getUsername();
        final PlaylistDTO playlistDTO = playlistService.createPlaylist(username, usersProviderId, partyRequestDTO.getPartyName(), partyRequestDTO.getDescription());

        if (!partyRequestDTO.getBasePlaylistId().equals("none")) {
            final List<TrackDTO> tracksFromPlaylist = playlistService.getTracksFromPlaylist(username, partyRequestDTO.getBasePlaylistId());
            playlistService.addTracksToPlaylist(username, playlistDTO.getPlaylistId(), tracksFromPlaylist);
            playlistDTO.setTracks(tracksFromPlaylist);
        }

        final PartyDTO partyDTO = partyCreationDataService.createParty(
                userId,
                partyRequestDTO.getPartyName(),
                partyRequestDTO.getPassword(),
                partyRequestDTO.isDownVoting(),
                partyRequestDTO.getDescription(),
                partyCodeGenerator.generatePartyCode());

        partyCreationDataService.savePlaylistWithTracks(partyDTO.getPartyCode(), playlistDTO);

        return partyInformationService.getPartyInformation(userPrincipal, partyDTO.getPartyCode());
    }
}
