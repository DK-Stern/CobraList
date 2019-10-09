package sh.stern.cobralist.search.music.request.usecases;

import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.search.music.request.api.SearchMusicRequestService;
import sh.stern.cobralist.search.music.request.dataaccess.port.SearchMusicRequestDataService;
import sh.stern.cobralist.search.music.request.domain.SearchMusicRequestDTO;
import sh.stern.cobralist.streaming.api.SearchTrackStreamingService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchMusicRequestPublicApiService implements SearchMusicRequestService {

    private final PartySecurityService partySecurityService;
    private final SearchTrackStreamingService searchTrackStreamingService;
    private final SearchMusicRequestDataService searchMusicRequestDataService;
    private final TrackDTOToSearchMusicRequestDTOMapper trackDTOToSearchMusicRequestDTOMapper;

    public SearchMusicRequestPublicApiService(PartySecurityService partySecurityService,
                                              SearchTrackStreamingService searchTrackStreamingService,
                                              SearchMusicRequestDataService searchMusicRequestDataService,
                                              TrackDTOToSearchMusicRequestDTOMapper trackDTOToSearchMusicRequestDTOMapper) {
        this.partySecurityService = partySecurityService;
        this.searchTrackStreamingService = searchTrackStreamingService;
        this.searchMusicRequestDataService = searchMusicRequestDataService;
        this.trackDTOToSearchMusicRequestDTOMapper = trackDTOToSearchMusicRequestDTOMapper;
    }

    @Override
    public List<SearchMusicRequestDTO> searchMusicRequest(UserPrincipal userPrincipal, String partyCode, String searchString) {
        partySecurityService.checkGetPartyInformationPermission(userPrincipal, partyCode);

        String creatorStreamingId = searchMusicRequestDataService.getPartyCreatorStreamingId(partyCode);
        final List<TrackDTO> trackDTOS = searchTrackStreamingService.searchTrack(creatorStreamingId, searchString);
        final Long playlistId = searchMusicRequestDataService.getPlaylistId(partyCode);

        return trackDTOS.stream()
                .map(trackDTOToSearchMusicRequestDTOMapper::map)
                .map(searchMusicRequestDTO -> {
                    searchMusicRequestDTO.setAlreadyInPlaylist(searchMusicRequestDataService.isMusicRequestAlreadyInPlaylist(playlistId, searchMusicRequestDTO.getTrackId()));
                    return searchMusicRequestDTO;
                })
                .collect(Collectors.toList());
    }
}
