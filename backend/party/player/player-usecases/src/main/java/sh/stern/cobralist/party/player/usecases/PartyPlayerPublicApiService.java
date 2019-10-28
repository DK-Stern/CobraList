package sh.stern.cobralist.party.player.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.player.api.PartyPlayerService;
import sh.stern.cobralist.party.player.dataaccess.port.PartyPlayerDataService;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.streaming.api.PlayerStreamingService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@Service
public class PartyPlayerPublicApiService implements PartyPlayerService {

    private final PartySecurityService partySecurityService;
    private final PartyPlayerDataService partyPlayerDataService;
    private final PlayerStreamingService playerStreamingService;

    @Autowired
    public PartyPlayerPublicApiService(PartySecurityService partySecurityService,
                                       PartyPlayerDataService partyPlayerDataService,
                                       PlayerStreamingService playerStreamingService) {
        this.partySecurityService = partySecurityService;
        this.partyPlayerDataService = partyPlayerDataService;
        this.playerStreamingService = playerStreamingService;
    }

    @Override
    public void startParty(UserPrincipal userPrincipal, String partyCode) {
        partySecurityService.checkIsPartyCreator(userPrincipal, partyCode);
        playerStreamingService.startPlaylist(userPrincipal.getUsername(), partyPlayerDataService.getPlaylistStreamingId(partyCode));
        partyPlayerDataService.setPartyStatus(partyCode, true);
    }

    @Override
    public void stopParty(UserPrincipal userPrincipal, String partyCode) {
        partySecurityService.checkIsPartyCreator(userPrincipal, partyCode);
        playerStreamingService.stopPlaylist(userPrincipal.getUsername());
        partyPlayerDataService.setPartyStatus(partyCode, false);
    }

    @Override
    public void skipMusicRequest(UserPrincipal userPrincipal, String partyCode) {
        partySecurityService.checkIsPartyCreator(userPrincipal, partyCode);
        playerStreamingService.skipSong(userPrincipal.getUsername());
    }
}
