package sh.stern.cobralist.party.player.api;

import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface PartyPlayerService {
    void startParty(UserPrincipal userPrincipal, String partyCode);

    void stopParty(UserPrincipal userPrincipal, String partyCode);

    void skipMusicRequest(UserPrincipal userPrincipal, String partyCode);
}
