package sh.stern.cobralist.party.delete.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.delete.api.DeletePartyService;
import sh.stern.cobralist.party.delete.dataaccess.port.DeletePartyDataService;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@Service
public class DeletePartyPublicApiService implements DeletePartyService {

    private final PartySecurityService partySecurityService;
    private final DeletePartyDataService deletePartyDataService;
    private final PlaylistService playlistService;

    @Autowired
    public DeletePartyPublicApiService(PartySecurityService partySecurityService,
                                       DeletePartyDataService deletePartyDataService,
                                       PlaylistService playlistService) {
        this.partySecurityService = partySecurityService;
        this.deletePartyDataService = deletePartyDataService;
        this.playlistService = playlistService;
    }

    @Override
    public void deleteParty(UserPrincipal userPrincipal, String partyCode) {
        partySecurityService.checkIsPartyCreator(userPrincipal, partyCode);
        String partyStreamingId = deletePartyDataService.getPlaylistStreamingId(partyCode);
        playlistService.deleteParty(userPrincipal.getUsername(), partyStreamingId);
        deletePartyDataService.deleteParty(partyCode);
    }
}
