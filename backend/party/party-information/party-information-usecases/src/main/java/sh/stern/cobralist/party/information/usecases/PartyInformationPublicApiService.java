package sh.stern.cobralist.party.information.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.current.track.api.CurrentTrackService;
import sh.stern.cobralist.party.information.api.PartyInformationService;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestService;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;

@Service
public class PartyInformationPublicApiService implements PartyInformationService {

    private final PartySecurityService partySecurityService;
    private final CurrentTrackService currentTrackService;
    private final MusicRequestService musicRequestService;

    @Autowired
    public PartyInformationPublicApiService(PartySecurityService partySecurityService,
                                            CurrentTrackService currentTrackService,
                                            MusicRequestService musicRequestService) {
        this.partySecurityService = partySecurityService;
        this.currentTrackService = currentTrackService;
        this.musicRequestService = musicRequestService;
    }

    @Override
    public PartyInformationDTO getPartyInformation(UserPrincipal userPrincipal, String partyCode) {
        partySecurityService.checkGetPartyInformationPermission(userPrincipal, partyCode);

        final PartyInformationDTO partyInformationDTO = new PartyInformationDTO();
        partyInformationDTO.setCurrentPlayback(currentTrackService.getCurrentTrack(partyCode));

        final List<MusicRequestDTO> musicRequests = musicRequestService.getMusicRequests(partyCode, userPrincipal.getId(), hasRoleUser(userPrincipal));
        partyInformationDTO.setMusicRequests(musicRequests);

        return partyInformationDTO;
    }

    private boolean hasRoleUser(@CurrentUser UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()));
    }
}
